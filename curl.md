## Примеры запросов к api.
#### Получение всех пользователей
`curl -s http://localhost:8090/topjava/rest/admin/users`

#### Получение конкретного пользователя
`curl -s http://localhost:8080/topjava/rest/admin/users/100001`

#### Получение конкретной еды
`curl -s http://localhost:8090/topjava/rest/profile/meals/100003`

#### Получение всей еды
`curl -s http://localhost:8090/topjava/rest/profile/meals`

#### Получение еды по времени
`curl -s "http://localhost:8090/topjava/rest/profile/meals/between?startDate=2020-01-30&startTime=09:00&endDate=2020-01-30&endTime="`

#### Получение несуществующей еды
`curl -s http://localhost:8090/topjava/rest/profile/meals/100500`

#### Удаление еды
`curl -s -X DELETE http://localhost:8090/topjava/rest/profile/meals/100004`

#### Добавление еды
`curl -s -X POST -d '{"dateTime":"2024-10-30T08:00:00.000Z","description":"Завтрак новый","calories":700}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8090/topjava/rest/profile/meals`

#### Обновление еды
`curl -s -X PUT -d '{"dateTime":"2020-01-30T09:00:00.000Z","description":"Завтрак обновленный","calories":200}' -H 'Content-Type: application/json' http://localhost:8090/topjava/rest/profile/meals/100003`