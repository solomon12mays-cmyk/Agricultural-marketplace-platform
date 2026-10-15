-- Each organization account owns a single organization registration profile.
CREATE UNIQUE INDEX uq_organizations_user_id
    ON marketplace.organizations (user_id);
