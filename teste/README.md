# webhook-subscriber
Standalone responsible to handle webhook events subscriptions


Information needed to properly interact with the code

APPLICATION

It's a spring-boot application managed by Maven, so you can run it in one of this ways:
 - directly from you IDE
 - run from pom.xml file location the command: mvn spring-boot:run
 
OpenAPI DOCs 
file is on this pathhttps: {your-local-project-directory}/webhook-subscriber/tree/master/src/main/resources/bosh-open-api

POSTMAN COLLECTION
To test the application you could import the Postman Collection from webhook-subscriber/src/main/resources/Bosh.postman_collection.json


basePath: localhost:8080
controllers:
 - subscriptions: ("/subscriptions")
 - events: ("/events")
 
DATABASE
Even dealing with a reactive application I decided to use Postgres as database. To solve the blocking behavior of SQL Database (as Postgres is), I used R2DBC as a driver to manage the connection pool and permit an assynchronous interaction. But, it'd be better to use a properly No-SQL database like MongoDB to deal with that kind of situation in a production environment. 



Considering a REST Hook pattern with this 4 basic mechanisms (see https://www.olioapps.com/blog/rest-hooks)

1 - Mechanism to store subscriptions
2 - Mechanism to modify subscriptions via API
3 - List of event types & implementation of events
4 - Mechanism to send hooks

I prioritize to implement a service that could manage in a proper way the subscriptions and its events associated. So you could see the first 3 points implemented. 
About the 4th point I began to develop it, but need a final point to deal with the publishers of the generateds events. 

In a global explanation you can execute a CRUD with subscriptions and events and you could trigger a event producer. At this last point I couldn't finalize the integration with my consumer due to lack of time from my side. 


Business consideration:
You could add as much subscription you will, but only if there is an event to you associate with by eventId as you could see in OpenAPI documentation. 
Your eventId should not be empty. 
If you delete an event, the subscriptions associated will be deleted too, since I'm considering that just make sense a subscription exist if there is an event. 
Updating an event is possible only if you pass a new eventType that aren't associated with any eventId. 


From my side I still need to improve this app, and I'll do it independt of the result of the process (but I'd love to work with Bosh Thermotechnology IT team). Some point to improve are:
 - get the events consumed and find a properly callbackURL to send event


As mentioned before, in a production environment it would be better to you a database that support non-blocking behavior of an reactive application (if we consider a top-bottom reactiveness). In tha way the driver R2DBC is still evolving and feel companies put that on a controlled production environment due to pool connection management issues. But, in a poc like that of the test it works very well. 

If you have any feedback or reflections, please share them with us. For
example, what would you have done differently? 

I don't feel confortable to say if I had done something different because there are many sitations that lead us to make an decision. And I'm sure that your team make the best choice available. From my side some points that I had considered to make a decison are:
  - the application will be cloud based or on premise?
    - if cloud (e.g AWS): could we use SQS or event Kafka to deal with event drive archtecture?
      its depends on the budget, business target and technical capacity available
    - if we have a on premise structure how could we scale the solution?
      maybe using an container orchestration (e.g. by means of Kubernates)
