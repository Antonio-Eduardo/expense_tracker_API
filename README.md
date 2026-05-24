# expense_tracker_API

![Status do Projeto](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
[![Licença MIT](https://img.shields.io/badge/licenca-MIT-green)](LICENSE)

> API REST de controle de gastos pessoais com autenticação, contas bancárias, categorias de gastos e histórico mensal. Persistência em PostgreSQL via Docker e testes de integração com Testcontainers.

---

## Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura Planejada](#estrutura-planejada)
- [Como Executar](#como-executar)
- [Testes](#testes)
- [Melhorias Futuras](#melhorias-futuras)

---

## Sobre o Projeto

O **expense_tracker_API** é uma API REST desenvolvida em Java com Spring Boot para controle de gastos pessoais. O sistema permite que usuários gerenciem suas contas bancárias, registrem gastos mensais categorizados e acompanhem limites por categoria.

O projeto está em fase inicial de desenvolvimento.

---

## Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|-----------|--------|-----|
| Java | 25 | Linguagem principal |
| Spring Boot | 4.0.6 | Framework principal |
| Spring Data JPA | — | Repositórios e persistência |
| Spring Security | — | Autenticação e autorização |
| Spring Validation | — | Validação de dados de entrada |
| PostgreSQL | 16 | Banco de dados |
| Flyway | — | Versionamento do schema do banco |
| Docker | — | Container do banco de dados |
| Testcontainers | — | PostgreSQL real nos testes de integração |
| Lombok | — | Redução de boilerplate |
| Maven | — | Gerenciamento de dependências |

---

## Estrutura Planejada

```
src/
├── main/
│   └── java/com/eduardo/expense_tracker/
│       ├── config/
│       ├── entities/
│       ├── enums/
│       ├── exceptions/
│       ├── repository/
│       ├── service/
│       └── controller/
└── test/
    └── java/com/eduardo/expense_tracker/
```

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

**3. Execute a aplicação:**
```bash
mvn spring-boot:run
```

---

## Testes

O projeto utiliza Testcontainers para testes de integração — o Spring sobe completo contra um PostgreSQL real em container, sem banco em memória.

```bash
mvn test
```

---

## Melhorias Futuras

- [ ] Modelagem e criação das entidades (User, Conta Bancária, Gasto Mensal, Category, Local)
- [ ] Flyway migrations
- [ ] Endpoints REST
- [ ] Autenticação com Spring Security
- [ ] Documentação com Swagger / OpenAPI
- [ ] Testes de integração
