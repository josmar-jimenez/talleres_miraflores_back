ALTER TABLE tag ADD column type_id int;
ALTER TABLE tag ADD column father_id int;
ALTER TABLE tag ADD CONSTRAINT tag_unique UNIQUE(name, type_id)
