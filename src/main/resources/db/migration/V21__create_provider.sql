CREATE SEQUENCE IF NOT EXISTS provider_id_sequence;

CREATE TABLE provider
(
    id                        INT          NOT NULL,
    short_name                VARCHAR(50)  NOT NULL,
    name                      VARCHAR(256) NOT NULL,
    address                   TEXT         NOT NULL,
    email                     VARCHAR(256) NOT NULL,
    phone                     VARCHAR(20)  NOT NULL,
    image                     TEXT,
    status_id                 INT NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE provider ALTER COLUMN id SET DEFAULT nextval('provider_id_sequence');