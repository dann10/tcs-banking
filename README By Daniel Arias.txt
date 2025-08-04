README By Daniel Arias

Se desplegó la solución en contenedor con éxito, a continuación se detalla el proceso seguido:

1. Crear un Dockerfile para cada microservicio y un docker-compose.yml en el directorio raíz.
2. Crear jar para los dos microservicios, en el directorio de cada microservicio ejecutar:
   > ./gradlew clean build -x test

3. En el directorio raíz del proyecto ejecutar generar un nuevo constructor local para Docker Buildx ya que permite mayor tolerancia a timeouts y tiene un mejor manejo de recursos:
   > docker buildx create --use --name local-builder

4. En el directorio de cada microservicio vamos a ejecutar siguiente comando, que nos permite crear una imagen de cada microservicio:
   > docker buildx build --tag banking-client-app --no-cache --progress=plain --load .
   > docker buildx build --tag banking-transaction-app --no-cache --progress=plain --load .

5. Se desplegará la solución completa con ambas imágenes creadas en donde se incluirá el despliegue y ejecución de nuestro servidor
   de base de datos MySQL y gestor de colas ActiveMQ, y también verificaremos la salud de los componentes (Directorio raíz):
   > docker-compose up -d
   > docker-compose ps

6. Para que ActiveMQ quede utilizable es importante verificar que tenga estatus activo
   > docker exec -it banking-activemq /bin/bash
   $ bin/activemq status
   $ bin/activemq start