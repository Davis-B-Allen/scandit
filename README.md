# Project 3
By Davis Allen and Ariel Mendoza
 
## Technologies Used
* Spring Boot
* Maven
* Pivotal Cloud Foundry
* Docker
* Feign Client
* Flyway
* Eureka Server
* Pivotal Tracker
* Postgresql
* IntelliJ
 
## Approach
After setting up the initial stories for essential functionality, we decided to begin by setting up a skeleton of the application including a eureka server and api gateway, and microservices for auth and posts, to tackle getting a microservice application up and running on pivotal cloud foundry. Once that was running successfully, we focused on building out the core services.
 
We chose to have all routes that required authentication be routed to an auth service, which also provided user information for other services to consume. 
 
 
## Challenges 
Our first major challenge was figuring out how to properly configure the services for deployment on PCF. Aside from that, our other major hurdle was retrieving data from multiple services based on one request, such as retrieving all of a user’s posts.

## Diagrams
### ERD
![ERD](images/Project3ERD.png)

### Architecture
![Architecture](images/Project3Architecture.png)
 
 
## User Stories
[Pivotal Tracker](https://www.pivotaltracker.com/n/projects/2417875)
 
 
## Links of Interest
 
[eureka server](http://dba-docker-test-eureka.cfapps.io/)
 
[post microservice](http://dba-docker-test-api-gateway.cfapps.io/post/)
 
[auth microservice](http://dba-docker-test-api-gateway.cfapps.io/auth/)
 
[post microservice communicating with auth microservice](http://dba-docker-test-api-gateway.cfapps.io/post/myposts)