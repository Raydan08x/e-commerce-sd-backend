# E-commerce Sierra Dorada — Backend

API REST para el e-commerce Sierra Dorada, construida con Java 21, Spring Boot 4.1, Gradle, Spring Web MVC, Spring Data JPA, MySQL, Bean Validation, Spring Security y JWT.

Frontend de referencia: https://github.com/Raydan08x/e-commerce-sierra-dorada

## Requisitos

- Java 21 o superior.
- MySQL 8.
- No es necesario instalar Gradle; el repositorio incluye Gradle Wrapper.

## Base de datos

1. Ejecute `data-base/tablas ER ecommerce sierra dorada SCRIPT.sql` en MySQL.
2. Configure las variables de entorno, o use los valores locales predeterminados:

| Variable | Valor predeterminado |
| --- | --- |
| `DB_URL` | `jdbc:mysql://localhost:3306/e-commerce-sierra-dorada` |
| `DB_USERNAME` | `root` |
| `DB_PASSWORD` | vacío |
| `JWT_SECRET` | clave local de desarrollo; cámbiela en producción |
| `JWT_EXPIRATION_MS` | `86400000` (24 horas) |
| `CORS_ALLOWED_ORIGINS` | localhost y GitHub Pages del frontend |

La configuración completa está en `src/main/resources/application.properties`. Hibernate usa `ddl-auto=update` de forma predeterminada para sincronizar la columna de rol agregada al modelo.

## Ejecución

En Windows:

```powershell
.\gradlew.bat bootRun
```

La API queda disponible en `http://localhost:8080`.

Para ejecutar las pruebas:

```powershell
.\gradlew.bat test
```

## Autenticación JWT

- `POST /registro` o `POST /api/auth/registro`: crea un cliente y devuelve el token.
- `POST /login` o `POST /api/auth/login`: recibe `usuario` (email) y `password` y devuelve el token.
- Las rutas protegidas requieren `Authorization: Bearer <token>`.
- Las escrituras de productos, categorías, métodos de pago y usuarios requieren rol `ADMIN`.

Para convertir un usuario existente en administrador durante el desarrollo:

```sql
UPDATE usuarios SET rol = 'ADMIN' WHERE email = 'admin@sierradorada.com';
```

## Endpoints CRUD

| Recurso | Rutas | Acceso |
| --- | --- | --- |
| Productos | `/productos`, `/api/productos` | GET público; POST/PUT/DELETE administrador |
| Categorías | `/categorias`, `/api/categorias` | GET público; POST/PUT/DELETE administrador |
| Usuarios | `/usuarios`, `/api/usuarios` | administrador |
| Pedidos | `/pedidos`, `/api/pedidos` | autenticado |
| Métodos de pago | `/api/metodos-pago` | GET público; escritura administrador |
| Pagos | `/api/pagos` | autenticado |

Cada recurso admite `GET`, `GET /{id}`, `POST`, `PUT /{id}` y `DELETE /{id}`. Productos también permite los filtros `soloActivos`, `buscar` y `categoriaId`; pedidos y pagos se pueden filtrar mediante `usuarioId` y `pedidoId`.

## Relación con el frontend

El frontend analizado todavía guarda usuarios, sesión y productos en `localStorage`. Para conectarlo a esta API se deben reemplazar esas operaciones en `login.js`, `registro.js`, `productos.js` y `admin.js` por solicitudes HTTP, guardar el token retornado y enviarlo en el encabezado `Authorization`.

## Integrantes

- Girley Barahona
- Carlos Madero
- Valeria Gutierrez
