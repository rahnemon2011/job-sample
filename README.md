# Job Sample
This is a sample project to create a CRUD with Spring Boot and also reading Excel files and parsing them as a Job asynchronous.

## Technologies: 
* Spring REST
* Spring AOP
* TDD(Tests)
* @Async
* Spring Data JPA
* Flyway

### Run test methods:
```
Use "mvn clean package" to run the tests with HSQL DB.
```

### Run in development envirenment:
To run the project with **spring-boot:run** in development environment, you have to at first install MYSQL Database.
```
Don't forget to change the schema, user and pass in application-dev.properties file.
```

### Run in production environment:
Use java -jar file with the below command:  
**java -jar -Dspring.profiles.active=prod jarName.jar**
 
>  Don't forget to set production DB config in application-prod.properties file too.

## Task lists
You can use these following stack technology to make the application more enterprise:
- [ ] Hibernate Envers
- [ ] Spring Batch
- [ ] JPA Specification
- [ ] Spring Rest Doc or Swagger
- [ ] Spring Security