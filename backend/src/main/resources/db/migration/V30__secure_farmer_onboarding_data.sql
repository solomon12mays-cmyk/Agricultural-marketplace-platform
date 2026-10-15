-- New farmer registrations keep identity numbers encrypted and use a keyed
-- digest only for uniqueness checks. The legacy plaintext column is nullable
-- for compatibility with the existing table but is never populated by the API.
ALTER TABLE marketplace.farmers ALTER COLUMN national_id DROP NOT NULL;
ALTER TABLE marketplace.farmers ADD COLUMN national_id_lookup_hash VARCHAR(64);
ALTER TABLE marketplace.farmers ADD COLUMN phone_verified_at TIMESTAMP WITH TIME ZONE;

CREATE UNIQUE INDEX uq_farmers_user_id
    ON marketplace.farmers (user_id);
CREATE UNIQUE INDEX uq_farmers_national_id_lookup_hash
    ON marketplace.farmers (national_id_lookup_hash);

CREATE INDEX idx_farmer_identity_verification
    ON marketplace.farmers (identity_verification_status, verification_status);
