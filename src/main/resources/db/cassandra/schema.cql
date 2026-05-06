-- Criar keyspace
CREATE KEYSPACE IF NOT EXISTS airport_logs
WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};

-- Usar o keyspace
USE airport_logs;

-- ============================
-- LOG CANCELAMENTO
-- ============================
CREATE TABLE IF NOT EXISTS log_cancelamento (
    id TEXT PRIMARY KEY,
    data DATE,
    horario TIME,
    entidade_id TEXT,
    entidade_tipo TEXT
);

-- ============================
-- LOG CRIAÇÃO
-- ============================
CREATE TABLE IF NOT EXISTS log_criacao (
    id TEXT PRIMARY KEY,
    data DATE,
    horario TIME,
    entidade_id TEXT,
    entidade_tipo TEXT
);

-- ============================
-- LOG MUDANÇA
-- ============================
CREATE TABLE IF NOT EXISTS log_mudanca (
    id TEXT PRIMARY KEY,
    data DATE,
    horario TIME,
    entidade_id TEXT,
    de TEXT,
    para TEXT,
    motivo TEXT
);

-- ============================
-- LOG CONFIRMAÇÃO
-- ============================
CREATE TABLE IF NOT EXISTS log_confirmacao (
    id TEXT PRIMARY KEY,
    data DATE,
    horario TIME,
    entidade_id TEXT,
    entidade_tipo TEXT
);