CREATE SEQUENCE IF NOT EXISTS stock_movement_id_sequence;
ALTER TABLE stock_movement ALTER COLUMN id SET DEFAULT nextval('stock_movement_id_sequence');
ALTER TABLE stock_movement ADD COLUMN IF NOT EXISTS cant INT;
