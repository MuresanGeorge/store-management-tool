INSERT INTO categories (name, description) VALUES
('shoes', 'shoes from various brands'), --id 1
('accessories', 'accessories from different jewelry makers' ); --id 2

INSERT INTO products (name, description, category_id, price, image_url) VALUES
('Nike v2 Run', 'Shoes from nike', 1, 180.50, 'urlPathToProduct'), --id 1
('Nike P-6000', 'Shoes from nike', 1, 150, 'urlPathToProduct'), --id 2
('Pulse Ring', 'Ring from X', 2, 299.99, 'urlPathToProduct'), --id 3
('Infinity Necklace ', 'Necklace from Y', 2, 550, 'urlPathToProduct'); --id 4

INSERT INTO inventories (stock, product_id) VALUES
(10, 1),
(10, 2),
(15, 3),
(15, 4);

INSERT INTO carts (ordered, order_date, created_at) VALUES
(false, null, current_timestamp),
(false, null, current_timestamp);

INSERT INTO cart_details (cart_id, product_id, quantity) VALUES
(1, 1, 5),
(1, 2, 3),
(1, 3, 1),
(1, 4, 1),
(2, 2, 1),
(2, 2, 1);



