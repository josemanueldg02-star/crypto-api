# 🚀 Crypto API - Motor Financiero Backend

Una API RESTful desarrollada en **Java y Spring Boot** para la gestión de carteras de inversión y criptomonedas. Este proyecto está diseñado aplicando buenas prácticas de la industria, arquitectura limpia y bases de datos relacionales.

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java 
* **Framework:** Spring Boot (Web, Data JPA, Validation)
* **Base de Datos:** PostgreSQL
* **Gestor de Dependencias:** Maven
* **Arquitectura:** MVC (Modelo-Vista-Controlador) enfocado a Microservicios.

## 🏗️ Arquitectura y Modelado de Datos

El sistema implementa una base de datos relacional estricta asegurando la integridad de los datos financieros:

* **Relación One-To-Many Bidireccional:** Un `Usuario` puede poseer múltiples `Carteras` (ej. EUR, BTC, ETH).
* **Protección de Datos:** Implementación de `@JsonIgnore` para prevenir recursión infinita y optimizar la carga útil (payload) de las respuestas JSON.
* **Validación de Entradas:** Uso de `Jakarta Validation` para asegurar la integridad de los datos antes de la persistencia.

## 🔌 Endpoints Disponibles

### 👤 Usuarios
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `POST` | `/api/usuarios` | Registra un nuevo usuario en el sistema. |
| `GET` | `/api/usuarios` | Lista todos los usuarios registrados. |

### 💼 Carteras (Wallets)
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `POST` | `/api/carteras/usuario/{id}` | Crea una nueva cartera (ej. BTC) vinculada a un usuario. |
| `GET` | `/api/carteras/usuario/{id}` | Obtiene todas las carteras y saldos de un usuario específico. |

## 🚀 Próximos Pasos (Roadmap)
- [ ] Implementar motor de transacciones (Compra/Venta de criptomonedas).
- [ ] Conexión con APIs externas de mercado (Binance/CoinGecko) para precios en tiempo real.
- [ ] Manejo global de excepciones (Global Exception Handler).

---
**Autor:** Jose Manuel Dominguez Garcia
**Repositorio:** [https://github.com/josemanueldg02-star/crypto-api](https://github.com/josemanueldg02-star/crypto-api)