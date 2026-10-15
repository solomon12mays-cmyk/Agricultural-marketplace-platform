-- PostgreSQL-only trigger keeping normalized RBAC tables in sync.

CREATE OR REPLACE FUNCTION marketplace.sync_user_primary_role()
RETURNS trigger LANGUAGE plpgsql AS $$
DECLARE
    role_key BIGINT;
BEGIN
    IF TG_OP = 'UPDATE' AND NEW.account_role IS NOT DISTINCT FROM OLD.account_role THEN
        RETURN NEW;
    END IF;
    SELECT id INTO role_key FROM marketplace.roles WHERE code = NEW.account_role;
    IF role_key IS NULL THEN
        RAISE EXCEPTION 'Unknown account role: %', NEW.account_role;
    END IF;
    DELETE FROM marketplace.user_roles WHERE user_id = NEW.id;
    INSERT INTO marketplace.user_roles (user_id, role_id) VALUES (NEW.id, role_key);
    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_users_sync_primary_role
AFTER INSERT OR UPDATE ON marketplace.users
FOR EACH ROW EXECUTE FUNCTION marketplace.sync_user_primary_role();

