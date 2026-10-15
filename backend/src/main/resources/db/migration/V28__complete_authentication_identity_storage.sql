-- Reuse marketplace.users as the single account identity table used by the
-- existing UserAccount/JPA and authentication modules.
ALTER TABLE marketplace.users ADD COLUMN display_name VARCHAR(120);
-- Reuse marketplace.users as the single account identity table used by the
-- existing UserAccount/JPA and authentication modules.
ALTER TABLE marketplace.users ADD COLUMN fan_number VARCHAR(80);
-- Reuse marketplace.users as the single account identity table used by the
-- existing UserAccount/JPA and authentication modules.
ALTER TABLE marketplace.users ADD COLUMN farmer_id BIGINT;
-- Reuse marketplace.users as the single account identity table used by the
-- existing UserAccount/JPA and authentication modules.
ALTER TABLE marketplace.users ADD COLUMN organization_name VARCHAR(120);
-- Reuse marketplace.users as the single account identity table used by the
-- existing UserAccount/JPA and authentication modules.
ALTER TABLE marketplace.users ADD COLUMN contact_person_name VARCHAR(120);
-- Reuse marketplace.users as the single account identity table used by the
-- existing UserAccount/JPA and authentication modules.
ALTER TABLE marketplace.users ADD COLUMN tin_number VARCHAR(80);
-- Reuse marketplace.users as the single account identity table used by the
-- existing UserAccount/JPA and authentication modules.
ALTER TABLE marketplace.users ADD COLUMN license_number VARCHAR(80);
-- Reuse marketplace.users as the single account identity table used by the
-- existing UserAccount/JPA and authentication modules.
ALTER TABLE marketplace.users ADD COLUMN account_role VARCHAR(20) NOT NULL DEFAULT 'PERSONAL_BUYER';

UPDATE marketplace.users
   SET display_name = COALESCE(NULLIF(username, ''), 'User')
 WHERE display_name IS NULL;
ALTER TABLE marketplace.users ALTER COLUMN display_name SET NOT NULL;
ALTER TABLE marketplace.users ALTER COLUMN status SET DEFAULT 'ACTIVE';
ALTER TABLE marketplace.users ALTER COLUMN password_change_required SET DEFAULT FALSE;
ALTER TABLE marketplace.users ALTER COLUMN failed_login_attempts SET DEFAULT 0;
ALTER TABLE marketplace.users ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE marketplace.users ADD CONSTRAINT ck_users_account_role CHECK (account_role IN (
        'FARMER', 'ORGANIZATION', 'PERSONAL_BUYER', 'DRIVER', 'OPERATOR'
    ));
ALTER TABLE marketplace.users ADD CONSTRAINT ck_users_failed_login_attempts CHECK (failed_login_attempts >= 0);
ALTER TABLE marketplace.users ADD CONSTRAINT fk_users_farmer_profile FOREIGN KEY (farmer_id) REFERENCES marketplace.farmers (id);

CREATE UNIQUE INDEX uq_users_fan_number ON marketplace.users (fan_number);
CREATE UNIQUE INDEX uq_users_farmer_profile ON marketplace.users (farmer_id);
CREATE UNIQUE INDEX uq_users_tin_number ON marketplace.users (tin_number);
CREATE UNIQUE INDEX uq_users_license_number ON marketplace.users (license_number);

-- Convert legacy wall-clock timestamps to UTC-aware PostgreSQL timestamps.
-- H2 (PostgreSQL mode) creates TIMESTAMP WITH TIME ZONE columns natively; the
-- wall-clock-to-timestamptz conversion is done in db/migration/postgres (V903).


-- H2 does not parse ON CONFLICT; migrations run once per database anyway, and
-- the delete guards below keep the plain INSERTs idempotent.
DELETE FROM marketplace.role_permissions
 WHERE role_id IN (SELECT id FROM marketplace.roles)
   AND permission_id IN (SELECT id FROM marketplace.permissions);
DELETE FROM marketplace.permissions;
DELETE FROM marketplace.roles;

-- Keep the normalized RBAC tables in sync with the platform's existing
-- single-primary-role account model while the service layer migrates to
-- assigning permissions directly.
INSERT INTO marketplace.roles (code, name, description) VALUES
    ('FARMER', 'Farmer', 'Farmer account'),
    ('ORGANIZATION', 'Organization', 'Organization buyer account'),
    ('PERSONAL_BUYER', 'Personal buyer', 'Individual buyer account'),
    ('DRIVER', 'Driver', 'Driver account'),
    ('OPERATOR', 'Operator', 'Internal operator account');

INSERT INTO marketplace.permissions (code, name, description) VALUES
    ('profile:read_self', 'Read own profile', 'Read the authenticated account profile'),
    ('profile:manage_self', 'Manage own profile', 'Manage the authenticated account profile'),
    ('inventory:manage', 'Manage inventory', 'Manage farmer inventory'),
    ('listing:manage', 'Manage listings', 'Manage farmer listings'),
    ('order:create', 'Create orders', 'Create buyer orders'),
    ('order:respond', 'Respond to orders', 'Accept or reject farmer orders'),
    ('pickup:confirm', 'Confirm pickup', 'Confirm assigned order pickup'),
    ('delivery:confirm', 'Confirm delivery', 'Confirm assigned order delivery'),
    ('logistics:respond', 'Respond to assignments', 'Accept or reject driver assignments'),
    ('verification:review', 'Review verification', 'Review identity and organization verification'),
    ('operator:manage', 'Manage operator tasks', 'Manage operator assistance and tasks'),
    ('users:manage', 'Manage users', 'Manage user accounts and access'),
    ('audit:read', 'Read audit records', 'Read protected audit records'),
    ('finance:manage', 'Manage finance', 'Manage payment and payout operations');

INSERT INTO marketplace.role_permissions (role_id, permission_id)
SELECT r.id, p.id
  FROM marketplace.roles r
  JOIN marketplace.permissions p ON p.code IN (
      'profile:read_self', 'profile:manage_self', 'inventory:manage',
      'listing:manage', 'order:respond', 'pickup:confirm'
  )
 WHERE r.code = 'FARMER';
INSERT INTO marketplace.role_permissions (role_id, permission_id)
SELECT r.id, p.id
  FROM marketplace.roles r
  JOIN marketplace.permissions p ON p.code IN (
      'profile:read_self', 'profile:manage_self', 'order:create', 'delivery:confirm'
  )
 WHERE r.code IN ('ORGANIZATION', 'PERSONAL_BUYER');
INSERT INTO marketplace.role_permissions (role_id, permission_id)
SELECT r.id, p.id
  FROM marketplace.roles r
  JOIN marketplace.permissions p ON p.code IN (
      'profile:read_self', 'profile:manage_self', 'logistics:respond',
      'pickup:confirm', 'delivery:confirm'
  )
 WHERE r.code = 'DRIVER';
INSERT INTO marketplace.role_permissions (role_id, permission_id)
SELECT r.id, p.id
  FROM marketplace.roles r
  JOIN marketplace.permissions p ON p.code IN (
      'profile:read_self', 'users:manage', 'operator:manage',
      'verification:review', 'audit:read', 'finance:manage'
  )
 WHERE r.code = 'OPERATOR';

-- sync_user_primary_role trigger moved to db/migration/postgres (PostgreSQL-only).

ALTER TABLE marketplace.user_sessions ADD COLUMN session_id_hash VARCHAR(128);
ALTER TABLE marketplace.user_sessions ALTER COLUMN refresh_token_hash DROP NOT NULL;
CREATE UNIQUE INDEX uq_user_sessions_session_hash ON marketplace.user_sessions (session_id_hash);
ALTER TABLE marketplace.user_sessions ADD CONSTRAINT ck_user_session_status CHECK (
        session_status IN ('ACTIVE', 'EXPIRED', 'LOGGED_OUT')
    );
