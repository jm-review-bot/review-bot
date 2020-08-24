Для работы проекта необходимо:

1. создать в папке src/resources/vkconfig.properties со следующим содержанием (скобки не нужны):
group_id=(id вашего сообщества)
access_token=(токен доступа для вашего сообщества (получить в настройках сообщества в ВК))

2. Параметрах VM указать (Edit configuration):
-Dspring.profiles.general=local-app (для запуска на ПК разработчика/тестировщика)
или
-Dspring.profiles.general=devops-app (для запуска на рабочем сервере)

Также возможен запуск приложения (jar-файла) из консоли:
1. Для запуска на ПК разработчика/тестировщика:
java "-Dspring.profiles.general=local-app" -jar <Путь_до_исполняемого_jar-файла> 
2. Для запуска на рабочем сервере:
java "-Dspring.profiles.general=devops-app" -jar <Путь_до_исполняемого_jar-файла>