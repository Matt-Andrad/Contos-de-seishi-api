CREATE DATABASE IF NOT EXISTS contos_de_seishi
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE contos_de_seishi;

CREATE TABLE usuarios (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname         VARCHAR(50)  NOT NULL UNIQUE,
    email            VARCHAR(150) NOT NULL UNIQUE,
    senha_hash       VARCHAR(255) NOT NULL,
    tipo             VARCHAR(20)  NOT NULL DEFAULT 'USUARIO',
    foto_arquivo_id  BIGINT       NULL,
    descricao        TEXT         NULL,
    ativo            BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);
