CREATE TABLE IF NOT EXISTS marketplace.order_headers (
    id BIGSERIAL PRIMARY KEY,
    buyer_name VARCHAR(120) NOT NULL,
    buyer_email VARCHAR(160) NOT NULL,
    shipping_address VARCHAR(500) NOT NULL,
    notes VARCHAR(1000),
    total_amount NUMERIC(12,2) NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS marketplace.order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    slug VARCHAR(150) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    price NUMERIC(12,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    quantity INTEGER NOT NULL,
    seller_name VARCHAR(120),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES marketplace.order_headers(id) ON DELETE CASCADE
);
