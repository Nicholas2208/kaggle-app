# Сборка Docker образа

1. В файле build.gradle в строке jib.to.image = 'your_docker_username/kaggle-app' замените 'your_docker_username' на свое имя пользователя.

2. Перейдите в командной строке в корневую директорию проекта, где расположен файл build.gradle, и выполните команду **gradle jib**.

# Загрузка на Dockerhub

Выполните следующую команду, чтобы загрузить образ на Dockerhub:

**docker image push docker.io/your_docker_username/kaggle-app**ю

# Залогиниться в Docker из cmd:
docker login --username=your_docker_usernam
ввести пароль.
