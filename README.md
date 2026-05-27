# 🚀 Crypto Exchange REST API

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)

Una API RESTful robusta y segura que simula el motor central (backend) de una plataforma de intercambio de criptomonedas (Exchange). Permite a los usuarios gestionar carteras de dinero fiat y criptomonedas, interactuando con precios reales del mercado.

## 🌟 Características Principales

* **Seguridad Avanzada (Spring Security + JWT):** Sistema de autenticación *stateless* basado en JSON Web Tokens. Rutas protegidas mediante filtros de autorización en cada petición.
* **Integración con Mercado Real:** Conexión con la API pública de **Binance** mediante `RestTemplate` para obtener el tipo de cambio en tiempo real al ejecutar operaciones de compra/venta.
* **Transacciones ACID:** Operaciones financieras protegidas con la anotación `@Transactional` de Spring, garantizando la consistencia de la base de datos (PostgreSQL) ante cualquier fallo durante el cruce de divisas.
* **Manejo Global de Excepciones:** Respuestas de error estandarizadas e interceptadas a través de `@ControllerAdvice`, ofreciendo JSON limpios y códigos HTTP adecuados (ej. `400 Bad Request` por saldo insuficiente).
* **Arquitectura Limpia:** Estructura multicapa (Controller, Service, Repository, Model, Security, Exception) facilitando la escalabilidad y el mantenimiento.

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3
* **Persistencia de Datos:** Spring Data JPA / Hibernate
* **Base de Datos:** PostgreSQL
* **Ciberseguridad:** Spring Security, io.jsonwebtoken (JWT)
* **Peticiones HTTP Externas:** RestTemplate

## 📖 Endpoints Principales

### 🔐 Autenticación
| Método | Ruta | Descripción | Seguridad |
|---|---|---|---|
| `POST` | `/api/auth/login` | Inicia sesión y devuelve el Token JWT | Público |

### 💼 Gestión de Carteras
| Método | Ruta | Descripción | Seguridad |
|---|---|---|---|
| `GET` | `/api/carteras/usuario/{id}` | Lista los balances (Fiat/Cripto) de un usuario | Requiere JWT |

### 💱 Motor Financiero
| Método | Ruta | Descripción | Seguridad |
|---|---|---|---|
| `POST` | `/api/transacciones/deposito/cartera/{id}` | Inyecta dinero fiat desde el banco | Requiere JWT |
| `POST` | `/api/transacciones/retiro/cartera/{id}` | Retira fondos fiat hacia el banco | Requiere JWT |
| `POST` | `/api/transacciones/comprar/usuario/{id}` | Compra criptomonedas con precio en tiempo real | Requiere JWT |
| `POST` | `/api/transacciones/vender/usuario/{id}` | Vende criptomonedas con precio en tiempo real | Requiere JWT |

*(Nota: En todas las rutas protegidas, es necesario enviar la cabecera `Authorization: Bearer <token>`)*

## 🚀 Instalación y Despliegue Local

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/josemanueldg02-star/crypto-api.git](https://github.com/josemanueldg02-star/crypto-api.git)
   cd crypto-api
   ```

2. **Configurar la base de datos:**
   Asegúrate de tener PostgreSQL instalado y en ejecución. Crea una base de datos llamada `crypto_portfolio` (o ajusta las credenciales en `src/main/resources/application.properties`).

3. **Ejecutar la aplicación:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Credenciales de prueba por defecto:**
   * **Usuario:** `josemanuel`
   * **Contraseña:** `admin123`

---
*Desarrollado como proyecto de portfolio para demostrar arquitecturas backend sólidas, integración de APIs de terceros y estándares de ciberseguridad.*