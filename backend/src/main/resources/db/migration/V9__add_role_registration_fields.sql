ALTER TABLE marketplace.user_accounts ADD COLUMN IF NOT EXISTS organization_name VARCHAR(120);
ALTER TABLE marketplace.user_accounts ADD COLUMN IF NOT EXISTS contact_person_name VARCHAR(120);
ALTER TABLE marketplace.user_accounts ADD COLUMN IF NOT EXISTS tin_number VARCHAR(80);
ALTER TABLE marketplace.user_accounts ADD COLUMN IF NOT EXISTS license_number VARCHAR(80);

ALTER TABLE marketplace.user_accounts ADD CONSTRAINT uq_user_accounts_tin_number UNIQUE (tin_number);
ALTER TABLE marketplace.user_accounts ADD CONSTRAINT uq_user_accounts_license_number UNIQUE (license_number);
