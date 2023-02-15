[![Java CI with Maven](https://github.com/Team-project-CS/ms/actions/workflows/maven.yml/badge.svg)](https://github.com/Team-project-CS/ms/actions/workflows/maven.yml)
[![Docker Image CI](https://github.com/Team-project-CS/ms/actions/workflows/docker-image.yml/badge.svg)](https://github.com/Team-project-CS/ms/actions/workflows/docker-image.yml)
# Build docker images

```
mvn clean package dockerfile:build -DskipTests=true
```