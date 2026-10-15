-- PostgreSQL-only conversion of legacy wall-clock timestamps to UTC-aware
-- timestamptz, moved out of the portable (H2-compatible) baseline.

ALTER TABLE marketplace.users
    ALTER COLUMN locked_until TYPE TIMESTAMP WITH TIME ZONE
        USING locked_until AT TIME ZONE 'UTC',
    ALTER COLUMN last_login_at TYPE TIMESTAMP WITH TIME ZONE
        USING last_login_at AT TIME ZONE 'UTC';
