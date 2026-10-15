package com.agricultural.marketplace.shared.security;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.agricultural.marketplace.shared.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Issues and validates self-signed HS256 JSON Web Tokens for API access.
 *
 * <p>The signing secret comes from configuration ({@code app.security.jwt.secret});
 * when it is not provided a per-instance random secret is generated so the
 * service still works locally. Tokens are validated for signature, issuer and
 * expiry; refresh tokens are additionally checked against their
 * {@code token_use=refresh} claim by the callers.</p>
 */
@Service
public class JwtTokenService {

    private static final String ALG_HEADER_VALUE = "HS256";
    private static final Base64.Encoder B64URL = Base64.getUrlEncoder().withoutPadding();

    private final byte[] signingSecret;
    private final String issuer;
    private final long accessTtlSeconds;
    private final long refreshTtlSeconds;
    private final Clock clock;

    public JwtTokenService(
            @Value("${app.security.jwt.secret:}") String configuredSecret,
            @Value("${app.security.jwt.issuer:agricultural-marketplace}") String issuer,
            @Value("${app.security.jwt.access-token-ttl-seconds:900}") long accessTtlSeconds,
            @Value("${app.security.jwt.refresh-token-ttl-seconds:1209600}") long refreshTtlSeconds) {
        // SHA-256 of the configured value (or a random placeholder) gives a
        // stable-length key regardless of how long the configured secret is.
        this.signingSecret = sha256(StringUtils.hasText(configuredSecret)
                ? configuredSecret
                : UUID.randomUUID() + ":" + UUID.randomUUID());

        this.issuer = issuer;
        this.accessTtlSeconds = accessTtlSeconds;
        this.refreshTtlSeconds = refreshTtlSeconds;
        this.clock = Clock.systemUTC();
    }

    public String createAccessToken(String subject, List<String> roles) {
        Instant now = clock.instant();
        return encodeHeader()
                + '.' + encodeClaims(subject, now, now.plusSeconds(accessTtlSeconds), "access",
                        roles == null ? List.of() : roles, null);
    }

    public String createAccessToken(String subject, List<String> roles, String jti) {
        Instant now = clock.instant();
        return encodeHeader()
                + '.' + encodeClaims(subject, now, now.plusSeconds(accessTtlSeconds), "access",
                        roles == null ? List.of() : roles, jti);
    }

    public String createRefreshToken(String subject, String sessionId) {
        Instant now = clock.instant();
        return encodeHeader()
                + '.' + encodeClaims(subject, now, now.plusSeconds(refreshTtlSeconds), "refresh",
                        List.of(), sessionId);
    }

    public boolean isValid(String token) {
        try {
            verifySignature(token);
            Claims claims = parseClaims(token);
            return claims.isNotExpired(clock.instant(), 0) && issuer.equals(claims.issuer());
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * Validates token integrity, issuer and expiry and returns its claims.
     */
    public Claims claims(String token) {
        verifySignature(token);
        Claims claims = parseClaims(token);
        if (!issuer.equals(claims.issuer())) {
            throw new UnauthorizedException("Token was issued by an untrusted issuer");
        }
        if (!claims.isNotExpired(clock.instant(), 0)) {
            throw new UnauthorizedException("Token has expired");
        }
        return claims;
    }

    /**
     * Returns the claims without verifying expiry, for revoked-refresh
     * validation flows that handle expiry themselves.
     */
    public Claims claimsAllowExpired(String token) {
        verifySignature(token);
        return parseClaims(token);
    }

    public long accessTtlSeconds() {
        return accessTtlSeconds;
    }

    public long refreshTtlSeconds() {
        return refreshTtlSeconds;
    }

    public String jwkSetUri() {
        throw new UnsupportedOperationException(
                "This deployment issues tokens directly, no external JWK set is used");
    }

    public String issuer() {
        return issuer;
    }

    private String encodeHeader() {
        return B64URL.encodeToString(
                ("{\"alg\":\"" + ALG_HEADER_VALUE + "\",\"typ\":\"JWT\"")
                        .getBytes(StandardCharsets.UTF_8));
    }

    private String encodeClaims(String subject, Instant issuedAt, Instant expiresAt,
                                String tokenUse, List<String> roles, String reference) {
        Claims claims = new Claims(
                makeJti(), subject, issuer, issuedAt, expiresAt, tokenUse, roles, reference);
        return B64URL.encodeToString(claims.toJson().getBytes(StandardCharsets.UTF_8));
    }

    private void verifySignature(String token) {
        if (token == null || token.isBlank()) {
            throw new UnauthorizedException("A bearer token is required");
        }
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new UnauthorizedException("Malformed bearer token");
        }
        byte[] expected = hmac(parts[0] + '.' + parts[1]);
        byte[] provided;
        try {
            provided = Base64.getUrlDecoder().decode(parts[2]);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Malformed bearer token signature", e);
        }
        if (!MessageDigest.isEqual(expected, provided)) {
            throw new UnauthorizedException("Bearer token signature verification failed");
        }
    }

    private Claims parseClaims(String token) {
        String[] parts = token.split("\\.");
        try {
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            return Claims.fromJson(payload);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Malformed bearer token payload", e);
        }
    }

    private byte[] hmac(String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKey key = new SecretKeySpec(signingSecret, "HmacSHA256");
            mac.init(key);
            return mac.doFinal(payload.getBytes(StandardCharsets.US_ASCII));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("HS256 signing is unavailable in this JVM", e);
        }
    }

    private static byte[] sha256(String value) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable", e);
        }
    }

    private static String makeJti() {
        return UUID.randomUUID().toString();
    }

    /**
     * Parsed JWT claims used by the authentication filters and services.
     */
    public record Claims(String jti, String subject, String issuer, Instant issuedAt,
                         Instant expiresAt, String tokenUse, List<String> roles, String reference) {

        boolean isNotExpired(Instant now, long allowedClockSkewSeconds) {
            return expiresAt != null && now.plusSeconds(allowedClockSkewSeconds).isBefore(expiresAt);
        }

        String toJson() {
            StringBuilder json = new StringBuilder("{");
            appendJsonField(json, "jti", jti, true);
            appendJsonField(json, "sub", subject, false);
            appendJsonField(json, "iss", issuer, false);
            json.append("\"iat\":").append(issuedAt.getEpochSecond()).append(',');
            json.append("\"exp\":").append(expiresAt.getEpochSecond()).append(',');
            appendJsonField(json, "token_use", tokenUse, false);
            json.append("\"roles\":[");
            for (int i = 0; i < roles.size(); i++) {
                if (i > 0) {
                    json.append(',');
                }
                appendJsonString(json, roles.get(i));
            }
            json.append(']');
            if (reference != null) {
                json.append(',');
                appendJsonField(json, "ref", reference, true);
            }
            return json.append('}').toString();
        }

        static Claims fromJson(String json) {
            String jti = stringValue(json, "jti");
            String sub = stringValue(json, "sub");
            String iss = stringValue(json, "iss");
            String tokenUse = stringValue(json, "token_use");
            String reference = stringValue(json, "ref");
            Instant issuedAt = Instant.ofEpochSecond(longValue(json, "iat"));
            Instant expiresAt = Instant.ofEpochSecond(longValue(json, "exp"));
            List<String> roles = stringListValue(json, "roles");
            return new Claims(jti, sub, iss, issuedAt, expiresAt, tokenUse, roles, reference);
        }

        private static void appendJsonField(StringBuilder json, String name, String value, boolean quoted) {
            json.append('"').append(name).append("\":");
            if (quoted) {
                appendJsonString(json, value);
            } else {
                json.append(value == null ? "\"\"" : value);
            }
            json.append(',');
        }

        private static void appendJsonString(StringBuilder json, String value) {
            json.append('"');
            if (value != null) {
                value.chars().forEach(c -> {
                    if (c == '"' || c == '\\') {
                        json.append('\\');
                    }
                    if (c >= 32) {
                        json.append((char) c);
                    } else {
                        json.append("\\u").append(String.format("%04x", c));
                    }
                });
            }
            json.append('"');
        }
    }

    private static String stringValue(String json, String key) {
        int index = json.indexOf("\"" + key + "\"");
        if (index < 0) {
            return null;
        }
        int valueStart = json.indexOf('"', json.indexOf(':', index) + 1);
        if (valueStart < 0) {
            return null;
        }
        int valueEnd = valueStart + 1;
        while (valueEnd < json.length()) {
            char c = json.charAt(valueEnd);
            if (c == '\\') {
                valueEnd += 2;
                continue;
            }
            if (c == '"') {
                break;
            }
            valueEnd++;
        }
        if (valueEnd >= json.length()) {
            return null;
        }
        return json.substring(valueStart + 1, valueEnd).replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private static long longValue(String json, String key) {
        int keyIndex = json.indexOf("\"" + key + "\"");
        if (keyIndex < 0) {
            return 0;
        }
        int colonIndex = json.indexOf(':', keyIndex);
        if (colonIndex < 0) {
            return 0;
        }
        StringBuilder digits = new StringBuilder();
        for (int index = colonIndex + 1; index < json.length(); index++) {
            char current = json.charAt(index);
            if (Character.isDigit(current) || (current == '-' && digits.isEmpty())) {
                digits.append(current);
            } else if (!" ".equals(String.valueOf(current)) && !digits.isEmpty()) {
                break;
            }
        }
        try {
            return Long.parseLong(digits.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static List<String> stringListValue(String json, String key) {
        int keyIndex = json.indexOf("\"" + key + "\"");
        if (keyIndex < 0) {
            return List.of();
        }
        int arrayStart = json.indexOf('[', keyIndex);
        int arrayEnd = json.indexOf(']', arrayStart == -1 ? keyIndex : arrayStart);
        if (arrayStart < 0 || arrayEnd < 0) {
            return List.of();
        }
        String arrayContent = json.substring(arrayStart + 1, arrayEnd);
        if (arrayContent.isBlank()) {
            return List.of();
        }
        return java.util.Arrays.stream(arrayContent.split(","))
                .map(element -> element.trim().replaceAll("^\"|\"$", ""))
                .filter(element -> !element.isEmpty())
                .toList();
    }

    /**
     * Adapted from Spring Framework's StringUtils for internal nullability
     * checks without importing the class in this class's public API surface.
     */
    private static final class StringUtils {

        private static boolean hasText(String value) {
            return value != null && !value.isBlank();
        }
    }
}
