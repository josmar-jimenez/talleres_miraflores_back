CREATE SEQUENCE IF NOT EXISTS role_action_id_sequence;
DELETE FROM role_action;
ALTER TABLE public.role_action DROP CONSTRAINT role_action_pkey;
ALTER TABLE public.role_action
    ADD COLUMN id integer NOT NULL DEFAULT nextval('role_action_id_sequence');
ALTER TABLE public.role_action
    ADD PRIMARY KEY (id);