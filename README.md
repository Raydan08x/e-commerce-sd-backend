# E-commerce Sierra Dorada — Backend

API REST para el e-commerce Sierra Dorada, construida con Java 21, Spring Boot 4.1, Gradle, Spring Web MVC, Spring Data JPA, MySQL, Bean Validation, Spring Security y JWT.

Frontend de referencia: https://github.com/Raydan08x/e-commerce-sierra-dorada

## Requisitos

- JDK 21 instalado.
- MySQL 8.
- No es necesario instalar Gradle; el repositorio incluye Gradle Wrapper.

## Base de datos

### Configuración desde IntelliJ IDEA

La configuración se realiza con el archivo [setup-ecommerce-sd.sql](data-base/setup-ecommerce-sd.sql). Es SQL estándar para MySQL 8 y se ejecuta directamente desde la herramienta **Database** de IntelliJ; ya no se necesita un script de PowerShell.

1. Cree o abra en IntelliJ una conexión administrativa local a MySQL con el usuario `root` y la contraseña de root de esa computadora.
2. Abra `data-base/setup-ecommerce-sd.sql`, seleccione esa conexión administrativa y ejecute el archivo completo. El script crea la base, el usuario del proyecto, sus permisos y las siete tablas. Puede volver a ejecutarse sin borrar datos.
3. Configure la conexión de uso normal del proyecto con estos valores:

| Campo de IntelliJ | Valor |
| --- | --- |
| Name | `ecommerce-sd` |
| Host | `localhost` |
| Port | `3306` |
| User | `ecommerce_sd` |
| Password | `Sierra2026*` |
| Database | `e-commerce-sierra-dorada` |

4. Pulse **Test Connection**. En la pestaña **Schemas**, seleccione `e-commerce-sierra-dorada` y sincronice la conexión para ver las tablas.

La contraseña de `root` no puede fijarse desde el repositorio porque pertenece a la instalación local de MySQL. Solo se usa una vez para ejecutar el script administrativo. El backend usa siempre la cuenta `ecommerce_sd`; el nombre visual de la conexión en IntelliJ no afecta a la aplicación.

Configure las variables de entorno, o use los valores locales predeterminados:

| Variable | Valor predeterminado |
| --- | --- |
| `DB_URL` | `jdbc:mysql://localhost:3306/e-commerce-sierra-dorada` |
| `DB_USERNAME` | `ecommerce_sd` |
| `DB_PASSWORD` | `Sierra2026*` |
| `JWT_SECRET` | clave local de desarrollo; cámbiela en producción |
| `JWT_EXPIRATION_MS` | `86400000` (24 horas) |
| `CORS_ALLOWED_ORIGINS` | localhost y GitHub Pages del frontend |

La configuración completa está en `src/main/resources/application.properties`. Hibernate usa `ddl-auto=update` de forma predeterminada para sincronizar la columna de rol agregada al modelo.

## Ejecución

En Windows:

```powershell
.\gradlew.bat bootRun
```

La API queda disponible en `http://localhost:8081`. En este equipo el puerto `8080` ya está ocupado por Docker/Traefik; en despliegue se puede sobrescribir con la variable `PORT`.

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
