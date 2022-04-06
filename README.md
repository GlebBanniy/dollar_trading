# dollar_trading
REST приложение, позволяющее выставлять заявки на обмен валюты.
Использованные технологии:
- Java 11
- Spring Boot
- MySQL
- H2 Database (for testing)
- Liquibase
- Hibernate ORM
- Maven
- Lombok
- OpenFeign
- Spring Cloud
- JUnit testing
- Mockito
- RabbitMQ
- Docker
- Git
- Validation API

Структура БД:

![image](https://user-images.githubusercontent.com/90478828/161961079-efa08ecb-635f-47b1-ba0c-9495b6b91243.png)

Аккаунт создает заявку на обмен валюты, посылается запрос в https://currencylayer.com/ для уточнения текущих курсов валют, для проверки находится ли указанная сумма в заявке в пределах текущего курса.
