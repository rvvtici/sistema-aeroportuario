# Sistema de Gestão Aeroportuária

## Visão Geral

Este projeto tem como objetivo o desenvolvimento de um sistema de gestão interna aeroportuária, com foco em operações, monitoramento e atualização de status de voos, integrando front-end e back-end e utilizando múltiplos bancos de dados.

O sistema não é voltado para passageiros, mas sim para uso interno por operadores aeroportuários, atendentes, supervisores e sistemas automatizados, permitindo:

- Gerenciamento de voos e aeroportos  
- Controle de passagens, tickets (check-in) e bagagens  
- Atualização de status operacionais em tempo real  
- Registro completo de logs de alterações e eventos  

---

## Objetivo do Projeto

Construir uma base conceitual e arquitetural sólida para um sistema que:

- Centralize dados críticos em um banco relacional
- Utilize bancos não relacionais para:
  - status em tempo real
  - logs/auditoria
- Permita sincronização e comunicação entre os bancos
- Seja escalável e adaptável a mudanças operacionais

---

## Ferramentas
- Docker
- DBeaver

## Funcionamento (em Arch Linux)
```
sudo pacman -S docker dbeaver # instalação o docker & dbveader
sudo systemctl start docker.service # inicialização do docker
sudoo systemctl enable docker.service # permissão ao docker
sudo usermod -aG docker $USER # para que não seja mais necessário digitar sudo toda vez
reboot # reinicialização para que o comando acima seja aplicado

# PostgreSQL
docker run --name nome-postgres -e POSTGRES_PASSWORD=user -e POSTGRES_USER=user -p 5454:5432 -d postgres // porta_sistema:porta_postgresql

# Cassandra
docker run --name nome-cassandra -e CASSANDRA_USER=user -e CASSANDRA_PASSWORD=user -p 9042:9042 -d cassandra

# Redis
docker run --name nome-redis -p 6379:6379 -d redis redis-server --requirepass "user"

docker ps -a # verifique se os três databases estão rodando

# no DBeaver:
# conecte os databases
# selecione o postgresql/cassandra/redis
# configure como host: localhost & port: 5454/9042/6379; username & senha = user
# teste a conexão
# vá em postgresql/cassandra/redis e em show all databases para visualizá-los no dbeaver

docker ps -a //all imagens

#comandos úteis
docker stop nome-cassandra nome-redis nome-postgres     # parar
docker start nome-cassandra nome-redis nome-postgres    # iniciar novamente
docker rm nome-cassandra nome-redis nome-postgres       # remover container
docker ps -a                                            # listar todos (rodando ou não)
```


## Arquitetura de Dados

O sistema utiliza três bancos de dados com papéis bem definidos:

### Banco Relacional (dados críticos)
Fonte da verdade do sistema.  
Armazena dados estruturais e permanentes, como:

- Pessoas, passageiros e funcionários
- Aeroportos
- Voos
- Passagens
- Tickets de voo (check-in)
- Bagagens

Esses dados seguem regras de integridade, normalização e relacionamentos bem definidos (PK/FK).

---

### Banco Não Relacional – Status em Tempo Real
Responsável por armazenar informações altamente voláteis, como:

- Status atual do voo
- Horários reais de partida e chegada
- Alterações de portão
- Atrasos
- Situação operacional corrente

Esses dados podem ser sincronizados com o banco relacional quando necessário.

---

### Banco Não Relacional – Logs
Responsável por registrar todos os eventos do sistema, como:

- Alterações manuais realizadas por usuários
- Atualizações automáticas de status
- Mudanças de voo, portão, horário ou paradas
- Origem do evento (humana ou automática)

⚠️ Os logs não alteram dados, apenas registram eventos para auditoria e rastreabilidade.

---

## Modelagem de Dados (MER / DER)

<img width="1645" height="840" alt="image" src="https://github.com/user-attachments/assets/9e7c9f1d-c449-4139-9c6b-e5c59efbebb9" />

### Aeroporto
Dados estruturais e críticos:
- IATA (PK)
- Nome
- Cidade
- UF
- País
- Fuso horário

---

### Voo
Representa um voo operacional:
- Origem e destino (Aeroporto)
- Companhia aérea
- Previsão e horários reais
- Portão, terminal, aeronave
- Status base do voo

---

### Passagem
Representa a **compra do direito de viajar**:
- Passageiro
- Voo
- Preço
- Classe do assento
- Data de emissão
- Status de pagamento

A passagem existe mesmo que o passageiro não realize o embarque.

---

### Ticket de Voo (Check‑in)
Representa a **confirmação operacional para embarque**:
- ID próprio
- Associação com a passagem
- Número do assento confirmado
- Possui bagagem (boolean)
- Status de embarque

O ticket **só pode ser criado se a passagem estiver paga**.

---

### Bagagem
Entidade dependente do ticket:
- ID próprio
- Associação com ticket
- Peso
- Status da bagagem

Um ticket pode possuir zero ou mais bagagens.

---

## Separação conceitual importante

- **Passagem ≠ Ticket**
  - Passagem → compra / contrato
  - Ticket → check-in / operação
- **Status de pagamento ≠ Status de embarque**
  - Pagamento pertence à passagem
  - Embarque pertence ao ticket

Essa separação evita inconsistências e facilita o acompanhamento operacional.

---

## Usuários do Sistema

O sistema foi pensado para diferentes perfis internos, como:
- Atendentes de check-in
- Operadores de voo
- Supervisores
- Gestores aeroportuários
- Sistemas automáticos de status

Cada perfil terá permissões e responsabilidades distintas, a serem definidas nas próximas etapas.

---

## Estado Atual do Projeto

✅ Modelagem conceitual (MER/DER) finalizada  
✅ Definição clara de entidades, atributos e relacionamentos  
✅ Arquitetura de dados com múltiplos bancos definida  
✅ Regras de negócio principais identificadas  

---

## Próximos Passos

- Definição dos **casos de uso**
- Mapeamento das **operações de back-end**
- Definição de **APIs**
