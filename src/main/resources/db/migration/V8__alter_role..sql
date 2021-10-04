ALTER TABLE public.users DROP CONSTRAINT role_id;

ALTER TABLE public.users
    ADD CONSTRAINT role_id FOREIGN KEY (role_id)
    REFERENCES public.role (id)
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;