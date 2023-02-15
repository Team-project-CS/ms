[![Maven build](https://github.com/Team-project-CS/ms/actions/workflows/maven.yml/badge.svg)](https://github.com/Team-project-CS/ms/actions/workflows/maven.yml)
[![Docker image](https://github.com/Team-project-CS/ms/actions/workflows/docker-image.yml/badge.svg)](https://github.com/Team-project-CS/ms/actions/workflows/docker-image.yml)
[![Deploy on server](https://github.com/Team-project-CS/ms/actions/workflows/deploy-job.yml/badge.svg)](https://github.com/Team-project-CS/ms/actions/workflows/deploy-job.yml)
# Build docker images

```
mvn clean package dockerfile:build -DskipTests=true
```