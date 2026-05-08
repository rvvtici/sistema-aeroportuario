-- bagagem
ALTER TABLE bagagem ALTER COLUMN ticket_id TYPE BIGINT;

-- ticket_de_voo
ALTER TABLE ticket_de_voo ALTER COLUMN passagem_id TYPE BIGINT;

-- passagem
ALTER TABLE passagem ALTER COLUMN voo_id TYPE BIGINT;
