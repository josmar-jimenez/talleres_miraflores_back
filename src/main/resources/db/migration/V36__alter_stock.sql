ALTER TABLE stock_movement RENAME COLUMN destiny_store to destiny_store_id;
ALTER TABLE stock_movement RENAME COLUMN source_store to source_store_id;
ALTER TABLE stock_movement RENAME COLUMN product to product_id;
ALTER TABLE stock_movement RENAME COLUMN "user" to user_id;