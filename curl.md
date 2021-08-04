MealRestController curl:

GET http://localhost:8080/topjava/rest/meals

GET http://localhost:8080/topjava/rest/meals/100005

DELETE http://localhost:8080/topjava/rest/meals/100005

POST http://localhost:8080/topjava/rest/meals
Content-Type: application/json

{"dateTime":"2020-02-01T18:00:00","description":"Созданный ужин","calories":300}

PUT http://localhost:8080/topjava/rest/meals/100002
Content-Type: application/json

{"id":100002,"dateTime":"2020-01-30T10:02:00","description":"Обновленный завтрак","calories":200}

GET http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-30&startTime=00:00&endTime=23:59

