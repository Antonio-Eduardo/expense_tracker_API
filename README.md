# expense_tracker_API

![Status do Projeto](https://img.shields.io/badge/status-production-brightgreen)
![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
[![Deploy](https://img.shields.io/badge/Railway-online-blueviolet)](https://expensetrackerapi-production-663e.up.railway.app)
[![Licença MIT](https://img.shields.io/badge/licenca-MIT-green)](LICENSE)

> API REST de controle de gastos pessoais com contas bancárias, categorias de gastos e histórico mensal. Autenticação via JWT, persistência em PostgreSQL, testes de integração com Testcontainers e testes unitários com Mockito. Deploy ativo no Railway.

**[→ API em produção](https://expensetrackerapi-production-663e.up.railway.app)**  
**[→ Documentação Swagger](https://expensetrackerapi-production-663e.up.railway.app/swagger-ui/index.html)**

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
- DTOs desacoplados para request e response em todos os recursos
- Processamento de despesas: atualiza automaticamente o total mensal e o limite disponível
- Validação de saldo insuficiente ao fechar o mês
- Documentação interativa com Swagger/OpenAPI (acessível em `/swagger-ui/index.html`)
- Testes de integração com banco PostgreSQL real (Testcontainers)
- Testes unitários de todos os serviços principais com Mockito

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
| Springdoc OpenAPI | — | Documentação interativa (Swagger UI) |
| Lombok | — | Redução de boilerplate |
| Maven | — | Gerenciamento de dependências |

---

## Estrutura do Projeto

```
src/
├── main/
│   └── java/com/eduardo/expense_tracker/
│       ├── controllers/
│       │   ├── AuthenticationController.java   # POST /auth/register e /auth/login
│       │   ├── BankAccountController.java
│       │   ├── CategoryController.java
│       │   ├── ExpenseController.java
│       │   ├── HomeController.java             # Redireciona / para o Swagger UI
│       │   ├── LocationController.java
│       │   ├── MonthlyExpenseController.java
│       │   └── UserController.java
│       ├── dtos/
│       │   ├── request/                        # DTOs de entrada (request body)
│       │   └── response/                       # DTOs de saída (response body)
│       ├── entities/
│       │   ├── user/
│       │   │   ├── User.java                   # Implementa UserDetails
│       │   │   └── UserRole.java               # Enum: ADMIN, USER
│       │   ├── BankAccount.java
│       │   ├── Category.java
│       │   ├── Expense.java
│       │   ├── Location.java
│       │   └── MonthlyExpense.java
│       ├── infra/
│       │   ├── exception/
│       │   │   ├── GlobalExceptionHandler.java # Tratamento global (@RestControllerAdvice)
│       │   │   └── StandartError.java          # Modelo padrão de erro
│       │   └── security/
│       │       ├── AuthorizationService.java   # Implementa UserDetailsService
│       │       ├── SecurityConfiguration.java  # Configuração de CORS, CSRF e rotas
│       │       ├── SecurityFilter.java         # Intercepta requisições e valida JWT
│       │       └── TokenService.java           # Geração e validação de tokens JWT
│       ├── repositories/
│       │   ├── BankAccountRepository.java
│       │   ├── CategoryRepository.java
│       │   ├── ExpenseRepository.java
│       │   ├── LocationRepository.java
│       │   ├── MonthlyExpenseRepository.java
│       │   └── UserRepository.java
│       ├── services/
│       │   ├── exceptions/
│       │   │   ├── BusinessException.java
│       │   │   ├── DuplicateResourceException.java
│       │   │   └── ResourceNotFoundException.java
│       │   ├── BankAccountService.java
│       │   ├── CategoryService.java
│       │   ├── ExpenseService.java
│       │   ├── LocationService.java
│       │   ├── MonthlyExpenseService.java
│       │   └── UserService.java
│       ├── swagger/config/
│       │   └── SwaggerConfig.java              # Configuração do OpenAPI/Swagger
│       └── ExpenseTrackerApplication.java
└── test/
    └── java/com/eduardo/expense_tracker/
        ├── integration/
        │   └── ExpenseTrackerApplicationTests.java  # Testes de integração com Testcontainers
        ├── TestcontainersConfiguration.java
        ├── TestExpenseTrackerApplication.java
        └── unit/service/
            ├── BankAccountTest.java
            ├── CategoryTest.java
            ├── ExpenseTest.java
            ├── LocationTest.java
            ├── MonthlyExpenseTest.java
            └── UserServiceTest.java
```

---

## Modelo de Dados

```
Location (1) ──── (N) User (1) ──── (N) BankAccount (1) ──── (N) MonthlyExpense (1) ──── (N) Expense
                                                                                                  │
                                                                                           (N) Category
```

- **Location** — cidade, estado, endereço e CEP
- **User** — nome, email, senha (BCrypt), CPF, telefone, data de nascimento, role e localização. Implementa `UserDetails`
- **BankAccount** — tipo de conta, saldo, data de fechamento do cartão, vinculada a um usuário
- **Category** — nome e limite de notificação de gastos
- **MonthlyExpense** — mês de referência, total gasto, limite mensal e conta bancária vinculada
- **Expense** — valor, descrição, momento do gasto, categoria e mês de referência

**Fluxo de processamento:**

1. `ExpenseService.processExpense()` — soma o valor ao total do mês e desconta do limite disponível. Lança `BusinessException` se o valor da despesa exceder o limite mensal
2. `MonthlyExpenseService.processMonthlyExpense()` — desconta o total mensal do saldo da conta. Lança `BusinessException` se o saldo for insuficiente

---

## Autenticação

A API utiliza autenticação **stateless com JWT**. Rotas públicas: `/auth/register`, `/auth/login` e `/swagger-ui/**`.

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
| GET | `/category/{id}` | Busca categoria por ID |
| POST | `/category/insert` | Cadastra nova categoria |
| PUT | `/category/update/{id}` | Atualiza categoria |
| DELETE | `/category/delete/{id}` | Remove categoria |

### Despesas Mensais — `/month`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/month` | Lista todos os meses |
| GET | `/month/{id}` | Busca mês por ID |
| POST | `/month/insert` | Cadastra novo mês |
| PUT | `/month/update/{id}` | Atualiza limite de gasto |
| DELETE | `/month/delete/{id}` | Remove mês |

### Despesas — `/expense`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/expense` | Lista todas as despesas |
| GET | `/expense/{id}` | Busca despesa por ID |
| POST | `/expense/insert` | Cadastra nova despesa |
| DELETE | `/expense/delete/{id}` | Remove despesa |

### Localizações — `/location`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/location` | Lista todas as localizações |
| GET | `/location/{id}` | Busca localização por ID |
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

Acesse a documentação em: `http://localhost:8080/swagger-ui/index.html`

---

## Testes

```bash
mvn test
```

### Testes de Integração (Testcontainers)

Sobem um container PostgreSQL real e validam o fluxo completo via MockMvc:

- Criação de conta bancária, categoria e despesa mensal
- `POST /expense/insert` — criação com validação de campos e IDs relacionados
- `GET /expense/{id}` — busca por ID com retorno correto
- `GET /expense` — listagem de todas as despesas
- `DELETE /expense/delete/{id}` — remoção com status 204
- `GET /expense/{id}` com ID inexistente — retorna 404

### Testes Unitários (Mockito)

Cobrem os principais fluxos de todos os serviços com repositórios mockados:

| Serviço | Cenários cobertos |
|---|---|
| `UserService` | Criar, buscar por email, listar, atualizar e deletar usuário |
| `BankAccountService` | Inserir, buscar por ID, listar, atualizar e deletar conta |
| `CategoryService` | Criar, buscar por ID, listar, atualizar e deletar categoria |
| `ExpenseService` | Criar, buscar por ID, listar e deletar despesa |
| `LocationService` | Criar, buscar por ID, listar, atualizar e deletar localização |
| `MonthlyExpenseService` | Criar, buscar por ID, listar, atualizar e deletar gasto mensal |

---

## Melhorias Futuras

- [ ] Notificação ao atingir limite de categoria
- [ ] Endpoint `GET /expense/{id}` com retorno mais detalhado (categoria e mês expandidos)
- [ ] Paginação nos endpoints de listagem
- [ ] Filtros por período, categoria e conta bancária nas despesas
- [ ] Relatório de gastos por categoria no mês