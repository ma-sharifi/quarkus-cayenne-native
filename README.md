# quarkus-cayenne-native MariaDB and Postgres Project
This project uses Quarkus, the Supersonic Subatomic Java Framework.
Used Quarkus as my framework and MariaDB and Postgres as the database and Apache Cayenne as my ORM.

You can follow the graalvm issue [here](https://github.com/oracle/graal/issues/5716). 

Run databases with:
```
docker compose up --remove-orphans
```
I faced with a lot of different errors, all of them solved by adding dependencies to *resources/reflect-config* file. 

### The following are some errors I've encountered: 
* org.apache.cayenne.di.DIRuntimeException: Error creating instance of class org.apache.cayenne.dba.postgres.PostgresAdapter of type org.apache.cayenne.dba.DbAdapter
* Caused by: org.apache.cayenne.CayenneRuntimeException: [v.4.2.M3 Mar 13 2021 14:15:14] Error creating TypesHandler 'null'.
* Both of them solved by adding following lines to the *resources/reflect-config* file.
```json
{
    "name":"org.apache.cayenne.dba.TypesHandler",
    "allDeclaredFields":true,
    "queryAllPublicMethods":true,
    "queryAllDeclaredConstructors":true
  }
```
Notes: After each change it needs to run following line to get new reflection items:
```shell script
java -agentlib:native-image-agent=config-output-dir=./native/ -jar target/quarkus-app/quarkus-run.jar
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
OR
 mvn clean package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

Then you can run your native executable:
```shell script
./target/quarkus-cayenne-native-1.0.0-SNAPSHOT-runner
```

You can then execute your native executable with: `./target/quarkus-cayenne-native-1.0.0-SNAPSHOT-runner`
If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

### TEST With Httpie
#### Request for test mariaDB:
```bash
http GET :8080/shipments/count-maria
```
Response will be like:
```
3
```
#### Request for test PostgreSQL:
```bash
http :8080/shipments/count-postgres
```
Response will be like:
```
1
```

#### Request for creating shipment:
```bash
http POST :8080/shipments name="Stock#1"
```
#### Successful response:
The header and Location is enough for this API. We can remove the response body. If client needs, It can get url of the just created entity from `Location` in HTTP Header.
```bash
HTTP/1.1 201
Location: http://localhost:8080/shipments/200
```

### Get one shipment
#### Request:
```bash
http GET localhost:8080/shipments/200
```
#### Successful response:
```bash
HTTP/1.1 200 OK
Content-Type: application/json
content-length: 18
```
```json
{
  "name": "Stock#1"
}
```