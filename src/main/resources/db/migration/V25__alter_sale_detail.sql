CREATE SEQUENCE IF NOT EXISTS sale_detail_id_sequence;

ALTER TABLE sale_detail DROP CONSTRAINT IF EXISTS product_id ;
ALTER TABLE sale_detail DROP CONSTRAINT IF EXISTS sale_id ;

ALTER TABLE sale_detail RENAME COLUMN sale to sale_id;
ALTER TABLE sale_detail RENAME COLUMN product to product_id;

ALTER TABLE sale_detail ADD COLUMN created DATE DEFAULT now();

ALTER TABLE sale_detail ALTER COLUMN id SET DEFAULT nextval('sale_detail_id_sequence');