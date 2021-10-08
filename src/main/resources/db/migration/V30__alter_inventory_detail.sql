ALTER TABLE inventory_detail RENAME COLUMN cant TO cant_physycal;

ALTER TABLE inventory_detail ADD COLUMN IF NOT EXISTS cant_system INTEGER;
ALTER TABLE inventory_detail ADD COLUMN IF NOT EXISTS inventory_id INTEGER;

