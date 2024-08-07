1.Name of project: user-manager-rest-gateway-api

2.Launch of project:
  2.1 Run Postgresql: user-manager-rest-gateway-api\user-server> docker-compose up
  2.2 Run eureka-server
  2.3 Run user-server
  2.4 Run gateway-server

3.Swagger documentation: 
  3.1 http://localhost:8080/user-server/v2/api-docs or http://localhost:8081/swagger-ui/index.html
  3.2 http://localhost:8080/user-server/swagger-ui/index.html or http://localhost:8081/v2/api-docs

4.Ports of the project:
    eureka-server: http://localhost:8761
    user-server: http://localhost:8081
    gateway-server: http://localhost:8080

5.Rest controllers:
UserController:
    registerUser(POST): http://localhost:8080/user-server/api/reg + body;
    authenticationUser(POST): http://localhost:8080/user-server/api/auth + body
    getAll(GET): http://localhost:8080/user-server/api/users
    getById(GET): http://localhost:8080/user-server/api/users/{id};
    update(PUT): http://localhost:8080/user-server/api/users/{id} + body;
    delete(DELETE): http://localhost:8080/user-server/api/users/{id}



