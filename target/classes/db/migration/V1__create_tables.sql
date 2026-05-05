-- ============================================================
--  Sistema de Gestão Aeroportuária — PostgreSQL
--  DDL — CRIAÇÃO DAS TABELAS
-- ============================================================

-- Aeroporto
CREATE TABLE aeroporto (
    iata        CHAR(3) PRIMARY KEY,
    nome        VARCHAR(100)  NOT NULL,
    cidade      VARCHAR(100)  NOT NULL,
    uf          CHAR(2),
    pais        VARCHAR(60)   NOT NULL,
    fuso_horario VARCHAR(50)
);

-- Voo
-- status: 'PROGRAMADO' | 'EMBARCANDO' | 'ATRASADO' | 'CANCELADO' | 'CONCLUIDO'
-- portão e status_embarque são integração com Redis (negrito/itálico no DER)
CREATE TABLE voo (
    id                  SERIAL PRIMARY KEY,
    companhia_aerea     VARCHAR(100) NOT NULL,
    origem              CHAR(3)  NOT NULL REFERENCES aeroporto(iata), -- a ideia é tratar como char e pelos iatas dos aeroportos, tanto destino quanto origem
    destino             CHAR(3)  NOT NULL REFERENCES aeroporto(iata),
    aeronave            VARCHAR(60),
    terminal            VARCHAR(10),
    portao              VARCHAR(10),          -- espelhado no Redis
    horario_partida     TIMESTAMP    NOT NULL,
    horario_chegada     TIMESTAMP    NOT NULL,
    previsao_partida    TIMESTAMP,
    previsao_chegada    TIMESTAMP,
    status              VARCHAR(20)  NOT NULL DEFAULT 'PROGRAMADO'  -- espelhado no Redis
);

-- Passageiro
CREATE TABLE passageiro (
    cpf             CHAR(11)     PRIMARY KEY,
    nome_completo   VARCHAR(150) NOT NULL,
    data_nascimento DATE         NOT NULL,
    telefone        VARCHAR(20),
    email           VARCHAR(150),
    endereco        VARCHAR(255)
);

-- Passagem (compra: passageiro → passagem)
CREATE TABLE passagem (
    id                  SERIAL PRIMARY KEY,
    cpf_passageiro      CHAR(11)        NOT NULL REFERENCES passageiro(cpf),
    voo_id              INT             NOT NULL REFERENCES voo(id),
    numero_assento      VARCHAR(6)      NOT NULL,
    classe_assento      VARCHAR(20)     NOT NULL,  -- 'ECONOMICA' | 'EXECUTIVA' | 'PRIMEIRA'
    preco               DECIMAL(10,2)   NOT NULL,
    data_emissao        TIMESTAMP       NOT NULL DEFAULT NOW(),
    status_pagamento    VARCHAR(20)     NOT NULL DEFAULT 'PENDENTE',  -- 'PENDENTE' | 'PAGO' | 'CANCELADO'
    status              VARCHAR(20)     NOT NULL DEFAULT 'ATIVA'       -- 'ATIVA' | 'CANCELADA' | 'USADA'
);

-- Ticket de voo (gerado pela passagem)
-- status_embarque é integração com Redis (negrito/itálico no DER)
CREATE TABLE ticket_de_voo (
    id                  SERIAL PRIMARY KEY,
    passagem_id         INT         NOT NULL REFERENCES passagem(id),
    status_pagamento    VARCHAR(20) NOT NULL DEFAULT 'PAGO',
    possui_bagagem      BOOLEAN     NOT NULL DEFAULT FALSE,
    status_embarque     VARCHAR(20) NOT NULL DEFAULT 'AGUARDANDO'  -- espelhado no Redis
);

-- Bagagem (passageiro possui bagagem via ticket)
-- status é integração com Redis (negrito/itálico no DER)
CREATE TABLE bagagem (
    id              SERIAL PRIMARY KEY,
    ticket_id       INT             NOT NULL REFERENCES ticket_de_voo(id),
    peso            DECIMAL(5,2)    NOT NULL,
    status          VARCHAR(30)     NOT NULL DEFAULT 'CHECK_IN'  -- espelhado no Redis
);

-- ============================================================
--  DDL — ÍNDICES ÚTEIS
-- ============================================================

CREATE INDEX idx_voo_origem     ON voo(origem_id);
CREATE INDEX idx_voo_destino    ON voo(destino_id);
CREATE INDEX idx_voo_partida    ON voo(horario_partida);
CREATE INDEX idx_passagem_voo   ON passagem(voo_id);
CREATE INDEX idx_passagem_cpf   ON passagem(cpf_passageiro);
CREATE INDEX idx_ticket_passagem ON ticket_de_voo(passagem_id);
CREATE INDEX idx_bagagem_ticket  ON bagagem(ticket_id);

