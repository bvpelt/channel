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
- channelid int not null
```   
   

## Technical description
- Use springboot
- Use rest/json web services

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

## Docs
- Liquibase https://www.liquibase.org/index.html
- Example liquibase in spring https://github.com/spring-projects/spring-boot/tree/v2.0.4.RELEASE/spring-boot-samples/spring-boot-sample-liquibase
- Example springboot with rest https://hellokoding.com/restful-api-example-with-spring-boot-spring-data-rest-and-mysql/

## Example
Add a channel
``` 
$ curl -i -X POST -H "Content-type:application/json" -d "{\"name\": \"Reizen\"}" http://localhost:8080/channels
HTTP/1.1 201 
Location: http://localhost:8080/channels/1
Content-Type: application/hal+json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 01 Sep 2018 19:01:48 GMT

{
  "name" : "Reizen",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/channels/1"
    },
    "channel" : {
      "href" : "http://localhost:8080/channels/1"
    }
  }
}

$ curl -i -X POST -H "Content-type:application/json" -d "{\"name\": \"Boeken\"}" http://localhost:8080/channels
HTTP/1.1 201 
Location: http://localhost:8080/channels/2
Content-Type: application/hal+json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 01 Sep 2018 19:12:28 GMT

{
  "name" : "Boeken",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/channels/2"
    },
    "channel" : {
      "href" : "http://localhost:8080/channels/2"
    }
  }
}

$ curl -i -X POST -H "Content-type:application/json" -d "{\"message\": \"Texel is a beautifull island\", \"channelid\": 1}" http://localhost:8080/messages
HTTP/1.1 201 
Location: http://localhost:8080/messages/1
Content-Type: application/hal+json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 01 Sep 2018 19:05:45 GMT

{
  "message" : "Texel is a beautifull island",
  "channelid" : 1,
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/messages/1"
    },
    "message" : {
      "href" : "http://localhost:8080/messages/1"
    }
  }
}

$ curl -i -X GET http://localhost:8080/channels/1
HTTP/1.1 200 
Content-Type: application/hal+json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 01 Sep 2018 19:13:49 GMT

{
  "name" : "Reizen",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/channels/1"
    },
    "channel" : {
      "href" : "http://localhost:8080/channels/1"
    }
  }
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
      "channelid" : 1,
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