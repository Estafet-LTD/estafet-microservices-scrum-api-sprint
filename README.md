# Estafet Microservices Scrum Sprint API
Microservices api for managing sprints and their lifecycle for the scrum demo application.
## What is this?
This application is a microservice provides an API to create new sprints based on a supplied number of days. The API includes methods for retrieving information for each working day of the sprint. Sprints are run contiguously for a specific project. A sprint can only exist for one project.

Each microservice has it's own git repository, but there is a master git repository that contains links to all of the repositories [here](https://github.com/Estafet-LTD/estafet-microservices-scrum).
## Getting Started
You can find a detailed explanation of how to install this (and other microservices) [here](https://github.com/Estafet-LTD/estafet-microservices-scrum#getting-started).
## API Interface

### Messaging

|Topic   |Event    |Message Type |
|--------|---------|-------------|
|new.sprint.topic|When a new sprint is created, it is published to this topic|Sprint JSON Object|
|update.sprint.topic|When an existing sprint has been modified, it is published to this topic|Sprint JSON Object|

### Sprint JSON object

```json
{
    "id": 1,
    "startDate": "2017-10-01 00:00:00",
    "endDate": "2017-10-06 00:00:00",
    "number": 1,
    "status": "Completed",
    "projectId": 1,
    "noDays": 5
}
```

### Restful Operations

To retrieve an example the project burndown object (useful for testing to see the microservice is online).

```
GET http://sprint-api/sprint
```

To retrieve a sprint that has an id = 1

```
GET http://sprint-api/sprint/1
```

To start a new sprint of 5 days for project 1. If there are no existing sprints, the sprint will start on the current day, otherwise it will start on the completion of the last sprint.

```
POST http://sprint-api/sprint/start-sprint
{
    "projectId": 1,
    "noDays": 5
}
```

To retrieve all of the sprints for a project that has an id = 1

```
GET http://sprint-api/project/1/sprints
```

To retrieve the working days for a sprint

```
GET http://sprint-api/sprint/1/days
```

To retrieve the current working day for a sprint

```
GET http://sprint-api/sprint/1/day
```

## Environment Variables
```
JBOSS_A_MQ_BROKER_URL=tcp://localhost:61616
JBOSS_A_MQ_BROKER_USER=estafet
JBOSS_A_MQ_BROKER_PASSWORD=estafet

SPRINT_API_JDBC_URL=jdbc:postgresql://localhost:5432/sprint-api
SPRINT_API_DB_USER=postgres
SPRINT_API_DB_PASSWORD=welcome1
```

## Domain Model States
A sprint has three states. It can only progress from each state via the specific actions or events illustrated.

![alt tag](https://github.com/Estafet-LTD/estafet-microservices-scrum-api-sprint/blob/master/SprintStateModel.png)

