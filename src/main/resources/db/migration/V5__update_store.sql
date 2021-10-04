CREATE SEQUENCE IF NOT EXISTS store_id_sequence;
ALTER TABLE store ALTER COLUMN id SET DEFAULT nextval('store_id_sequence');

INSERT INTO public.store(
	name, address, image, email, phone, status)
	VALUES ('La Candelaria','La Candelaria',null, 'candelaria@dos.com', '04141234444', '1');