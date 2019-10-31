**Aprendizaje a tu medida**

Para desplegar la aplicación hay que descargar primero a Tomee de la siguiente ruta:

https://www.apache.org/dyn/closer.cgi/tomee/tomee-8.0.0/apache-tomee-8.0.0-plume.tar.gz

Después de descomprimirlo hay que crear un datasource en tomee. 
Para eso, en el archivo `${tomee.root}/conf/tomee.xml` hay que agregar dentro de la tag `<tomee>` lo siguiente:
~~~~
<Resource id="myDataSource" type="javax.sql.DataSource">
     jdbcDriver = com.mysql.cj.jdbc.Driver
     jdbcUrl = jdbc:mysql://localhost:3306/mydb?serverTimezone=UTC
     JtaManaged = false
     password = <password>
     userName = <username>
 </Resource>
~~~~
 
 Para que surta efecto este cambio es necesario reiniciar el servidor.
 
 **Desplegar a app utilizando Maven**
 
 En el archivo `pom.xml` modifica la propiedad `tomee.path` con la ruta en la que se encuentra instalado tu tomee.
 Después desde la raiz del proyecto corre el siguiente comando:
 
 `mvn clean package antrun:run`
 
