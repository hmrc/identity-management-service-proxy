# identity-management-service-proxy

Proxy for the Identity Management Service (IDMS). Serves as the IDMS in a 
secondary Integration Hub environment. Forwards calls to the real IDMS via HODS Proxy
and Scrubbing Centre.

In environments without a real IDMS then this proxy forwards to the IDMS stub,
identity-management-service-stubs.

## Requirements

This service is written in [Scala](http://www.scala-lang.org/) and [Play](http://playframework.com/), so needs at least a [JRE] to run.

## Dependencies
Beyond the typical HMRC Digital platform dependencies this service relies on:
- IDMS

The full set of dependencies can be started using Service Manager and the group API_HUB_ALL.

You can view service dependencies using the Tax Catalogue's Service Relationships
section here:
https://catalogue.tax.service.gov.uk/service/identity-management-service-proxy

### IDMS
All requests to this service starting with `/identity-management-service-proxy/identity` 
are forwarded to IDMS. The incoming `Authorization` header is passed on. This
service does not add authorisation.

The IDMS to use is configured in `application.conf` in these settings: 
- `microservice.services.idms`

## Using the service

### Running the application

To run the application use `sbt run` to start the service. All local dependencies should be running first.

Once everything is up and running you can access the application at

```
http://localhost:9000/identity-management-service-proxy
```

### Authentication
This service does not authenticate incoming requests.

## Building the service
This service can be built on the command line using sbt.
```
sbt compile
```

### Unit tests
This microservice has many unit tests that can be run from the command line:
```
sbt test
```

### Integration tests
This microservice has some integration tests that can be run from the command line:
```
sbt it:test
```

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
