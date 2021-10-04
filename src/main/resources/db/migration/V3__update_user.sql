CREATE SEQUENCE IF NOT EXISTS user_id_sequence;
ALTER TABLE "user" RENAME COLUMN status to status_id;
ALTER TABLE "user" ADD UNIQUE(nick);
ALTER TABLE "user" ALTER COLUMN id SET DEFAULT nextval('user_id_sequence');