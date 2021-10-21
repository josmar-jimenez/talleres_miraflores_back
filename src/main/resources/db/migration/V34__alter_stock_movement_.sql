ALTER TABLE stock_movement DROP CONSTRAINT IF EXISTS movement_type ;
ALTER TABLE stock_movement DROP COLUMN IF EXISTS movement_type ;
ALTER TABLE stock_movement ADD COLUMN IF NOT EXISTS movement_type VARCHAR(50);

DROP TABLE movement_type;