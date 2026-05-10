CREATE TABLE usuario (
    id              SERIAL PRIMARY KEY,
    nome            VARCHAR(100)  NOT NULL,
    login           VARCHAR(50)   NOT NULL UNIQUE,
    senha_hash      VARCHAR(255)  NOT NULL,
    role            VARCHAR(30)   NOT NULL DEFAULT 'ATENDENTE',
    aeroporto_iata  CHAR(3)       REFERENCES aeroporto(iata)
);
