# expense_tracker_API

![Status do Projeto](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
[![Licença MIT](https://img.shields.io/badge/licenca-MIT-green)](LICENSE)

> API REST de controle de gastos pessoais com contas bancárias, categorias de gastos e histórico mensal. Autenticação via JWT, persistência em PostgreSQL via Docker e testes de integração com Testcontainers.

---

## Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Modelo de Dados](#modelo-de-dados)
- [Autenticação](#autenticação)
- [Endpoints Disponíveis](#endpoints-disponíveis)
- [Como Executar](#como-executar)
- [Testes](#testes)
- [Melhorias Futuras](#melhorias-futuras)

---

## Sobre o Projeto

O **expense_tracker_API** é uma API REST desenvolvida em Java com Spring Boot para controle de gastos pessoais. O sistema permite que usuários gerenciem suas contas bancárias, registrem despesas mensais categorizadas e acompanhem limites de gasto por categoria.

As principais funcionalidades já implementadas incluem:

- Tratamento global de exceções com respostas padronizadas (`@RestControllerAdvice`)
- Autenticação stateless com JWT (registro, login e proteção de rotas)
- Controle de roles de acesso (`ADMIN` e `USER`)
- Cadastro e gerenciamento de usuários com localização
- Contas bancárias associadas a cada usuário
- Registro de despesas agrupadas por mês (`MonthlyExpense`)
- Categorização de gastos com limite de notificação por categoria
- Controllers REST completos para todos os recursos (User, BankAccount, Category, MonthlyExpense, Expense, Location)
- DTOs para desacoplamento entre camada de transporte e entidades
- Processamento de despesas: atualiza automaticamente o total mensal e o saldo da conta bancária
- Testes de integração com banco PostgreSQL real (Testcontainers)

---

## Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 25 | Linguagem principal |
| Spring Boot | 4.0.6 | Framework principal |
| Spring Data JPA | — | Repositórios e persistência |
| Spring Security | — | Autenticação e autorização stateless |
| Auth0 Java JWT | — | Geração e validação de tokens JWT |
| PostgreSQL | 16 | Banco de dados |
| Docker | — | Container do banco de dados |
| Testcontainers | — | PostgreSQL real nos testes de integração |
| Flyway | — | Versionamento do schema do banco |
| Lombok | — | Redução de boilerplate |
| Maven | — | Gerenciamento de dependências |

---

## Estrutura do Projeto

```
src/
├── main/
│   └── java/com/eduardo/expense_tracker/
│       ├── controllers/
│       │   └── AuthenticationController.java   # Endpoints /auth/register e /auth/login
│       ├── dtos/
│       │   ├── AuthenticationDTO.java           # Payload de login (email + password)
│       │   ├── LoginResponseDTO.java            # Resposta com token JWT
│       │   ├── RegisterDTO.java                 # Payload de registro (email, password, role)
│       │   ├── BankAccountDTO.java
│       │   ├── ExpenseDTO.java
│       │   ├── MonthlyExpenseDTO.java
│       │   └── UserDTO.java
│       ├── entities/
│       │   ├── user/
│       │   │   ├── User.java                    # Implementa UserDetails
│       │   │   └── UserRole.java                # Enum: ADMIN, USER
│       │   ├── Location.java
│       │   ├── BankAccount.java
│       │   ├── Category.java
│       │   ├── MonthlyExpense.java
│       │   └── Expense.java
│       ├── infra/
│       │   ├── exception/
│       │   │   ├── GlobalExceptionHandler.java  # Tratamento global de exceções (@RestControllerAdvice)
│       │   │   └── StandartError.java           # Payload padronizado de erro
│       │   ├── SecurityConfiguration.java       # Filtros, CSRF, rotas públicas/protegidas
│       │   ├── SecurityFilter.java              # Intercepta requisições e valida JWT
│       │   └── TokenService.java                # Geração e validação de tokens JWT
│       ├── repositories/
│       │   ├── UserRepository.java
│       │   ├── LocationRepository.java
│       │   ├── BankAccountRepository.java
│       │   ├── CategoryRepository.java
│       │   ├── MonthlyExpenseRepository.java
│       │   └── ExpenseRepository.java
│       ├── resource/
│       │   ├── UserResource.java
│       │   ├── BankAccountResource.java
│       │   ├── CategoryResource.java
│       │   ├── MonthlyExpenseResource.java
│       │   ├── ExpenseResource.java
│       │   └── LocationResource.java
│       ├── services/
│       │   ├── UserService.java
│       │   ├── LocationService.java
│       │   ├── BankAccountService.java
│       │   ├── CategoryServices.java
│       │   ├── MonthlyExpenseService.java
│       │   ├── ExpenseService.java
│       │   ├── exceptions/
│       │   │   └── ResourceNotFind.java
│       │   └── servicesAuth/
│       │       └── AuthorizationService.java    # Implementa UserDetailsService
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

- **User** — nome, email, senha (BCrypt), CPF, telefone, data de nascimento, role e localização. Implementa `UserDetails` do Spring Security
- **UserRole** — enum com os valores `ADMIN` (acesso a `ROLE_ADMIN` + `ROLE_USER`) e `USER` (acesso a `ROLE_USER`)
- **Location** — cidade, estado, endereço, CEP
- **BankAccount** — tipo de conta, saldo, data de fechamento do cartão
- **Category** — nome e limite de notificação de gastos
- **MonthlyExpense** — mês de referência, total gasto, limite mensal e conta bancária vinculada
- **Expense** — valor, descrição, momento do gasto, categoria e mês de referência

**Fluxo de processamento:**

1. `ExpenseService.processExpense()` — adiciona o valor da despesa ao total do mês e desconta do limite mensal
2. `MonthlyExpenseService.processMonthlyExpense()` — desconta o total mensal do saldo da conta bancária

---

## Autenticação

A API utiliza autenticação **stateless com JWT**. As únicas rotas públicas são `/auth/register` e `/auth/login`; todas as demais exigem token válido no header.

### Registro

```http
POST /auth/register
Content-Type: application/json

{
  "email": "usuario@email.com",
  "password": "suasenha",
  "role": "USER"
}
```

Retorna `200 OK` em caso de sucesso ou `400 Bad Request` se o e-mail já estiver cadastrado.

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "email": "usuario@email.com",
  "password": "suasenha"
}
```

Resposta:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Usando o token

Inclua o token em todas as requisições protegidas:

```http
Authorization: Bearer <token>
```

O token tem validade de **24 horas**. A senha é armazenada com hash BCrypt.

### Configuração do segredo JWT

Defina a variável no `application.properties`:

```properties
api.security.token.secret=seu-segredo-aqui
```

---

## Endpoints Disponíveis

> Todos os endpoints abaixo exigem autenticação via Bearer Token, exceto `/auth/*`.

### Autenticação — `/auth`

| Método | Rota | Descrição | Autenticação |
|---|---|---|---|
| POST | `/auth/register` | Registra novo usuário | Pública |
| POST | `/auth/login` | Realiza login e retorna token JWT | Pública |

### Usuários — `/users`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/users` | Lista todos os usuários |
| GET | `/users/{id}` | Busca usuário por ID |
| PUT | `/users/update/{id}` | Atualiza nome, telefone, CPF, data de nascimento e localização |
| DELETE | `/users/delete/{id}` | Remove usuário |

### Contas Bancárias — `/bank-account`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/bank-account` | Lista todas as contas |
| GET | `/bank-account/{id}` | Busca conta por ID |
| POST | `/bank-account/insert` | Cadastra nova conta (via `BankAccountDTO`) |
| PUT | `/bank-account/update/{id}` | Atualiza tipo de conta e data de fechamento |
| DELETE | `/bank-account/delete/{id}` | Remove conta |

### Categorias — `/category`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/category` | Lista todas as categorias |
| GET | `/category/{id}` | Busca categoria por ID |
| POST | `/category/insert` | Cadastra nova categoria |
| PUT | `/category/update/{id}` | Atualiza nome e limite de notificação |
| DELETE | `/category/delete/{id}` | Remove categoria |

### Despesas Mensais — `/month`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/month` | Lista todos os meses |
| GET | `/month/{id}` | Busca mês por ID |
| POST | `/month/insert` | Cadastra novo mês (via `MonthlyExpenseDTO`) |
| PUT | `/month/update/{id}` | Atualiza limite de gasto |
| DELETE | `/month/delete/{id}` | Remove mês |

### Despesas — `/expense`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/expense` | Lista todas as despesas |
| GET | `/expense/{id}` | Busca despesa por ID |
| POST | `/expense/insert` | Cadastra nova despesa (via `ExpenseDTO`) |
| DELETE | `/expense/delete/{id}` | Remove despesa |

### Localizações — `/location`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/location` | Lista todas as localizações |
| GET | `/location/{id}` | Busca localização por ID |
| POST | `/location/insert` | Cadastra nova localização |
| PUT | `/location/update/{id}` | Atualiza dados da localização |
| DELETE | `/location/delete/{id}` | Remove localização |

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

**3. Configure o `application.properties`:**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/expense_tracker
spring.datasource.username=postgres
spring.datasource.password=minhasenha

api.security.token.secret=seu-segredo-jwt-aqui
```

**4. Execute a aplicação:**

```bash
mvn spring-boot:run
```

---

## Testes

O projeto utiliza Testcontainers para testes de integração — o Spring sobe completo contra um PostgreSQL real em container, sem banco em memória.

O teste principal (`deveriaDescontarDoMensalEBanco`) valida o fluxo completo:

- Criação de localização, usuário, conta bancária, categoria e despesas
- Processamento das despesas no mês (atualização de total e limite)
- Desconto do total mensal no saldo da conta bancária

```bash
mvn test
```

---

## Melhorias Futuras

- [ ] Notificação ao atingir limite de categoria
- [ ] DTOs de resposta para evitar exposição direta das entidades nos GETs
