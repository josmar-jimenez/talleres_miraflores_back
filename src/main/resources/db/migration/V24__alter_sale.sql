CREATE SEQUENCE IF NOT EXISTS sale_id_sequence;

ALTER TABLE sale DROP CONSTRAINT IF EXISTS product_id ;
ALTER TABLE sale DROP CONSTRAINT IF EXISTS store ;

ALTER TABLE sale RENAME COLUMN store to store_id;
ALTER TABLE sale RENAME COLUMN "user" to user_id;

ALTER TABLE sale ADD COLUMN created DATE DEFAULT now();

ALTER TABLE sale ALTER COLUMN id SET DEFAULT nextval('sale_id_sequence');