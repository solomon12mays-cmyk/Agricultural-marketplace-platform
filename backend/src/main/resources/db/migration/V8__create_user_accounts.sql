CREATE TABLE marketplace.user_accounts (
    id BIGSERIAL PRIMARY KEY,
    login_id VARCHAR(40) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    display_name VARCHAR(120) NOT NULL,
    phone_number VARCHAR(40) UNIQUE,
    fan_number VARCHAR(80) UNIQUE,
    farmer_id BIGINT UNIQUE REFERENCES marketplace.farmers(id),
    account_role VARCHAR(20) NOT NULL,
    must_change_password BOOLEAN NOT NULL DEFAULT FALSE
);
