# expense_tracker_API

![Status do Projeto](https://img.shields.io/badge/status-production-brightgreen)
![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
[![Deploy](https://img.shields.io/badge/Railway-online-blueviolet)](https://expensetrackerapi-production-663e.up.railway.app)
[![Licença MIT](https://img.shields.io/badge/licenca-MIT-green)](LICENSE)

> API REST de controle de gastos pessoais com contas bancárias, categorias de gastos e histórico mensal. Autenticação via JWT, persistência em PostgreSQL, testes de integração com Testcontainers e testes unitários com Mockito. Deploy ativo no Railway.

**[→ API em produção](https://expensetrackerapi-production-663e.up.railway.app)**

---

## Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Modelo de Dados](#modelo-de-dados)
- [Autenticação](#autenticação)
- [Endpoints Disponíveis](#endpoints-disponíveis)
- [Tratamento de Exceções](#tratamento-de-exceções)
- [Como Executar](#como-executar)
- [Testes](#testes)
- [Melhorias Futuras](#melhorias-futuras)

---

## Sobre o Projeto

O **expense_tracker_API** é uma API REST desenvolvida em Java com Spring Boot para controle de gastos pessoais. O sistema permite que usuários gerenciem suas contas bancárias, registrem despesas mensais categorizadas e acompanhem limites de gasto por categoria.

As principais funcionalidades implementadas incluem:

- Autenticação stateless com JWT (registro, login e proteção de rotas)
- Controle de roles de acesso (`ADMIN` e `USER`)
- Tratamento global de exceções com respostas padronizadas (`@RestControllerAdvice`)
- Exceções customizadas por tipo: recurso não encontrado, duplicidade e regra de negócio
- Cadastro e gerenciamento de usuários com localização
- Contas bancárias associadas a cada usuário
- Registro de despesas agrupadas por mês (`MonthlyExpense`)
- Categorização de gastos com limite de notificação por categoria
- Controllers REST completos para todos os recursos
- DTOs para desacoplamento entre camada de transporte e entidades
- Processamento de despesas: atualiza automaticamente o total mensal e o saldo da conta bancária
- Validação de saldo insuficiente ao fechar o mês
- Testes de integração com banco PostgreSQL real (Testcontainers)
- Testes unitários de serviços com Mockito

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
| Docker | — | Container do banco de dados (dev) |
| Testcontainers | — | PostgreSQL real nos testes de integração |
| JUnit 5 | — | Testes unitários e de integração |
| Mockito | — | Mocks nos testes unitários |
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
│       │   ├── AuthenticationController.java   # Endpoints /auth/register e /auth/login
│       │   ├── UserController.java
│       │   ├── BankAccountController.java
│       │   ├── CategoryController.java
│       │   ├── MonthlyExpenseController.java
│       │   ├── ExpenseController.java
│       │   └── LocationController.java
│       ├── dtos/
│       ├── entities/
│       │   ├── user/
│       │   │   ├── User.java                   # Implementa UserDetails
│       │   │   └── UserRole.java               # Enum: ADMIN, USER
│       │   ├── Location.java
│       │   ├── BankAccount.java
│       │   ├── Category.java
│       │   ├── MonthlyExpense.java
│       │   └── Expense.java
│       ├── infra/
│       │   ├── exception/
│       │   │   ├── GlobalExceptionHandler.java # Tratamento global (@RestControllerAdvice)
│       │   │   └── StandartError.java
│       │   ├── AuthorizationService.java
│       │   ├── SecurityConfiguration.java
│       │   ├── SecurityFilter.java             # Intercepta requisições e valida JWT
│       │   └── TokenService.java
│       ├── repositories/
│       ├── services/
│       │   └── exceptions/
│       │       ├── ResourceNotFoundException.java
│       │       ├── DuplicateResourceException.java
│       │       └── BusinessException.java
│       └── ExpenseTrackerApplication.java
└── test/
    └── java/com/eduardo/expense_tracker/
        ├── integration/
        │   └── ExpenseTrackerApplicationTests.java
        └── unit/
            └── service/
                └── UserServiceTest.java
```

---

## Modelo de Dados

```
Location (1) ──── (N) User (1) ──── (N) BankAccount (1) ──── (N) MonthlyExpense (1) ──── (N) Expense
                                                                                                  │
                                                                                           (N) Category
```

- **User** — nome, email, senha (BCrypt), CPF, telefone, data de nascimento, role e localização. Implementa `UserDetails`
- **BankAccount** — tipo de conta, saldo, data de fechamento do cartão
- **Category** — nome e limite de notificação de gastos
- **MonthlyExpense** — mês de referência, total gasto, limite mensal e conta bancária vinculada
- **Expense** — valor, descrição, momento do gasto, categoria e mês de referência

**Fluxo de processamento:**

1. `ExpenseService.processExpense()` — adiciona o valor ao total do mês e desconta do limite da categoria. Lança `BusinessException` se exceder o limite
2. `MonthlyExpenseService.processMonthlyExpense()` — desconta o total mensal do saldo da conta. Lança `BusinessException` se saldo insuficiente

---

## Autenticação

A API utiliza autenticação **stateless com JWT**. Rotas públicas: `/auth/register` e `/auth/login`.

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

```http
Authorization: Bearer <token>
```

Token válido por **24 horas**. Senha armazenada com BCrypt.

---

## Endpoints Disponíveis

> Todos os endpoints exigem autenticação via Bearer Token, exceto `/auth/*`.
> Base URL em produção: `https://expensetrackerapi-production-663e.up.railway.app`

### Autenticação — `/auth`

| Método | Rota | Descrição | Auth |
|---|---|---|---|
| POST | `/auth/register` | Registra novo usuário | Pública |
| POST | `/auth/login` | Login e retorna token JWT | Pública |

### Usuários — `/users`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/users` | Lista todos os usuários |
| GET | `/users/{id}` | Busca usuário por ID |
| PUT | `/users/update/{id}` | Atualiza dados do usuário |
| DELETE | `/users/delete/{id}` | Remove usuário |

### Contas Bancárias — `/bank-account`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/bank-account` | Lista todas as contas |
| GET | `/bank-account/{id}` | Busca conta por ID |
| POST | `/bank-account/insert` | Cadastra nova conta |
| PUT | `/bank-account/update/{id}` | Atualiza conta |
| DELETE | `/bank-account/delete/{id}` | Remove conta |

### Categorias — `/category`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/category` | Lista todas as categorias |
| POST | `/category/insert` | Cadastra nova categoria |
| PUT | `/category/update/{id}` | Atualiza categoria |
| DELETE | `/category/delete/{id}` | Remove categoria |

### Despesas Mensais — `/month`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/month` | Lista todos os meses |
| POST | `/month/insert` | Cadastra novo mês |
| PUT | `/month/update/{id}` | Atualiza limite de gasto |
| DELETE | `/month/delete/{id}` | Remove mês |

### Despesas — `/expense`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/expense` | Lista todas as despesas |
| POST | `/expense/insert` | Cadastra nova despesa |
| DELETE | `/expense/delete/{id}` | Remove despesa |

### Localizações — `/location`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/location` | Lista todas as localizações |
| POST | `/location/insert` | Cadastra nova localização |
| PUT | `/location/update/{id}` | Atualiza localização |
| DELETE | `/location/delete/{id}` | Remove localização |

---

## Tratamento de Exceções

| Exceção | HTTP | Quando ocorre |
|---|---|---|
| `ResourceNotFoundException` | 404 | Recurso não encontrado |
| `DuplicateResourceException` | 409 | E-mail já cadastrado |
| `BusinessException` | 400 | Regras de negócio violadas |
| `Exception` | 500 | Erros inesperados |

Formato padrão de erro:

```json
{
  "timestamp": "2026-05-01T12:00:00Z",
  "status": 404,
  "error": "Resource not found",
  "message": "User not found with id: 99"
}
```

---

## Como Executar

### Pré-requisitos

- Java 25+
- Docker Desktop
- Maven

### Passos

```bash
git clone https://github.com/Antonio-Eduardo/expense_tracker_API.git
cd expense_tracker_API
```

```bash
docker run --name expense-postgres \
  -e POSTGRES_PASSWORD=minhasenha \
  -e POSTGRES_DB=expense_tracker \
  -p 5432:5432 \
  -d postgres:16-alpine
```

Configure o `application-dev.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/expense_tracker
spring.datasource.username=postgres
spring.datasource.password=minhasenha
api.security.token.secret=seu-segredo-jwt-aqui
```

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## Testes

```bash
mvn test
```

### Testes de Integração (Testcontainers)

Valida o fluxo completo contra PostgreSQL real em container:
- Criação de localização, usuário, conta bancária, categoria e despesas
- Processamento de despesas com atualização de total e limite
- Desconto do total mensal no saldo da conta bancária

### Testes Unitários (Mockito)

Cobrem os principais fluxos do `UserService` com repositórios mockados.

---

## Melhorias Futuras

- [ ] Notificação ao atingir limite de categoria
- [ ] DTOs de resposta para evitar exposição direta das entidades nos GETs
- [ ] Testes unitários para os demais serviços
- [ ] Documentação da API com Swagger/OpenAPI
