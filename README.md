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
- Spring Boot
- Maven
- PostgreSQL
- Cassandra
- Redis
- Docker

## Como Executar o Projeto
Pré-requisitos:
- Docker Desktop (Windows) | docker-compose & docker (archlinux)
- Java 17
- Maven | maven (archlinux)

## Instalação (archlinux)
```sudo pacman -S maven docker-compose docker```

## Rodando o Projeto
### Subindo o ambiente
Ao acessar a pasta do projeto (cd sistema-aeroportuario), rodar:
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
Se o comando entrar em **cqlsh**, significa que está pronto para rodar. Basta digitar EXIT para voltar à pasta do projeto.

Finalmente, rodar o backend por:
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

## Frontend (Monitoramento)
O projeto possui um frontend simples para simulação de telões operacionais. Ao consumir os endpoints REST do backend, temos a exibição de:
- Status de voos
- Portões
- Bagagens e Tickets

A população de dados foi efetuada a partir de uma mescla de dados reais, como os aeroportos, mas majoritariamente com dados fictícios para pessoas, voos e objetos.

## Estrutura do Projeto 
O backend é uma aplicação Java com Spring Boot organizada em um único projeto Maven. A arquitetura é dividida por responsabilidade de banco de dados (PostgreSQL, Cassandra e Redis), com separação em camadas.
```bash
airport-management/
├── docker-compose.yml              # Sobe PostgreSQL, Cassandra e Redis localmente
├── pom.xml                         # Dependências e configuração do Maven
└── src/
    └── main/
        ├── java/com/airport/
        │   ├── AirportApplication.java         # Entrada da aplicação (@SpringBootApplication)
        │   │
        │   ├── config/                         # Configurações da integração com os bancos
        │   │   ├── PostgresConfig.java         
        │   │   ├── CassandraConfig.java        
        │   │   └── RedisConfig.java            
        │   │
        │   ├── postgres/                       # Banco relacional — dados transacionais críticos
        │   │   ├── entity/                     # Entidades JPA
        │   │   ├── repository/                 # Interfaces JpaRepository
        │   │   ├── service/                    # Regras de negócio
        │   │   └── controller/                 # Endpoints REST
        │   │
        │   ├── cassandra/                      # Banco de logs/eventos — dados massivos e contínuos
        │   │   ├── entity/                     # Entidades @Table
        │   │   ├── repository/                 # Interfaces
        │   │   ├── service/                    # Lógica de registro
        │   │   └── controller/                 # Endpoints REST 
        │   │
        │   └── redis/                          # Banco de status em tempo real — dados voláteis e cache
        │       ├── service/                    # Operações com RedisTemplate
        │       └── controller/                 # Endpoints REST
        │
        └── resources/
            ├── application.yml                 # Configuração dos bancos (URLs, portas, credenciais)
            │
            ├── static/                         # Frontend simples (servido pelo Spring)
            │   └── index.html                  # Telão/monitoramento
            └── db/
                ├── migration/                  # Scripts do Flyway (PostgreSQL)
                │   ├── V1__create_tables.sql   # DDL — criação das tabelas do PostgreSQL (Flyway)
                │   └── V2__seed_data.sql       # DML — população inicial dos dados (Flyway)
                │
                └── cassandra/                  # Scripts CQL (Cassandra)
                    └── schema.cql              # Criação de keyspace/tabelas de log
    frontend
        airport-frontend
            enlist.config.js
            node_modules
            package.json
            index.html
            package-lock.json
            vite.config.js
            src
                App.css
                App.jsx
                api.js
                index.css
                main.jsx
                assets
                    hero.png
                    react.svg
                    vite.svg
                components
                    BagagemCard.jsx
                    StatusBadge.jsx
                    TimeDisplay.jsx
                    VooRow.jsx
                hooks
                    usePolling.js
```

## Arquitetura de Dados

O sistema utiliza três bancos, cada um com papéis bem definidos:

### PostgreSQL - Dados Críticos
Armazena dados estruturais e persistentes, como:
- Passageiros
- Aeroportos
- Voos
- Passagens
- Tickets de voo
- Bagagens

#### Modelagem de Dados (MER / DER)
Gerado através do LucidChart.
<img width="1645" height="840" alt="image" src="https://github.com/user-attachments/assets/9e7c9f1d-c449-4139-9c6b-e5c59efbebb9" />

---

### Cassandra - Logs e Auditoria
Registra todos os eventos do sistema, como:
- Atualizações de status
- Mudanças de portão
- Atualizações operacionais

---

### Redis - Status em Tempo Real
Armazena dados voláteis, como:
- Status atual do voo
- Horários reais de partida e chegada
- Alterações de portão
- Atrasos
- Situação operacional corrente

Esses dados podem ser sincronizados com o banco relacional quando necessário.

---

## Entidades principais

### Passageiro
- Nome completo
- CPF (PK)
- Endereço
- E-mail
- Telefone
- Data de nascimento

### Voo
- ID (PK)
- Origem e destino (através do IATA)
- Companhia aérea
- Horários
- Portão
- Aeronave
- Terminal
- Status base do voo
---

### Passagem
Representa a **compra do direito de viajar**:
- ID (PK)
- Passageiro
- Voo
- Preço
- Número do assento
- Classe do assento
- Data de emissão
- Status de pagamento (FK)
- Status de embarque

A passagem existe mesmo que o passageiro não realize o embarque.

---

### Ticket de Voo (Check‑in)
Representa a **confirmação operacional para embarque**:
- ID próprio (PK)
- Possui bagagem (boolean)
- Status de embarque

O ticket **só pode ser criado se a passagem estiver paga**.

---

### Bagagem
Entidade dependente do ticket:
- ID próprio (PK)
- Associação com ticket
- Peso
- Status da bagagem

Um ticket pode possuir zero ou mais bagagens.

---

### Aeroporto
Dados estruturais e críticos:
- IATA (PK)
- Nome
- Cidade
- UF
- País
- Fuso horário

---

## Conceitos importantes

- **Passagem ≠ Ticket**
  - Passagem → compra / contrato
  - Ticket → check-in / operação
- **Status de pagamento ≠ Status de embarque**
  - Pagamento pertence à passagem
  - Embarque pertence ao ticket

---

## Usuários do Sistema

O sistema foi pensado para diferentes perfis internos, como:
- Atendentes de check-in
- Operadores de voo
- Supervisores
- Gestores aeroportuários
- Sistemas automatizados

---
## Autoria
Desenvolvido por **Ana Lima** e **Ravi Macedo**.

## Professor Responsável: Leonardo Anjoletto Ferreira
