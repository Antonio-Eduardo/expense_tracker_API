# expense_tracker_API

![Status do Projeto](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
[![Licença MIT](https://img.shields.io/badge/licenca-MIT-green)](LICENSE)

> API REST de controle de gastos pessoais com contas bancárias, categorias de gastos e histórico mensal. Persistência em PostgreSQL via Docker e testes de integração com Testcontainers.

---

## Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Modelo de Dados](#modelo-de-dados)
- [Endpoints Disponíveis](#endpoints-disponíveis)
- [Como Executar](#como-executar)
- [Testes](#testes)
- [Melhorias Futuras](#melhorias-futuras)

---

## Sobre o Projeto

O **expense_tracker_API** é uma API REST desenvolvida em Java com Spring Boot para controle de gastos pessoais. O sistema permite que usuários gerenciem suas contas bancárias, registrem despesas mensais categorizadas e acompanhem limites de gasto por categoria.

As principais funcionalidades já implementadas incluem:

- Cadastro e gerenciamento de usuários com localização
- Contas bancárias associadas a cada usuário
- Registro de despesas agrupadas por mês (`MonthlyExpense`)
- Categorização de gastos com limite de notificação por categoria
- Processamento de despesas: atualiza automaticamente o total mensal e o saldo da conta bancária
- Dados iniciais carregados automaticamente via `CommandLineRunner`
- Testes de integração com banco PostgreSQL real (Testcontainers)

---

## Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 25 | Linguagem principal |
| Spring Boot | 4.0.6 | Framework principal |
| Spring Data JPA | — | Repositórios e persistência |
| Spring Security | — | Configuração de segurança (CSRF desabilitado, acesso livre) |
| PostgreSQL | 16 | Banco de dados |
| Docker | — | Container do banco de dados |
| Testcontainers | — | PostgreSQL real nos testes de integração |
| Lombok | — | Redução de boilerplate |
| Maven | — | Gerenciamento de dependências |

---

## Estrutura do Projeto

```
src/
├── main/
│   └── java/com/eduardo/expense_tracker/
│       ├── configs/
│       │   ├── InitialData.java       # Dados de seed via CommandLineRunner
│       │   └── SecurityConfig.java    # Configuração do Spring Security
│       ├── entities/
│       │   ├── User.java
│       │   ├── Location.java
│       │   ├── BankAccount.java
│       │   ├── Category.java
│       │   ├── MonthlyExpense.java
│       │   └── Expense.java
│       ├── repositories/
│       │   ├── UserRepository.java
│       │   ├── LocationRepository.java
│       │   ├── BankAccountRepository.java
│       │   ├── CategoryRepository.java
│       │   ├── MonthlyExpenseRepository.java
│       │   └── ExpenseRepository.java
│       ├── resource/
│       │   └── UserResource.java      # Endpoints REST de usuário
│       ├── services/
│       │   ├── UserService.java
│       │   ├── LocationService.java
│       │   ├── BankAccountService.java
│       │   ├── CategoryServices.java
│       │   ├── MonthlyExpenseService.java
│       │   ├── ExpenseService.java
│       │   └── exceptions/
│       │       └── ResourceNotFind.java
│       └── ExpenseTrackerApplication.java
└── test/
    └── java/com/eduardo/expense_tracker/
        ├── ExpenseTrackerApplicationTests.java
        ├── TestcontainersConfiguration.java
        └── TestExpenseTrackerApplication.java
```

---

## Modelo de Dados

```
Location (1) ──── (N) User (1) ──── (N) BankAccount (1) ──── (N) MonthlyExpense (1) ──── (N) Expense
                                                                                                  │
                                                                                           (N) Category
```

**Entidades:**

- **User** — nome, email, senha, CPF, telefone, data de nascimento e localização
- **Location** — cidade, estado, endereço, CEP
- **BankAccount** — tipo de conta, saldo, data de fechamento do cartão
- **Category** — nome e limite de notificação de gastos
- **MonthlyExpense** — mês de referência, total gasto, limite mensal e conta bancária vinculada
- **Expense** — valor, descrição, momento do gasto, categoria e mês de referência

**Fluxo de processamento:**

1. `ExpenseService.processExpense()` — adiciona o valor da despesa ao total do mês e desconta do limite mensal
2. `MonthlyExpenseService.processMonthlyExpense()` — desconta o total mensal do saldo da conta bancária

---

## Endpoints Disponíveis

### Usuários — `/users`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/users` | Lista todos os usuários |
| GET | `/users/{id}` | Busca usuário por ID |
| POST | `/users/insert` | Cadastra novo usuário |
| PUT | `/users/update/{id}` | Atualiza nome, email e telefone |
| DELETE | `/users/delete/{id}` | Remove usuário |

> Os demais recursos (BankAccount, Category, MonthlyExpense, Expense, Location) possuem services e repositories implementados, mas ainda **não têm controllers REST expostos**.

---

## Como Executar

### Pré-requisitos

- Java 25+
- Docker Desktop
- Maven

### Passos

**1. Clone o repositório:**

```bash
git clone https://github.com/Antonio-Eduardo/expense_tracker_API.git
cd expense_tracker_API
```

**2. Suba o banco de dados com Docker:**

```bash
docker run --name expense-postgres \
  -e POSTGRES_PASSWORD=minhasenha \
  -e POSTGRES_DB=expense_tracker \
  -p 5432:5432 \
  -d postgres:16-alpine
```

**3. Configure as credenciais no `application.properties`** (se necessário):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/expense_tracker
spring.datasource.username=postgres
spring.datasource.password=minhasenha
```

**4. Execute a aplicação:**

```bash
mvn spring-boot:run
```

Ao iniciar, a aplicação carrega dados de exemplo automaticamente (usuário Eduardo, conta bancária, categorias Alimentação e Transporte, gastos de maio).

---

## Testes

O projeto utiliza Testcontainers para testes de integração — o Spring sobe completo contra um PostgreSQL real em container, sem banco em memória.

O teste principal (`deveriaDescontarDoMensalEBanco`) valida o fluxo completo:

- Criação de usuário, conta bancária, categoria e despesas
- Processamento das despesas no mês (atualização de total e limite)
- Desconto do total mensal no saldo da conta bancária

```bash
mvn test
```

---

## Melhorias Futuras

- [ ] Controllers REST para BankAccount, Category, MonthlyExpense, Expense e Location
- [ ] DTOs para evitar exposição direta das entidades
- [ ] Tratamento global de exceções (`@ControllerAdvice`)
- [ ] Validação de entrada com Spring Validation (`@Valid`)
- [ ] Autenticação com Spring Security + JWT
- [ ] Flyway para versionamento do schema do banco
- [ ] Documentação com Swagger / OpenAPI
- [ ] Notificação ao atingir limite de categoria
