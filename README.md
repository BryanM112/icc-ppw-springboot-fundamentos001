# 01_configuracion

## Ejecutarlo

.\gradlew bootRun

## Captura de verificación de Java

![Verificación de Java](/assets/01-verificacion-java.png)

## Captura del servidor Spring Boot ejecutándose

![Servidor Spring boot ejecutandose](/assets/02-springboot-ejecutandose.png)

## Captura del endpoint /api/status funcionando en el navegador o Postman o Bruno

![Captura del endpoint](/assets/03-endpoint.png)

## Captura del siguiente comando en terminal

![Comando en el terminal ls](/assets/04-comando-ls.png)

En esta práctica aprendí a crear una aplicación utilizando Spring boot. Primero verifique la versión de java, luego generé el proyecto mediante Spring Initializr utilizando Gradle como herramienta principal.

También se implemento un endpoint /api/status. Un endpoint es una dirección de la aplicación a donde los clientes pueden enviar solicitudes HTTP. 

Además comprendí que spring boot simplifica y facilita la creación de aplicaciones backend ya que incorpora un servidor embebido, en este proyecto, Tomcat. Debido a esto, no necesitamos instalar un servidor web externo.

# Creación de Students

## Students /models

Se creo esta carpeta y una clase Student.java para presentar la información de estudiante dentro de la aplicación. la clase contiene variables como id, age y name. Más constructores y getters y setters. 

## Students /controllers

Se creo esta carpeta y una clase StudentController.java para exponer los endpoints Rest relacionado con estudiantes

el Endpoint GET /students se encarga de devolver la lista de estudiantes en formato JSON

el Endpoint GET /students/count se encarga de devolver la cantidad de estudiantes registrados en la lista.

Con esta implementación se aprendió el uso de los controladores para recibir solicitudes HTTP y devolver respuestas en formato JSON

# 03_api_rest

## Captura Get

Captura de get de 3 productos existentes.

![03-get-product](/assets/03-get-product.png)

## Captura Get id

Captura de un get id de un producto existente.
![03-get-product-id](/assets/03-get-product-id.png)

## Captura delete

Captura de un delete de un producto existente

![03-delete-existente1](/assets/03-delete-product-existente1.png)

Ya no aparece en el get

![03-delete-existente2](/assets/03-delete-product-existente2.png)

## Captura delete, no existente

Captura de un delete de un producto no existente

![03-delete-noexistente](/assets/03-delete-product-noexistente.png)

# 04_servicios

## Captura de ProductServiceImpl.java

en la captura se refleja el uso de @Service, lista de memoria, generación ID, uso del mapper

![ProductServiceImpl](/assets/04-productserviceimpl.png)

## Captura de ProductController.java

En la captura se refleja la inyección de ProductService, los endpoints llamando al servicio y la ausencia de lógica CRUD dentro del controlador

![ProductController](/assets/04-productcontroller.png)

## ¿Cómo se inyecta el servicio en el controlador?

El servicio se inyecta en el controlador mediante inyección de dependencias. ProductController necesita una implementación de ProductService. Pero como ProductServiceImpl está anotada con @Service, Spring crea automáticamente una instancia que proporciona al controlador. Así, el controlador recibe solamente peticiones HTTP y la lógica de negocio lo deja al servicio.

# 05_repositorios_persistencia

## Captura de 5 productos creados en PostSQL

![Captura de 5 productos](/assets/05-5productos.png)

## Explicación del flujo de datos desde la API REST hasta PostgreSQL y viceversa

El proyecto se estructura en capas y cada una separa responsabilidades de cada componente. El flujo de datos, cuando un cliente realiza una petición HTTP a la API REST es la siguiente:

Primero va al Controller, recibe la solicitud HTTP y entrega la operación al servicio correspondiente.

Segundo va al Servicio, contiene la lógica del negocio. Es decir, procesas la información recibida y realiza las operaciones necesarias.

Tercero va al Mapper, Convierte los datos entre DTOs, modelos y entidades para que la capa de presentación y persistencia se encuentren separadas. 

Cuarto va al repository, Aquí se utiliza Spring DATA JPA que interactua con PostgreSQL. Esto nos evita la necesidad de escribir consultas básicas.

Quinto y último va a PostgreSQL. Se almacena de manera permanente la información en las tablas correspondientes.

Para el sentido inverso es similar:

Primero, en PostgreSQL se almacenan los registros de manera permanente.

Segundo, en Repository, consulta la base de datos utilizando Spring DATA JPA y consigue las entidades adecuadas.

Tercero, en service, recibe las entidades y coordina la lógica de negocio necesaria.

Cuarto, Mapper transforma las entidades en modelos y después en DTOs. Su objetivo es evitar exponer información interna de la aplicación.

Quinto, Controller recibe los DTOs, entonces devuelve al cliente en formato JSON mediante el API REST.

Sexto, el cliente recibe la respuesta y muestra los datos que se han solicitado.

Para evitar generar código innecesario y repetitivo. Las entidades, tanto UserEntity como ProductEntity heredan de BaseEntity atributos comúnes en todas las tablas como el id, createAt, updateAt, deleted.

También, BaseEntity utiliza @PrePersist y @PreUpdate para actualizar de forma automática las fechas de creación y modificación cuando una entidad se modifica o se almacena en PostgreSQL.

La herencia BaseEntity nos evita la duplicación de código, además de que las entidades comparten la estructura básica.

# 06_modelos_dtos_validacion

## Captura de respuesta de error al enviar un POST inválido

![post inválido](/assets/06-error-postinvalido.png)

## Captura de CRUD de productos validado correctamente

error al crear el producto con precio negativo

![precio negativo](/assets/06-producto-precio-negativo.png)

error al actualizar producto eliminado

![error al actualizr producto eliminado](/assets/06-error-actualizar-producto-eliminado.png)

findAll no devuelve productos eliminados. Se creo un producto con id 8, luego se procedió borrarlo, podemos obvservar que no se visualiza el producto 8 después de eliminarlo

![eliminar producto 8](/assets/06-producto-eliminar.png)

![findall no devuelve productos eliminados](/assets/06-get-producto-eliminado.png)

# 08_relacion_entidades

## Capturas

Captura de la descripción de tabla products en PostgreSQL

![Descripcion tabla productos](/assets/08-estructura-productos.png)

Captura de la creación de un producto en bruno

![Creacion de producto con sus relaciones](/assets/08-Creacion-Productos-Bruno.png)

Captura de consulta de productos por categoría

![Consulta de productos por categoría](/assets/08-get-productos-categoria.png)

## ¿Cómo se relaciona ProductEntity con UserEntity y CategoryEntity usando @ManyToOne y @JoinColumn?

ProductEntity se relaciona con UserEntity y CategoryEntity usando @ManyToOne, es decir, que muchos productos pueden pertenecer a un solo usuario y a una misma categoría. MIentras que @JoinColumn se encarga de definir la columna de clave foránea en la tabla products usando user_id y category_id. 

# 09_relacion_requestparam

## Capturas

Captura de consulta con filtros por usuario

![Captura de consulta con filtros por usuario](/assets/09-captura-de-consulta-con-filtros-por-usuario.png)

Captura de consulta con filtros por categoría

![Captura de consulta con filtros por categoría](/assets/09-captura-de-consulta-con-filtro-por-categoria.png)

## ¿Por qué se usa ProductService y ProductRepository para consultar productos aunque el endpoint esté dentro del contexto /users/{id}/products o /categories/{id}/products?

porque el recurso principal que se está consultando es Product. Esa es la razón por la cual la lógica del negocio que se relaciona con la búsqueda, validación, y filtrado se implementa en ProductService y las consultas de base de datos en ProductRepository

## ¿Qué cambió al pasar de Product N ──── 1 Category a Product N ──── N Category?

Product N ----- 1 Category, significa que un producto podía pertenecer a una única categoría. Se implementó una Foreign key llamada category_id en la tabla de products

Al cambiar la estructura a Product N ----- N Category, se traduce a que un producto ahora puede pertenecer a múltiples categorías y una categoría a varios productos. Se elimino el FK en la tabla de products y en su lugar se creo una tabla intermedia (product_categoires), que almacena las asosiaciones entre productos y categorías.

# 10_paginación

## Capturas

Captura de respuesta con page

![captura de respuesta con page](/assets/10-captura-de-respuesta-con-page.png)

Captura de respuesta con Slice

![Captura de respuesta con Slice](/assets/10-captura-de-respuesta-con-slice.png)

Captura de error por paginación inválida

![Captura de error por paginación inválida](/assets/10-captura-de-errpr-por-paginacion-invalida.png)

Captura de endpoint de categoría paginado

![Captura de endpoint de categoría paginado](/assets/10-captura-de-endpoint-de-categoria-paginado.png)

Captura de endpoint de categoría paginado

![Captura de endpoint de categoría paginado](/assets/10-captura-de-endpoint-de-categoria-slice.png)

## ¿Cuál es la diferencia entre page y slice?

La diferencia está en que Page se encarga de entregar información adicional, en esa información se encuentra la cantidad total de elementos, la cantidad total de páginas, la ubicación precisa de la página actual. Para realizar esto, page hace un count. Por lo que la consulta tardará un poco más

Sin embargo, el slice no hace este count adicional, en su lugar entrega la información necesaria para determinar si existe una página anterior o siguiente. Ofrece un mejor rendimiento y su tiempo de respuesta para la consulta es más rápida.

## ¿Por qué la paginación debe aplicarse en el repositorio y no después de traer todos los datos en memoria?

Porque si la paginación se aplica después de traer los datos en memoria, el servidor tendría que cargar todos los registros, lo que da como resultado más consumo de memoria, aumenta el tiempo de carga y envia respuestas de mayor tamaño. Esto se traduce como peor rendimiento. Al usar el repositorio Spring Data JPA traduce automáticamente los parámetros de paginación a instrucciones SQL. En pocas palabras, la base de datos solo devuelve los registros solicitados.





# 15 Documentación de Endpoints con Swagger, OpenAPI y Seguridad JWT

## Capturas

### Captura de Swagger UI cargado

![Swagger UI cargado](/assets/15-Swagger-Ui-cargado.png)

### Captura del JSON OpenAPI

![JSON OpenAPI](/assets/15-Json-OpenApi.png)

### Captura de AuthController documentado

![AuthController documentado](/assets/15-AuthController-Documentado.png)

### Captura del botón Authorize

![botón Authorize](/assets/15-boton-authorize.png)

### Captura de endpoint protegido sin token

![endpoint protegido sin token](/assets/15-endpoint-protegido-sin-token.png)

### Captura de endpoint protegido con token desde Swagger

![endpoint protegido con token](/assets/15-endpoint-protegido-con-tokekn.png)

### Captura de endpoint ADMIN con usuario normal

![endpoint ADMIN con usuario normal](/assets/15-endpoint-ADMIN-con-usuario-normal.png)

### Captura de endpoint ADMIN con usuario administrador

![endpoint ADMIN con usuario administrador](/assets/15-endpoint-ADMIN-con-usuario-administrador.png)

## Explicación breve

### ¿Cuál es la diferencia entre Swagger UI y OpenAPI?

OpenAPI es la especificación que describe una API REST mediante un documento estructurado.

Mientras que Swagger UI es la interfaz web que utiliza OpenAPI para mostrar la documentación de forma visual.

### ¿Por qué Swagger puede ser público pero los endpoints seguir protegidos?

Swagger solo muestra la documentación del API. La protección de endpoints ya depende de la configuración de seguridad de la aplicación. Entonces cualquier usuario podrá acceder a Swagger pero los endpoints seguirán requiriendo de autenticación porque así está configurado

### ¿Cómo se configura Swagger para enviar un JWT en Authorization: Bearer?

Se define un esquema de seguridad de tipo HTTP Bearer en la configuración de OpenAPI. Después el usuario ingresa un JWT válido mediante un botón Authorize. Swagger agrega automáticamente el encabezado.


# 16_Docker_Ubuntu_Server

## Capturas

###  docker ps de Ubuntu Server mostrando ambos contenedores en ejecución.

![Docker ps de ubuntu server](/assets/16-Docker%20-ps-Ubuntu.png)

### curl de /api/actuator/health desde Ubuntu Server.

![Curl de /api/actuator/health desde Ubuntu desktop](/assets/16-actuator-health-ubuntu-desktop.png)

### curl de /api/actuator/health desde la máquina anfitriona.

![Curl de /api/actuator/health desde la máquina anfitriona](/assets/16-acutator-health-maquina-anfitriona.png)

### Explicación de la conexión a PostgreSQL externo o evidencia de fallback utilizado.

Se utilizó PostgreSQL ejecutándose en la máquina anfitriona, mientras que Spring Boot se ejecutaba en un contenedor Docker dentro de Ubuntu Desktop

La comunicación se realizó con la dirección IP de la máquina anfitriona:
jdbc:postgresql://192.168.56.1:5432/devdb
Para evitar almacenar información sensible o código delicado, las credenciales utilizadas fueron suministradas mediante variables de entorno definidas en .env.ubuntu

Conectividad utilizando Cliente PostgreSQL

![Conectividad utilizando cliente PostgreSQL](/assets/16-conectividad-utilizando-cliente-PostgreSQL.png)


### login desde la máquina anfitriona con Bruno o Postman.

![Login desde la máquina anfitriona con Bruno](/assets/16-consumo-login-desde-máquina-anfitriona-con-Bruno.png)
























































