CREATE SEQUENCE IF NOT EXISTS inventory_id_sequence;

ALTER TABLE inventory DROP CONSTRAINT IF EXISTS status_id ;
ALTER TABLE inventory DROP CONSTRAINT IF EXISTS user_id ;
ALTER TABLE inventory RENAME COLUMN "user" TO user_id;
ALTER TABLE inventory RENAME COLUMN store TO store_id;

ALTER TABLE inventory ALTER COLUMN id SET DEFAULT nextval('inventory_id_sequence');