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