In order to access and test the project please follow these steps:
1.clone the repo into your local env
2.move to the master branch
3.run the mvn clean install comamnd in terminal

You can find the database at localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
user name: test
password: test

We have 2 different users:
1. username:user
password: user 
role:USER
2. username: admin
password: admin
role:ADMIN

so u need to basic auth before sending any requests .
please check http://localhost:8080/swagger-ui/index.html#/ - in order to see implemented APIs
The admin is the only one who can add products.
Good luck !
