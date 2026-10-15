# Farmer phone OTP

## Lifecycle

Farmer registration continues through `FarmerOnboardingService`. After the account and farmer profile are created in its transaction, it issues a `REGISTRATION` code through `OtpService`. The USSD registration path uses the same service and sends the code to the caller's phone. A farmer can verify from the web client with the OTP verify endpoint or from USSD by choosing **6. Verify phone** and entering the SMS code. Successful verification timestamps `farmers.phone_verified_at`; operator identity and payout-bank review remain separate activation requirements.

Authenticated farmers can request a `PHONE_VERIFICATION` code from `/api/v1/farmers/verify-phone`. Public OTP endpoints support request, resend, and verify. Resend uses the same request limits as request.

## API

- `POST /api/v1/auth/otp/request` and `POST /api/v1/auth/otp/resend`: `{ "phoneNumber": "+2519…", "purpose": "REGISTRATION" }`
- `POST /api/v1/auth/otp/verify`: `{ "phoneNumber": "+2519…", "purpose": "REGISTRATION", "otp": "123456" }`

Request responses contain a status and expiry timestamp, never the code. Verification responds with a boolean and a status code such as `VERIFIED`, `INVALID_CODE`, `EXPIRED`, or `TOO_MANY_ATTEMPTS`. Unknown and already verified phone numbers receive the same request response, and no message is sent.

## Security and delivery

Codes are generated with `SecureRandom`, stored only as a purpose and phone scoped HMAC digest using the configured PII key, and compared in constant time. Only one challenge is stored per phone, so issuing a new code invalidates the old code across purposes. Codes expire after five minutes by default, allow five guesses, allow five requests per rolling hour, and enforce a 60-second resend cooldown. These settings are configurable.

`SmsProvider` separates code lifecycle from message delivery. The local `dev` profile enables the mock provider and code logging for manual testing. The OTP is never included in production logs, even if the mock is selected accidentally. Production defaults to an unconfigured provider that returns service unavailable; no SMS vendor credentials or integration are assumed. Configure `APP_OTP_SMS_PROVIDER` to select `mock` or `unconfigured`. A real provider can implement `SmsProvider` without changing onboarding logic.

## Configuration

`APP_OTP_LENGTH` (default 6), `APP_OTP_EXPIRATION_SECONDS` (300),
`APP_OTP_RESEND_COOLDOWN_SECONDS` (60), `APP_OTP_MAX_ATTEMPTS` (5),
`APP_OTP_MAX_REQUESTS_PER_HOUR` (5), and `APP_OTP_SMS_PROVIDER` are read from
application configuration/environment variables. Code hashing reuses
`APP_PII_ENCRYPTION_KEY`; configure the existing PII key before enabling farmer
registration. `APP_OTP_MOCK_LOG_CODE` is false by default and is enabled by the
`dev` profile for local manual verification.
