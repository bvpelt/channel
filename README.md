# Channel


## Functional description
- Add message to a predefined channel
- Get specific message from a channel
- Get all messages from a channel

### Datamodel

channel (1) ----< (0..n) message

``` 
channel content
- id int autoincrement, primary key, not null
- name varchar[24] not null

message content
- id int autoincrement, primary key, not null
- message varchar[1024] not null
- channelId int not null
```   
   

## Technical description
- Use springboot
- Use rest/json web services
- Use [caching](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-caching.html) and [configuration of caching](https://docs.spring.io/spring/docs/5.0.9.RELEASE/spring-framework-reference/integration.html#cache)

## Development
- Test using h2, console during test available at http://localhost:8080/h2-console 
- Run using postgresql

## Project creation/setup
- create pom from https://start.spring.io
- use packages h2, postgresql, actuator, jpa, liquibase, rest repositories

## Create database
- start database
```
$ sudo systemctl start postgresql 
```
- create database with name channels
``` 
$ createdb channels
```
- grant access to user
```
$ createuser -P testuser
$ psql channels
Pager usage is off.
psql (9.5.14)
Type "help" for help.

channels=# grant all privileges on all tables in schema public to testuser;
GRANT
channels=# ALTER USER "testuser" WITH PASSWORD '12345'; 
```

- Update sequence
``` 
$ psql channels
channels=# ALTER SEQUENCE message_messageid_seq RESTART WITH 35;
```
## Docs
- Liquibase https://www.liquibase.org/index.html
- Example liquibase in spring https://github.com/spring-projects/spring-boot/tree/v2.0.4.RELEASE/spring-boot-samples/spring-boot-sample-liquibase
- Example springboot with rest https://hellokoding.com/restful-api-example-with-spring-boot-spring-data-rest-and-mysql/
- Example CORS https://grokonez.com/spring-framework/spring-boot/spring-cors-example-crossorigin-spring-boot
- Example springboot and angular https://mydevgeek.com/angular-4-crud-application-with-spring-boot-rest-service-part-1/
- Date time https://stackoverflow.com/questions/23718383/jpa-support-for-java-8-new-date-and-time-api
- Example changelog https://github.com/liquibase/liquibase/blob/master/liquibase-integration-tests/src/test/resources/changelogs/yaml/common.tests.changelog.yaml


## Example
Add a channel
``` 
curl -v -i -X POST -H "Content-typeapplication/json" -d "{\"name\": \"Filmen\"}" http://localhost:8080/channels
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /channels HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.47.0
> Accept: */*
> Content-type:application/json
> Content-Length: 18
> 
* upload completely sent off: 18 out of 18 bytes
< HTTP/1.1 200 
HTTP/1.1 200 
< Content-Type: application/hal+json;charset=UTF-8
Content-Type: application/hal+json;charset=UTF-8
< Transfer-Encoding: chunked
Transfer-Encoding: chunked
< Date: Fri, 21 Sep 2018 17:31:36 GMT
Date: Fri, 21 Sep 2018 17:31:36 GMT

< 
{
  "name" : "Filmen",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/channels/15"
    }
  }
* Connection #0 to host localhost left intact
}

$ curl -i -X GET http://localhost:8080/channels
HTTP/1.1 200 
Content-Type: application/hal+json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 01 Sep 2018 19:12:57 GMT

{
  "_embedded" : {
    "channels" : [ {
      "name" : "Reizen",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/channels/1"
        },
        "channel" : {
          "href" : "http://localhost:8080/channels/1"
        }
      }
    }, {
      "name" : "Boeken",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/channels/2"
        },
        "channel" : {
          "href" : "http://localhost:8080/channels/2"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/channels{?page,size,sort}",
      "templated" : true
    },
    "profile" : {
      "href" : "http://localhost:8080/profile/channels"
    }
  },
  "page" : {
    "size" : 20,
    "totalElements" : 2,
    "totalPages" : 1,
    "number" : 0
  }
}

$ curl -i -X GET http://localhost:8080/messages
HTTP/1.1 200 
Content-Type: application/hal+json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 01 Sep 2018 19:08:05 GMT

{
  "_embedded" : {
    "messages" : [ {
      "message" : "Texel is a beautifull island",
      "channelId" : 1,
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/messages/1"
        },
        "message" : {
          "href" : "http://localhost:8080/messages/1"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/messages{?page,size,sort}",
      "templated" : true
    },
    "profile" : {
      "href" : "http://localhost:8080/profile/messages"
    }
  },
  "page" : {
    "size" : 20,
    "totalElements" : 1,
    "totalPages" : 1,
    "number" : 0
  }
}
```