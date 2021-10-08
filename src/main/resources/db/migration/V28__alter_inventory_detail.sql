CREATE SEQUENCE IF NOT EXISTS inventory_detail_id_sequence;

ALTER TABLE inventory_detail DROP CONSTRAINT IF EXISTS product_id ;
ALTER TABLE inventory_detail RENAME COLUMN product TO product_id;

ALTER TABLE inventory_detail ALTER COLUMN id SET DEFAULT nextval('inventory_detail_id_sequence');