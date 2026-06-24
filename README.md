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