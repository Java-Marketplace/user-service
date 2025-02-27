### Инструкция по использованию
1. Переименуйте template/user-service на свое название микросервиса (например: user-service) в файлах: application.yaml, .env.example
2. Переименуйте Main и Test класс в название вашего микросервиса (например: UserServiceApplication, UserServiceApplicationTests)
3. Замените проект в sonar-project.properties на ваш (нужно создать в SonarCloud)
4. Добавьте SONAR_TOKEN в Variables в Github репозитории микросервиса (так же из SonarCloud берется) 
5. Удалите данный файл после настройки