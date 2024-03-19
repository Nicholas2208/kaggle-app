Собрать Docker образ:
В build.gradle в строке jib.to.image = 'your_docker_username/kaggle-app' вставьте свое имя пользователя.
Перейдите в командной строке в корневую директорию проекта где раасположен файл build.gradle и выполните команду gradle jib.
Запушить на Dockerhub:
docker image push docker.io/your_docker_username/kaggle-app
