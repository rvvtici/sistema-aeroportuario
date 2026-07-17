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

## Pré-requisitos:
- Docker Desktop (Windows) | docker-compose & docker (ArchLinux)
- Java 17
- Maven

### Instalação no ArchLinux
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
Ao apertar ENTER,  digitar EXIT para deixar **cqlsh** e voltar à pasta do projeto.

Finalmente, podemos rodar o backend por:
```bash
cd backend
mvn spring-boot:run
```
Para acessar o frontend, via outro terminal, acesse a pasta do projeto em:
```
cd frontend/airport-frontend/
npm install
npm run dev
```
A aplicação estará disponível em:
```bash
http://localhost:5173/
```
## Interface do Usuário
O projeto conta com uma interface web voltada para operações aeroportuárias internas, oferecendo um sistema de login para autenticação e controle de acesso conforme os diferentes tipos de usuário.
<br>
<img width="504" height="439" alt="image" src="https://github.com/user-attachments/assets/341716e8-847f-439e-ae5a-1d716b6e7e0d" />
<br>
Os logins são divididos entre admin, operador e atendente, com sufixos associados ao aeroporto nacional que estará realizando a conexão ao ambiente. As senhas para todos foram padronizadas.
Exemplo usado: 
```
login: atendente.gru
senha: 123456
```
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
sistema-aeroportuario/
├── README.md
├── cassandra-init.cql              # Script CQL rodado pelo cassandra-init no docker-compose
├── docker-compose.yml              # Sobe PostgreSQL, Cassandra e Redis localmente
│
├── backend/                        # Aplicação Spring Boot
│   ├── pom.xml                     # Dependências e configuração do Maven
│   └── src/main/
│       ├── java/com/airport/
│       │   ├── AirportApplication.java         # Entrada da aplicação (@SpringBootApplication)
│       │   ├── config/                         # Configuração dos três bancos
│       │   ├── cassandra/                      # Módulo de logs — dados massivos e contínuos
│       │   │   ├── controller/
│       │   │   ├── entity/
│       │   │   ├── repository/
│       │   │   └── service/
│       │   ├── postgres/                       # Módulo relacional — dados transacionais críticos
│       │   │   ├── controller/
│       │   │   ├── entity/
│       │   │   ├── repository/
│       │   │   └── service/
│       │   └── redis/                          # Módulo de status em tempo real — dados voláteis
│       │       ├── controller/
│       │       └── service/
│       └── resources/
│           ├── application.yml                 # Configuração dos três bancos (URLs, portas, credenciais)
│           ├── db/
│           │   ├── cassandra/
│           │   │   └── schema.cql              # Criação de keyspace/tabelas (referência)
│           │   └── migration/                  # Scripts Flyway — rodam automaticamente no boot
│           │       ├── V1__create_tables.sql   # DDL — criação das tabelas
│           │       ├── V2__seed_data.sql           # DML — população inicial
│           │       ├── V3__fix_char_columns.sql    # Altera colunas para VARCHAR
│           │       ├── V4__fix_serial_columns.sql    # Altera colunas para BIGINT
│           │       ├── V5__fix_all_int_to_bigint.sql    # Altera colunas de bagagem, ticket_de_voo e passagem para BIGINT
│           │       ├── V6__fix_all_char_columns.sql    # Altera colunas de passageiro, voo e passagem para VARCHAR
│           │       ├── V7__create_usuarios    # Criação de tabela usuario para Autenticação
│           │       ├── V8__seed_usuarios.sql  # População de tabela usuario com todos os logins admin, operador e atendente para cada aeroporto nacional
│           └── static/                         # Build do frontend servido pelo Spring (porta 8080)
│
└── frontend/                       # Código fonte do frontend React
    └── airport-frontend/
        ├── vite.config.js          # Proxy /api → localhost:8080
        ├── package.json
        └── src/
            ├── App.jsx             # Painel principal (voos + bagagens)
            ├── api.js              # Camada de chamadas HTTP para a API
            ├── index.css           # Variáveis CSS e reset global
            ├── components/         # StatusBadge, TimeDisplay, VooRow, BagagemCard
            └── hooks/
                └── usePolling.js   # Atualização automática a cada N segundos
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

SELECT * FROM voo;
SELECT * FROM bagagem;
SELECT * FROM passageiro;
```
- Autenticação da API:
```
$response = Invoke-RestMethod `
-Uri "http://localhost:8080/auth/login" `
-Method POST `
-ContentType "application/json" `
-Body '{
  "login":"admin.gru",
  "senha":"123456"
}'

$token = $response.token

$headers = @{
    Authorization = "Bearer $token"
}
```
- TESTE - Criar voo via API:
```
Invoke-RestMethod `
-Uri "http://localhost:8080/api/voos" `
-Method POST `
-Headers $headers `
-ContentType "application/json" `
-Body '{
  "companhiaAerea":"Gol",
  "origem":{"iata":"GRU"},
  "destino":{"iata":"SDU"},
  "aeronave":"Airbus 2700",
  "terminal":"T1",
  "portao":"A08",
  "horarioPartida":"2026-08-10T06:00:00",
  "horarioChegada":"2026-08-10T07:10:00",
  "previsaoPartida":"2026-08-10T06:05:00",
  "previsaoChegada":"2026-08-10T07:15:00",
  "status":"PROGRAMADO"
}'
```
- TESTE - Atualizar voo via API
```
Invoke-RestMethod `
-Uri "http://localhost:8080/api/voos/1" `
-Method PUT `
-Headers $headers `
-ContentType "application/json" `
-Body '{
  "companhiaAerea":"Gol",
  "origem":{"iata":"GRU"},
  "destino":{"iata":"SDU"},
  "aeronave":"Airbus 2700",
  "terminal":"T1",
  "portao":"B22",
  "horarioPartida":"2026-08-10T06:00:00",
  "horarioChegada":"2026-08-10T07:10:00",
  "previsaoPartida":"2026-08-10T06:05:00",
  "previsaoChegada":"2026-08-10T07:15:00",
  "status":"EMBARCANDO"
}'
```
- Redis:
```
docker exec -it airport_redis redis-cli
KEYS *
HGETALL voo:1
```
- Cassandra:
```
docker exec -it airport_cassandra cqlsh 
USE airport_logs;
DESC tables; -- Lista as tabelas (log_criacao, log_cancelamento, log_confirmacao, log_mudanca)
SELECT * FROM log_criacao; -- É possível acessar qualquer tabela para verificar
```
---
## Autoria
Desenvolvido por **Ana Lima** e **Ravi Macedo**.

## Professor Responsável: Leonardo Anjoletto Ferreira