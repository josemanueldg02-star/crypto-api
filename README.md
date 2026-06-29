# Crypto Exchange — REST API

A secure REST API simulating the backend engine of a cryptocurrency exchange 
platform. Users manage fiat and crypto wallets and execute buy/sell orders 
against real-time market prices from Binance. Built as the backend for 
[crypto-dashboard](https://github.com/josemanueldg02-star/crypto-dashboard).

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-green)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-latest-336791)](https://www.postgresql.org/)
[![Binance API](https://img.shields.io/badge/Binance-API-F0B90B)](https://binance-docs.github.io/apidocs/)

---

## Features

- **Stateless JWT authentication** — every protected route validates the token 
via a custom filter; no server-side sessions
- **Real-time pricing** — buy/sell orders fetch live prices from the Binance 
public API via `RestTemplate` at execution time
- **ACID transactions** — all financial operations are wrapped in `@Transactional` 
to guarantee database consistency on failure
- **Global exception handling** — `@ControllerAdvice` intercepts all errors and 
returns standardized JSON responses with appropriate HTTP status codes

---

## Tech Stack

- **Java 17**, Spring Boot 3
- **Security:** Spring Security + `io.jsonwebtoken` (JWT)
- **Persistence:** Spring Data JPA / Hibernate + PostgreSQL
- **External API:** Binance REST API via `RestTemplate`
- **Architecture:** Controller → Service → Repository → Model

---

## API Reference

**Authentication**

| Method | Route | Description | Auth |
|--------|-------|-------------|------|
| `POST` | `/api/auth/login` | Returns JWT token | Public |

**Wallets**

| Method | Route | Description | Auth |
|--------|-------|-------------|------|
| `GET` | `/api/carteras/usuario/{id}` | List fiat and crypto balances | JWT |

**Transactions**

| Method | Route | Description | Auth |
|--------|-------|-------------|------|
| `POST` | `/api/transacciones/deposito/cartera/{id}` | Deposit fiat funds | JWT |
| `POST` | `/api/transacciones/retiro/cartera/{id}` | Withdraw fiat funds | JWT |
| `POST` | `/api/transacciones/comprar/usuario/{id}` | Buy crypto at live price | JWT |
| `POST` | `/api/transacciones/vender/usuario/{id}` | Sell crypto at live price | JWT |

All protected routes require the header: `Authorization: Bearer <token>`

---

## Technical Highlights

- **Custom JWT filter.** A `OncePerRequestFilter` validates and extracts the 
token on every request, keeping authentication logic decoupled from business logic.
- **Live price injection.** Buy/sell endpoints call the Binance API at order 
execution time, so the price used is always the current market rate.
- **Layered exception model.** Domain exceptions (e.g. insufficient funds) 
propagate cleanly to `@ControllerAdvice`, which maps them to `400 Bad Request` 
with a descriptive JSON body.

---

## Running Locally

**Prerequisites:** Java 17, Maven, PostgreSQL

```bash
git clone https://github.com/josemanueldg02-star/crypto-api.git
cd crypto-api
```

Create a database named `crypto_portfolio` in PostgreSQL, then configure 
credentials in `src/main/resources/application.properties`.

```bash
./mvnw spring-boot:run
```

API available at `http://localhost:8080`.

**Test credentials:**
- Username: `josemanuel`
- Password: `admin123`

To use with the frontend, also run [crypto-dashboard](https://github.com/josemanueldg02-star/crypto-dashboard).

---

## Author

**José Manuel Domínguez García** · [@josemanueldg02-star](https://github.com/josemanueldg02-star)
