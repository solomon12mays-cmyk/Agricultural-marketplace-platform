CREATE TABLE IF NOT EXISTS marketplace.product_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS marketplace.products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    slug VARCHAR(150) NOT NULL UNIQUE,
    origin VARCHAR(200) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    price NUMERIC(12,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'ETB',
    stock_quantity INTEGER NOT NULL,
    description VARCHAR(1000),
    image_url VARCHAR(255),
    status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
    category_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES marketplace.product_categories(id)
);

CREATE INDEX IF NOT EXISTS idx_products_category_id ON marketplace.products(category_id);

INSERT INTO marketplace.product_categories (name, slug, description)
VALUES
    ('Cereals', 'cereals', 'Staple grains and flour staples for households and food processors.'),
    ('Vegetables', 'vegetables', 'Fresh produce sourced from regional growers and cooperatives.'),
    ('Fruit', 'fruit', 'Seasonal fruit and orchard produce for local and wholesale buyers.');

INSERT INTO marketplace.products (
    name, slug, origin, unit, price, currency, stock_quantity, description, image_url, status, category_id
)
VALUES
    ('Teff', 'teff', 'Amhara Region', 'kg', 4200, 'ETB', 180, 'High-quality teff for injera and flour blending.', NULL, 'AVAILABLE',
     (SELECT id FROM marketplace.product_categories WHERE slug = 'cereals')),
    ('Maize', 'maize', 'Oromia Region', 'kg', 2600, 'ETB', 240, 'Clean maize for porridge, milling, and animal feed.', NULL, 'AVAILABLE',
     (SELECT id FROM marketplace.product_categories WHERE slug = 'cereals')),
    ('Tomatoes', 'tomatoes', 'SNNPR', 'kg', 1800, 'ETB', 95, 'Fresh tomatoes sourced daily from cooperative farms.', NULL, 'LOW_STOCK',
     (SELECT id FROM marketplace.product_categories WHERE slug = 'vegetables')),
    ('Bananas', 'bananas', 'Sidama', 'bunch', 3200, 'ETB', 70, 'Well-packed bananas ready for retail and wholesale delivery.', NULL, 'AVAILABLE',
     (SELECT id FROM marketplace.product_categories WHERE slug = 'fruit'));
