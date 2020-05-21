# ASIO LDP

Plataforma Linked Data para el proyecto ASIO. 

La plataforma está basada en el proyecto [Trellis LDP](https://www.trellisldp.org/). Se trata de un servidor LDP modular que hace incapié tanto en la escalabilidad como en estar conforme a los estándares web.

## OnBoarding

Para iniciar el entorno de desarrollo se necesita cumplir los siguientes requisitos:

* OpenJDK 11
* Eclipse JEE 2019-09 con plugins:
  * Lombok

## Arquitectura

Para la arquitectura de la aplicación se toma como base el proyecto [trellis-ldp](https://github.com/trellis-ldp/trellis). Este al ser un proyecto modular, permite intercambiar de forma muy sencilla alguno de sus módulos para extenderlos o añadir nuevas funcionalidades. Más información sobre la arquitectura de Trellis LDP en https://github.com/trellis-ldp/trellis/wiki/Trellis-Architecture

Sobre la base de trellis se han añadido los siguientes módulos:

- asio-ldp-dropwizard: módulo que permite la composición de un nuevo artefacto ejecutable
- asio-ldp-persistence: módulo de persistencia para modificar la capa de almacenamiento

### Capa de persistencia - asio-ldp-persistence

Se trata de un módulo cuyo cometido es el de ser la nueva capa de persistencia. Trellis está diseñado para que sea posible generar un nuevo adaptador de la capa de persisencia. Para ello dispone de 2 interfaces que es preciso implementar:

- `org.trellisldp.api.ResourceService`
- `org.trellisldp.api.AuditService`

Los métodos que es necesario implementar son los siguiente:

```java
CompletableFuture<Resource> get(IRI identifier);

CompletableFuture<Void> create(IRI identifier, IRI interactionModel, Dataset dataset, IRI container, Binary binary);

CompletableFuture<Void> replace(IRI identifier, IRI interactionModel, Dataset dataset, IRI container, Binary binary);

CompletableFuture<Void> delete(IRI identifier, IRI container);
```

### Proyecto generador del artefacto - asio-ldp-dropwizard

Trellis está compuesto de varios módulos que se ensamblan en una única aplicación. Para hacer este ensamblado se ha generado un proyecto que contiene como dependencia todas aquellas librerías que contiene trellis además de las nuevas que se añadan:

- asio-ldp-persistence: sustituye a trellis-triplestore
- trellis-constraint-rules
- trellis-io-jena
- trellis-api
- trellis-http
- trellis-app
- trellis-cache
- trellis-dropwizard
- trellis-vocabulary
- trellis-file
- trellis-namespaces
- trellis-audit
- trellis-event-jackson
- trellis-webac
- trellis-jms
- trellis-rdfa
- trellis-kafka
- trellis-auth-oauth
- trellis-auth-basic

Para generar el empaquetado con todas estas librerías se utiliza el [framework dropwizard](https://www.dropwizard.io/), el cual permite además de realizar el empaquetado, gestionar la [configuración de la aplicación](#configuracion), mediante un fichero yml. 

Una aplicación Dropwizard dispone de su propia subclase de `io.dropwizard.Configuration` la cual dispone de los parámetros específicos del entorno.  Trellis dispone de la implementación base `org.trellisldp.dropwizard.config.TrellisConfiguration` la cual será preciso extender.  

Será preciso también crear una clase que extendienda la clase es.um.asio.ldp.app.TrellisApplication, la cual a su vez extiende `io.dropwizard.Application<T>`, la cual tiene estará parametrizada con la clase de configuración indicada anteriormente. Esta será la clase que contenta el método `main` de la aplicación y por tanto la que hay que ejecutar para arrancar el servicio.

También es necesario implementar la clase `org.trellisldp.http.core.ServiceBundler`, para definir todos los servicios que forman la aplicación Trellis. Trellis proveé una implementación `org.trellisldp.app.BaseServiceBundler` la cual se puede utilizar como base. 

## Construcción

Para construir la aplicación se utiliza maven, por lo que será necesario ejecutar el siguiente comando:

```
mvn clean package
```

Como resultado, se obtendrán los siguientes artefactos ubicados en `asio-ldp-dropwizard/target`:

- asio-ldp-dropwizard-${project.version}.jar
- asio-ldp-dropwizard-${project.version}-shaded.jar

El primero de ellos se trata del JAR básico con las clases y recursos del proyecto. El que dispone del sufijo `-shaded` se trata de un 'uber-jar', el cual contiene el empaquetado del módulo actual así como todas sus dependencias. También incluye en el MANIFEST.MF la clase que contiene el método main. Para conseguir este empaquetado se utiliza el plugin de maven [maven-shade-plugin](https://maven.apache.org/plugins/maven-shade-plugin/).

## Arranque

Para el arranque de la aplicación, al generar un Uber JAR, se arranca de la forma habitual.

```
java -jar asio-ldp-dropwizard-${project.version}-shaded.jar
```

## Configuración

Trellis necesita un fichero de configuración en formato YML, cuya ubicación puede ser obtenida por el siguiente orden:

- Parámetro de entrada
- Variable de entorno ASIO_LDP_CONFIG_FILE: ruta del fichero de configuración (opción recomendada)
- Fichero dentro del classpath con nombre `config-dev.yml`. Este fichero se ha generado para entorno de desarrollo.

En cuanto a la composición del fichero de configuración, se puede tomar como base la [documentación de trellis](https://github.com/trellis-ldp/trellis/wiki/Module-Configuration).

Más información:

- https://github.com/trellis-ldp/trellis/wiki/App-Configuration-Guide

## Configuración módulos

Muchos de los módulos de los que dispone Trellis disponen de sus propias variables de configuración. Estas variables pueden ser definidas a través de variables de entorno o de sistema (con el argumento -D de Java). Esta gestión se realiza mediante Apache Tamaya, https://tamaya.incubator.apache.org/documentation/api.html

## Configuración WebAC

WebAC es el mecanismo que se utiliza para la realizar la autorización. Para ello utiliza ficheros en formato TTL que definen los permisos. Por defecto utiliza el fichero ubicado en `org/trellisldp/webac/defaultAcl.ttl`. Este valor puede modificarse utilizando la variable `trellis.webac.default-acl-location`, la cual puede ser definida cómo se indica en el apartado anterior (variable de sistema o de entorno).

Más información: https://www.w3.org/wiki/WebAccessControl
