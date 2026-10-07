CREATE TABLE IF NOT EXISTS marketplace.shipments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    carrier VARCHAR(120) NOT NULL,
    tracking_code VARCHAR(120) NOT NULL,
    origin VARCHAR(200) NOT NULL,
    destination VARCHAR(200) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    eta VARCHAR(200) NOT NULL,
    notes VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO marketplace.shipments (order_id, carrier, tracking_code, origin, destination, status, eta, notes)
SELECT * FROM (
    VALUES
        (1, 'Ethiopian Logistics', 'ET-1001', 'Addis Ababa', 'Dire Dawa', 'IN_TRANSIT', 'Tomorrow, 3:00 PM', 'Cold chain handling required'),
        (2, 'Rift Valley Freight', 'RV-2345', 'Adama', 'Bahir Dar', 'PACKED', '2 days', 'Route confirmed with local partner')
) AS seed(order_id, carrier, tracking_code, origin, destination, status, eta, notes)
WHERE NOT EXISTS (
    SELECT 1 FROM marketplace.shipments s WHERE s.tracking_code = seed.tracking_code
);
