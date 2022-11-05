# How to use:
1. run  ```docker/docker-compose-rabbitmq.yml```
2. run application 

## Functional: 
* POST ```localhost:8090/api/rabbit?queueName=X``` to create queue X 
* PUT  ```localhost:8090/api/rabbit?queueName=X&message=Y``` to send message Y to queue X
* GET  ```localhost:8090/api/rabbit?queueName=X```  to read from queue X
