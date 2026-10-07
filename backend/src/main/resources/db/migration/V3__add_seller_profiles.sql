CREATE TABLE IF NOT EXISTS marketplace.sellers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    slug VARCHAR(120) NOT NULL UNIQUE,
    location VARCHAR(160) NOT NULL,
    description VARCHAR(500),
    rating DOUBLE PRECISION NOT NULL DEFAULT 4.8
);

ALTER TABLE marketplace.products
    ADD COLUMN IF NOT EXISTS seller_id BIGINT;

ALTER TABLE marketplace.products
    ADD CONSTRAINT fk_products_seller
    FOREIGN KEY (seller_id) REFERENCES marketplace.sellers(id);

INSERT INTO marketplace.sellers (name, slug, location, description, rating)
SELECT * FROM (
    VALUES
        ('Alemu Cooperative', 'alemu-cooperative', 'Amhara Region', 'Community-led farmer cooperative focused on premium cereal grains.', 4.9),
        ('Kebede Greens', 'kebede-greens', 'Oromia Region', 'Fresh produce growers supplying high-turnover market vegetables.', 4.8),
        ('Sidama Orchard Group', 'sidama-orchard-group', 'Sidama', 'Fruit and orchard producers supporting stable local distribution.', 4.7)
) AS seed(name, slug, location, description, rating)
WHERE NOT EXISTS (
    SELECT 1 FROM marketplace.sellers s WHERE s.slug = seed.slug
);

UPDATE marketplace.products
SET seller_id = (
    SELECT id FROM marketplace.sellers WHERE slug = 'alemu-cooperative'
)
WHERE slug IN ('teff', 'maize');

UPDATE marketplace.products
SET seller_id = (
    SELECT id FROM marketplace.sellers WHERE slug = 'kebede-greens'
)
WHERE slug = 'tomatoes';

UPDATE marketplace.products
SET seller_id = (
    SELECT id FROM marketplace.sellers WHERE slug = 'sidama-orchard-group'
)
WHERE slug = 'bananas';
