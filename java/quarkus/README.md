# weather Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/weather-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

## Sample cURL statements

### v1 Endpoints: No Error Monitoring

```
curl -i http://localhost:8080/api/v1/locations

# Success
curl -i http://localhost:8080/api/v1/weather/austin

# Returns an HTTP 404, but no error in Airbrake
curl -i http://localhost:8080/api/v2/weather/boston
```


### v2 Endpoints: Inline Error Monitoring

#### Get Locations

```
curl -i http://localhost:8080/api/v2/locations
```

#### Get Weather for a Location

```
# Success
curl -i http://localhost:8080/api/v2/weather/austin

# Produces a "boston is not a valid location" Airbrake error
curl -i http://localhost:8080/api/v2/weather/boston
```

### v3 Endpoints: Error Monitoring using Logging Middleware

NOTE: Not implemented because Quarkus and log4j won't work. Javabrake's log4javabrake2 middleware is not compatible with Quarkus.

### v4 Endpoints: Global Exception Handlers

#### Get Locations

```
curl -i http://localhost:8080/api/v2/locations
```

#### Get Weather for a Location

```
# Success
curl -i http://localhost:8080/api/v2/weather/austin

# Produces an error of type InvalidLocationException
curl -i http://localhost:8080/api/v2/weather/boston

# Produces an error of type CustomException
curl -i http://localhost:8080/api/v2/weather/bypass
```
