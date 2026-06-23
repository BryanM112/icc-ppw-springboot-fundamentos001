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