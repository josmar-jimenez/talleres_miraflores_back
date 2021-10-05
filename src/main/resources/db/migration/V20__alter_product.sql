CREATE SEQUENCE IF NOT EXISTS product_id_sequence;
ALTER TABLE product ALTER COLUMN id SET DEFAULT nextval('product_id_sequence');