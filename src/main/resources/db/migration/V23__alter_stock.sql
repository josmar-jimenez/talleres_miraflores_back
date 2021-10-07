CREATE SEQUENCE IF NOT EXISTS stock_id_sequence;

ALTER TABLE stock DROP CONSTRAINT IF EXISTS product_id ;
ALTER TABLE stock DROP CONSTRAINT IF EXISTS store_id ;

ALTER TABLE stock RENAME COLUMN product to product_id;
ALTER TABLE stock RENAME COLUMN store to store_id;

ALTER TABLE stock ADD COLUMN status_id INT;

ALTER TABLE stock ALTER COLUMN id SET DEFAULT nextval('stock_id_sequence');