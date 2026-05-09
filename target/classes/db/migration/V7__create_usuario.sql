CREATE TABLE usuario (
    id              SERIAL PRIMARY KEY,
    nome            VARCHAR(100)  NOT NULL,
    login           VARCHAR(50)   NOT NULL UNIQUE,
    senha_hash      VARCHAR(255)  NOT NULL,
    role            VARCHAR(30)   NOT NULL DEFAULT 'ATENDENTE',
    aeroporto_iata  CHAR(3)       REFERENCES aeroporto(iata)
);

INSERT INTO usuario (nome, login, senha_hash, role, aeroporto_iata) VALUES
('Admin GRU', 'admin.gru', '$2a$10$7EqJtq98hPqEX7fNZaFWoOe3PBFnMgH1pCVxNOJO3UGNGWoN1bLGC', 'GESTOR', 'GRU'),
('Atendente GRU', 'atendente.gru', '$2a$10$7EqJtq98hPqEX7fNZaFWoOe3PBFnMgH1pCVxNOJO3UGNGWoN1bLGC', 'ATENDENTE', 'GRU'),
('Operador GIG', 'operador.gig', '$2a$10$7EqJtq98hPqEX7fNZaFWoOe3PBFnMgH1pCVxNOJO3UGNGWoN1bLGC', 'OPERADOR', 'GIG'),
('Supervisor CGH', 'supervisor.cgh', '$2a$10$7EqJtq98hPqEX7fNZaFWoOe3PBFnMgH1pCVxNOJO3UGNGWoN1bLGC', 'SUPERVISOR', 'CGH');