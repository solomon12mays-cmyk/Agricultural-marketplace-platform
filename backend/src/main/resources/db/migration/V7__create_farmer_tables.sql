CREATE TABLE IF NOT EXISTS marketplace.farmers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    slug VARCHAR(120) NOT NULL UNIQUE,
    region VARCHAR(120) NOT NULL,
    crop_focus VARCHAR(120) NOT NULL,
    farm_size VARCHAR(80) NOT NULL,
    rating DOUBLE PRECISION NOT NULL DEFAULT 4.8,
    bio VARCHAR(500),
    contact_name VARCHAR(160) NOT NULL,
    contact_phone VARCHAR(40) NOT NULL,
    harvests_this_season INTEGER NOT NULL DEFAULT 0
);

INSERT INTO marketplace.farmers (name, slug, region, crop_focus, farm_size, rating, bio, contact_name, contact_phone, harvests_this_season)
VALUES
    ('Alemu Bekele', 'alemu-bekele', 'Amhara', 'Teff and maize', '18 hectares', 4.8, 'Smallholder farmer focused on resilient cereal production and soil regeneration.', 'Alemu Bekele', '+251911000111', 14),
    ('Selamawit Hailu', 'selamawit-hailu', 'Oromia', 'Vegetables and herbs', '7 hectares', 4.9, 'Community grower supplying fresh produce to nearby markets and institutions.', 'Selamawit Hailu', '+251911000222', 11),
    ('Tadesse Mamo', 'tadesse-mamo', 'SNNPR', 'Coffee and fruits', '12 hectares', 4.7, 'Producer focused on quality coffee and orchard fruit varieties for regional buyers.', 'Tadesse Mamo', '+251911000333', 9);
