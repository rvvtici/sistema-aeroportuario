-- ============================================================
--  DML — POPULAÇÃO DE DADOS
-- ============================================================

-- Aeroportos
INSERT INTO aeroporto (nome, iata, cidade, uf, pais, fuso_horario) VALUES
('Aeroporto Internacional de Guarulhos',    'GRU', 'Guarulhos',        'SP', 'Brasil',   'America/Sao_Paulo'),
('Aeroporto Santos Dumont',                 'SDU', 'Rio de Janeiro',   'RJ', 'Brasil',   'America/Sao_Paulo'),
('Aeroporto Internacional de Brasília',     'BSB', 'Brasília',         'DF', 'Brasil',   'America/Sao_Paulo'),
('Aeroporto Internacional de Confins',      'CNF', 'Belo Horizonte',   'MG', 'Brasil',   'America/Sao_Paulo'),
('Aeroporto Internacional de Heathrow',     'LHR', 'Londres',          NULL, 'Reino Unido', 'Europe/London'),
('Aeroporto Internacional JFK',             'JFK', 'Nova Iorque',      NULL, 'EUA',      'America/New_York'),
('Aeroporto Internacional de Miami',        'MIA', 'Miami',            NULL, 'EUA',      'America/New_York'),
('Aeroporto Charles de Gaulle',             'CDG', 'Paris',            NULL, 'França',   'Europe/Paris');

-- Voos
INSERT INTO voo (companhia_aerea, origem_id, destino_id, aeronave, terminal, portao, horario_partida, horario_chegada, previsao_partida, previsao_chegada, status) VALUES
('LATAM Airlines',  1, 2, 'Airbus A320',   'T1', 'A12', '2025-08-10 06:00:00', '2025-08-10 07:10:00', '2025-08-10 06:05:00', '2025-08-10 07:15:00', 'PROGRAMADO'),
('Gol',             2, 3, 'Boeing 737-800','T2', 'B03', '2025-08-10 08:30:00', '2025-08-10 10:00:00', '2025-08-10 08:45:00', '2025-08-10 10:15:00', 'ATRASADO'),
('Azul',            3, 4, 'Embraer E195',  'T1', 'C07', '2025-08-10 11:00:00', '2025-08-10 12:15:00', '2025-08-10 11:00:00', '2025-08-10 12:15:00', 'EMBARCANDO'),
('LATAM Airlines',  1, 6, 'Boeing 777',    'T3', 'D22', '2025-08-10 14:00:00', '2025-08-11 03:00:00', '2025-08-10 14:30:00', '2025-08-11 03:30:00', 'PROGRAMADO'),
('Gol',             4, 1, 'Boeing 737-800','T2', 'A05', '2025-08-10 16:00:00', '2025-08-10 17:30:00', '2025-08-10 16:00:00', '2025-08-10 17:30:00', 'PROGRAMADO'),
('Azul',            1, 5, 'Airbus A330',   'T3', 'E11', '2025-08-10 22:00:00', '2025-08-11 14:00:00', '2025-08-10 22:00:00', '2025-08-11 14:00:00', 'PROGRAMADO'),
('LATAM Airlines',  2, 8, 'Boeing 787',    'T1', 'F01', '2025-08-11 01:00:00', '2025-08-11 18:30:00', '2025-08-11 01:00:00', '2025-08-11 18:30:00', 'PROGRAMADO'),
('Gol',             3, 1, 'Airbus A320',   'T1', 'A09', '2025-08-11 07:00:00', '2025-08-11 09:00:00', '2025-08-11 07:10:00', '2025-08-11 09:10:00', 'PROGRAMADO');

-- Passageiros
INSERT INTO passageiro (cpf, nome_completo, data_nascimento, telefone, email, endereco) VALUES
('12345678901', 'Ana Paula Souza',       '1990-03-15', '11988001111', 'ana.souza@email.com',    'Rua das Flores, 100, São Paulo - SP'),
('98765432100', 'Carlos Eduardo Lima',   '1985-07-22', '21977002222', 'carlos.lima@email.com',  'Av. Atlântica, 500, Rio de Janeiro - RJ'),
('11122233344', 'Mariana Costa Pereira', '1995-11-08', '61966003333', 'mari.pereira@email.com', 'SQN 304, Brasília - DF'),
('55566677788', 'Roberto Alves Neto',    '1978-01-30', '31955004444', 'roberto.neto@email.com', 'Rua Minas Gerais, 200, BH - MG'),
('22233344455', 'Fernanda Rocha',        '2000-06-19', '11944005555', 'fer.rocha@email.com',    'Alameda Santos, 44, São Paulo - SP'),
('66677788899', 'Pedro Henrique Matos',  '1992-09-05', '21933006666', 'ph.matos@email.com',     'Rua Ipanema, 88, Rio de Janeiro - RJ'),
('33344455566', 'Juliana Ferreira',      '1988-12-25', '61922007777', 'ju.ferreira@email.com',  'Asa Norte, Brasília - DF'),
('77788899900', 'Lucas Andrade',         '1997-04-14', '31911008888', 'lucas.andrade@email.com','Savassi, Belo Horizonte - MG');

-- Passagens
INSERT INTO passagem (cpf_passageiro, voo_id, numero_assento, classe_assento, preco, data_emissao, status_pagamento, status) VALUES
('12345678901', 1, '12A', 'ECONOMICA',  450.00,  '2025-07-01 10:00:00', 'PAGO',      'ATIVA'),
('98765432100', 2, '05C', 'EXECUTIVA',  980.00,  '2025-07-02 11:30:00', 'PAGO',      'ATIVA'),
('11122233344', 3, '20F', 'ECONOMICA',  320.00,  '2025-07-03 09:00:00', 'PAGO',      'ATIVA'),
('55566677788', 4, '02B', 'PRIMEIRA',   4500.00, '2025-07-05 14:00:00', 'PAGO',      'ATIVA'),
('22233344455', 1, '15D', 'ECONOMICA',  450.00,  '2025-07-06 08:00:00', 'PAGO',      'ATIVA'),
('66677788899', 5, '08A', 'ECONOMICA',  390.00,  '2025-07-07 16:00:00', 'PENDENTE',  'ATIVA'),
('33344455566', 6, '01C', 'EXECUTIVA',  3200.00, '2025-07-08 09:00:00', 'PAGO',      'ATIVA'),
('77788899900', 2, '22E', 'ECONOMICA',  320.00,  '2025-07-09 11:00:00', 'CANCELADO', 'CANCELADA');

-- Tickets de voo
INSERT INTO ticket_de_voo (passagem_id, status_pagamento, possui_bagagem, status_embarque) VALUES
(1, 'PAGO',      TRUE,  'EMBARCADO'),
(2, 'PAGO',      TRUE,  'AGUARDANDO'),
(3, 'PAGO',      FALSE, 'EMBARCADO'),
(4, 'PAGO',      TRUE,  'AGUARDANDO'),
(5, 'PAGO',      FALSE, 'AGUARDANDO'),
(6, 'PENDENTE',  FALSE, 'AGUARDANDO'),
(7, 'PAGO',      TRUE,  'AGUARDANDO'),
(8, 'CANCELADO', FALSE, 'CANCELADO');

-- Bagagens (apenas tickets com possui_bagagem = TRUE)
INSERT INTO bagagem (ticket_id, peso, status) VALUES
(1, 23.5, 'DESPACHADA'),
(2, 18.0, 'CHECK_IN'),
(4, 30.2, 'CHECK_IN'),
(7, 21.0, 'CHECK_IN');