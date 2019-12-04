# Project 3
By Davis Allen and Ariel Mendoza

## Technologies Used

### Core Technologies
* Spring Boot
* Maven
* Docker
* Feign Client
* Flyway
* Eureka Server
* Postgresql

### Agile Development
* Pivotal Tracker

### Integration and Unit Testing
* Jacoco
* JUnit
* Jenkins
* AWS EC2

### Logging and Monitoring
* Elasticsearch
* Logstash
* Kibana

### Documentation
* Javadoc
* Springfox Swagger

### Email Notifications
* RabbitMQ
* SendGrid

### IDE
* IntelliJ


## Approach
*After setting up the initial stories for essential functionality,we decided to begin by setting up a skeleton of the application including a eureka server and api gateway, and microservices for auth and posts, to tackle getting a microservice application up and running on pivotal cloud foundry. Once that was running successfully, we focused on building out the core services.*

*Before Zuul routes any request on to the microservices, it first calls a dedicated Auth Service (this is implemented with Zuul's Pre Filters).*

*We tried to keep everything as loosely coupled as possible. We have separate databases for each service. Posts and Comments services are the only two services which talk to each other (excluding zuul and eureka).*

*We used username as the foreign key wherever possible, allowing us to avoid needing to call the user database for user info.*

<!-- Project 4 Approach -->
We began by integrating the ELK stack for monitoring our application, as well as documentation using Swagger and Javadoc. We then added functionality for users to receive email notifications when someone comments on their post using RabbitMQ.

Early on we also worked on setting up continuous integration and deployment via Jenkins to avoid having to manually rebuild our services as our code was updated. Once that was set up, we finished with integration and unit testing, as well as handling exceptions gracefully and validating user input.




## Challenges and Wins
*Our first major challenge was figuring out how to properly configure the services for deployment on PCF. Aside from that, our other major hurdle was retrieving data from multiple services based on one request, such as retrieving all of a user’s posts.*

*We also had issues with serializing objects out to JSON and deserializing objects we were getting back in JSON.*

<!-- Project 4 Challenges & Wins -->
One of our challenges was getting tests to run properly using Cobertura. It was causing issues with our Jenkins builds, so after several attempts we ended up switching to Jacoco which was able to run our tests successfully.

We also faced challenges integrating Swagger documentation. We initially didn't have the correct routes displaying, and the UI was exposing all of the internal routes. The UI was also showing additional parameters for our service models, which we were able to fix by customizing how Swagger read our APIs using the APIImplicitParams annotation.

Another issue involved Zuul routing, where it was stripping prefixes which then threw 404 errors. Updating the YML file to prevent Zuul from automatically stripping the prefix resolved the issue.

One thing we were proud of was being able to keep our routes the same for the frontend after integrating the various additional services. Resolving the issue with how Zuul was routing our requests saved us from having to alter any routes, which saved us some troubleshooting later on.



## Diagrams
### ERD
![ERD](images/Project3ERD.png)

### Architecture
![Architecture](Project-3-Architecture.png)


## User Stories
[Pivotal Tracker](https://www.pivotaltracker.com/n/projects/2417875)
