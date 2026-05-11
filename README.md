# 🪂 GLIDER - Sistema de Gestão Aeroportuária

## Visão Geral

***Glider*** é um sistema de gerenciamento aeroportuário voltado para operações internas e monitoramento em tempo real, utilizando uma arquitetura baseada em múltiplos bancos de dados. O sistema permite:
- Gerenciamento de voos e aeroportos  
- Controle de passagens, tickets (check-in) e bagagens  
- Atualização de status operacionais em tempo real  
- Registro completo de logs de alterações e eventos  

---

## Objetivo do Projeto

Construir uma arquitetura escalável que:

- Centralize dados críticos em um banco relacional
- Utilize bancos NoSQL para:
  - status em tempo real
  - logs/auditoria
- Permita sincronização e comunicação entre os bancos
- Simule um cenário real de sistemas distribuídos

---

## Tecnologias Utilizadas
- Java 17
- Spring Boot 3.3.0
- Maven
- PostgreSQL
- Cassandra
- Redis
- Docker

## Como executar
### Pré-requisitos:
- Docker Desktop (Windows) | Docker-compose & Docker (archlinux)
- Java 17
- Maven

### Instalação (archlinux)
```sudo pacman -S maven docker-compose docker```

## Rodando o Projeto
### Subindo o ambiente
Ao acessar o local da pasta do projeto, rodar:
```bash
docker-compose up -d 
```
E verificar se os containers foram gerados:
```bash
docker ps 
```
Após confirmação, aguardar a inicialização do Cassandra (aproximadamente 1-2 minutos) e validar com:
```bash
docker exec -it airport_cassandra cqlsh 
```
Se o comando entrar em **cqlsh**, significa que estará pronto para rodar. Caso seja a primeira vez que o projeto estiver subindo no docker, será preciso criar a estrutura abaixo no Cassandra:
```bash
CREATE KEYSPACE airport_logs
WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};
```
Basta digitar EXIT para deixar **cqlsh** e voltar à pasta do projeto.

Finalmente, podemos rodar o backend por:
```bash
mvn spring-boot:run
```
Para acessar o frontend, via outro terminal, acesse a pasta do projeto em:
```
cd frontend/airport-frontend/
npm run dev
```
A aplicação estará disponível em:
```bash
http://localhost:5173/
```
## Interface do Usuário
O projeto conta com uma interface web voltada para operações aeroportuárias internas, oferecendo um sistema de login para autenticação e controle de acesso conforme os diferentes tipos de usuário. 
<img width="504" height="439" alt="image" src="https://github.com/user-attachments/assets/341716e8-847f-439e-ae5a-1d716b6e7e0d" />
Após a autentificação, o usuário é direcionado para um painel operacional inspirado em telões aeroportuários, priorizando leitura rápida para monitoramento e atualização contínua de informações.
O sistema é dividido em duas abas principais:
- Voos: Página principal da aplicação. Exibe os voos associados ao aeroporto, incluindo informações operacionais e atualizações de status em tempo real.
<img width="1365" height="590" alt="image" src="https://github.com/user-attachments/assets/3f71fa63-bd00-443f-ad7b-887b781f9114" />
<br>
- Bagagens: Área complementar destinada ao gerenciamento e monitoramento de bagagens, permitindo visualizar suas principais informações, como passageiro relacionado, vínculos com voos, ticket e status.
<br>
<img width="1365" height="506" alt="image" src="https://github.com/user-attachments/assets/62330f29-998a-4510-a75f-c888126c1326" />
<br>
A base de dados utilizada no projeto combina elementos reais (aeroportos, companhias aéreas, códigos IATA) com dados fictícios criados para passageiros, voos, bagagens e demais entidades do sistema.

## Usuários do Sistema

O sistema possui três perfis de acesso, cada um com escopo de visualização e permissões distintas:

| Perfil | Visualização | Editar status | Excluir bagagens |
|---|---|---|---|
| **ATENDENTE** | Só seu aeroporto | ✗ | ✗ |
| **OPERADOR** | Só seu aeroporto | ✓ | ✗ |
| **ADMIN** | Todos os aeroportos | ✓ | ✓ |

Cada usuário é vinculado a um aeroporto específico no cadastro. Atendentes e Operadores visualizam apenas voos e bagagens cujo aeroporto de origem ou destino coincide com o seu. Administradores têm visão global do sistema.

A autenticação é feita via login e senha, com tokens JWT gerados no backend e validados a cada requisição.

## Estrutura do Projeto 
O backend é uma aplicação Java com Spring Boot organizada em um único projeto Maven. A arquitetura é dividida por responsabilidade de banco de dados (PostgreSQL, Cassandra e Redis), com separação em camadas.
```bash
airport-management/
├── docker-compose.yml              # Sobe PostgreSQL, Cassandra e Redis localmente
├── pom.xml                         # Dependências e configuração do Maven
├── cassandra-init.cql              # Script inicial de criação/configuração do Cassandra
├── README.md                       # Documentação principal do projeto
│
├── src/
│   └── main/
│       ├── java/com/airport/
│       │   ├── AirportApplication.java         # Entrada da aplicação (@SpringBootApplication)
│       │   │
│       │   ├── config/                         # Configurações da integração com os bancos
│       │   │   ├── PostgresConfig.java         
│       │   │   ├── CassandraConfig.java        
│       │   │   └── RedisConfig.java            
│       │   │
│       │   ├── postgres/                       # Banco relacional — dados transacionais críticos
│       │   │   ├── entity/                     # Entidades JPA
│       │   │   ├── repository/                 # Interfaces JpaRepository
│       │   │   ├── service/                    # Regras de negócio
│       │   │   └── controller/                 # Endpoints REST
│       │   │
│       │   ├── cassandra/                      # Banco de logs/eventos — dados massivos e contínuos
│       │   │   ├── entity/                     # Entidades @Table
│       │   │   ├── repository/                 # Interfaces
│       │   │   ├── service/                    # Lógica de registro
│       │   │   └── controller/                 # Endpoints REST 
│       │   │
│       │   └── redis/                          # Banco de status em tempo real — dados voláteis e cache
│       │       ├── service/                    # Operações com RedisTemplate
│       │       └── controller/                 # Endpoints REST
│       │
│       └── resources/
│           ├── application.yml                 # Configuração dos bancos (URLs, portas, credenciais)
│           │
│           ├── static/                         # Frontend simples (servido pelo Spring)
│           │   └── index.html                  # Telão/monitoramento
│           │
│           └── db/
│               ├── migration/                  # Scripts do Flyway (PostgreSQL)
│               │   ├── V1__create_tables.sql   # DDL — criação das tabelas do PostgreSQL (Flyway)
│               │   └── V2__seed_data.sql       # DML — população inicial dos dados (Flyway)
│               │
│               └── cassandra/                  # Scripts CQL (Cassandra)
│                   └── schema.cql              # Criação de keyspace/tabelas de log
│
└── frontend/
    └── airport-frontend/
        ├── package.json                        # Dependências e scripts do frontend React/Vite
        ├── package-lock.json                   # Lockfile das dependências npm
        ├── vite.config.js                      # Configuração do Vite
        ├── eslint.config.js                    # Configuração do ESLint
        ├── index.html                          # Página base da aplicação frontend
        │
        ├── src/
        │   ├── main.jsx                        # Ponto de entrada da aplicação React
        │   ├── App.jsx                         # Componente principal da interface
        │   ├── App.css                         # Estilos principais da aplicação
        │   ├── index.css                       # Estilos globais
        │   ├── api.js                          # Configuração e chamadas da API backend
        │   │
        │   ├── assets/                         # Arquivos estáticos e imagens
        │   │
        │   ├── components/                     # Componentes reutilizáveis da interface
        │   │   ├── BagagemCard.jsx             # Card de exibição das bagagens
        │   │   ├── StatusBadge.jsx             # Indicador visual de status
        │   │   ├── TimeDisplay.jsx             # Relógio/horário em tempo real
        │   │   └── VooRow.jsx                  # Linha individual da tabela de voos
        │   │
        │   └── hooks/
        │       └── usePolling.js               # Hook de atualização automática via polling
        │
        └── node_modules/                       # Dependências instaladas pelo npm
```

## Arquitetura de Dados

O sistema utiliza três bancos, cada um com papéis bem definidos:

### PostgreSQL - Dados Críticos
Armazena todos os dados estruturais e persistentes do sistema, como fonte de verdade para as operações transacionais. Entre as entidades:
- Aeroportos: Cadastro com IATA, cidade, fuso horário
- Voos: Rotas, horários, terminal, portão e status operacional
- Passageiros: Dados cadastrais e de contato
- Passagens: Vínculo entre passageiro e voo, com assento e status de pagamento
- Tickets de voo: Gerados a partir de passagens confirmadas, controlam o embarque
- Bagagens: Associadas a tickets, com peso e status de rastreamento
- Usuários: Autenticação e controle de acesso por perfil e aeroporto

#### Modelagem de Dados (MER / DER)
Modelado através do LucidChart.
<img width="1645" height="840" alt="image" src="https://github.com/user-attachments/assets/9e7c9f1d-c449-4139-9c6b-e5c59efbebb9" />

---

### Cassandra - Logs e Auditoria
Registra todos os eventos do sistema para fins de rastreabilidade e auditoria. Cada tabela cobre um tipo específico de evento:

- log_mudanca — alterações de status em voos e bagagens, com valor anterior e novo
- log_criacao — registro de novos recursos criados no sistema (voos, bagagens)
- log_cancelamento — exclusões de bagagens e cancelamentos de voos
- log_confirmacao — eventos de conclusão bem-sucedida: bagagens retiradas e voos concluídos

---

### Redis - Status em Tempo Real
Armazena dados voláteis que precisam de acesso rápido e são atualizados com frequência durante a operação:
- Status atual de voos e bagagens
- Portões de embarque
- Sincronização automática: Ao inicializar, o backend espelha todos os dados do PostgreSQL no Redis; alterações feitas pelo sistema atualizam os dois bancos simultaneamente
<br>
Os dados do Redis são sempre sincronizados com o PostgreSQL.
<br>

## Conceitos importantes

- **Passagem ≠ Ticket**
  - Passagem → compra / contrato
  - Ticket → check-in / operação
  - A passagem existe mesmo que o passageiro não realize o embarque.
  - O ticket **só pode ser criado se a passagem estiver paga**.
  - Um ticket pode possuir zero ou mais bagagens.
- **Status de pagamento ≠ Status de embarque**
  - Pagamento pertence à passagem
  - Embarque pertence ao ticket

## CRUD no Backend - Consultas para teste
- Postgres:
```
docker exec -it airport_postgres psql -U postgres -d airport
\dt -- para listar as tabelas

-- TESTE - PASSAGEIRO
INSERT INTO passageiro (cpf, nome_completo, data_nascimento, telefone, email, endereco) VALUES
('40152037022', 'Joe Limer',       '2000-03-07', '1197422221', 'Joe.limer@email.com',    'Rua das Flores Amarelas, 12, São Paulo - SP');

-- TESTE - VOO
INSERT INTO voo (companhia_aerea, origem, destino, aeronave, terminal, portao, horario_partida, horario_chegada, previsao_partida, previsao_chegada, status) VALUES
('Gol', 'GRU', 'SDU', 'Airbus 2700', 'T1', 'A08', '2026-08-10 06:00:00', '2026-08-10 07:10:00', '2026-08-10 06:05:00', '2026-08-10 07:15:00', 'PROGRAMADO');

-- TESTE - BAGAGEM
INSERT INTO bagagem (ticket_id, peso, status) VALUES
(1, 17.6, 'RETIRADA');
```
- Redis:
```
docker exec -it airport_redis redis-cli
KEYS *
```
- Cassandra:
```
docker exec -it airport_cassandra cqlsh 
USE airport_logs;
DESC tables; -- Lista as tabelas (log_criacao, log_cancelamento, log_confirmacao, log_mudanca)
SELECT * FROM log_criacao -- É possível acessar qualquer tabela para verificar
```
---
## Autoria
Desenvolvido por **Ana Lima** e **Ravi Macedo**.

## Professor Responsável: Leonardo Anjoletto Ferreira
