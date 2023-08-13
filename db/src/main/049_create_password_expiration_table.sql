CREATE TABLE doc_main.password_expiration
(
    id            serial4      NOT NULL,
    "password"    varchar(255) NOT NULL,
    user_id       int8         NOT NULL,
    creation_time timestamp    NOT NULL DEFAULT now(),
    needs_change  bool         NOT NULL DEFAULT false,
    CONSTRAINT password_expiration_pk PRIMARY KEY (id),
    CONSTRAINT password_expiration_user_fk FOREIGN KEY (user_id) REFERENCES doc_main."user" (id)
) INHERITS (doc_main.doc_base_table);