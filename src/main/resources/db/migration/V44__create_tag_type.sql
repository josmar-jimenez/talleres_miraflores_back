CREATE SEQUENCE IF NOT EXISTS tag_type_id_sequence;

CREATE TABLE tag_type
(
    id                        INT          NOT NULL,
    name                      VARCHAR(128) NOT NULL,
    father_id                 INT,
    PRIMARY KEY (id)
);

ALTER TABLE tag_type ALTER COLUMN id SET DEFAULT nextval('tag_type_id_sequence');