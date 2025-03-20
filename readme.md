# ABS

Service responsible to do transaction between users. There are two different implementation of approach to maintain
transfer transaction.

1. Standalone approach with optimistic lock mechanism
2. Distributed approach with SAGA pattern

### High level architecture of distributed approach

```mermaid
graph TD
;
    Kafka --> abs-backend1;
    Kafka --> abs-backend2;
    abs-backend1 --> Kafka;
    abs-backend2 --> Kafka;
    abs-backend1 --> abs-database;
    abs-backend2 --> abs-database;
```

## Steps to run:

+ Build

```shell 
mvn clean package
```

+ Build abs-backend docker image

```shell 
cd abs-backend 
docker build -t 'abs-backend' . 
```

+ Run docker compose

```shell
cd ..
cd docker
docker compose up
```