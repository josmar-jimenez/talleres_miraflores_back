ALTER TABLE product ADD column code VARCHAR(50);
ALTER TABLE product ADD column color VARCHAR(100);
ALTER TABLE product ADD column tax NUMERIC;

UPDATE product SET code=concat('code-',id) WHERE code is null;
ALTER TABLE product ADD CONSTRAINT code_unique UNIQUE(code)
