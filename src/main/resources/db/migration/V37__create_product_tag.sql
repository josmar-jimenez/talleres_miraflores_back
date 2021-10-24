CREATE SEQUENCE IF NOT EXISTS product_tag_id_sequence;

CREATE TABLE product_tag
(
    id                        INT          NOT NULL,
    product_id                INT          NOT NULL,
    tag_id                    INT          NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE product_tag ALTER COLUMN id SET DEFAULT nextval('product_tag_id_sequence');