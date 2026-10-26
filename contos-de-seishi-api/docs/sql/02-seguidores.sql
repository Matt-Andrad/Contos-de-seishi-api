USE contos_de_seishi;

CREATE TABLE seguidores (
    seguidor_id  BIGINT   NOT NULL,
    seguido_id   BIGINT   NOT NULL,
    seguido_em   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (seguidor_id, seguido_id),
    FOREIGN KEY (seguidor_id) REFERENCES usuarios (id),
    FOREIGN KEY (seguido_id)  REFERENCES usuarios (id)
);
