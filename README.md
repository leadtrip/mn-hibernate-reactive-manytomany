## Micronaut project using
* Project reactor
* Hibernate reactive
* Flyway
* Postgres
* Test resources

Primarily created to test ManyToMany handling with no cascade option set.

`curl --location 'http://localhost:8080/genres/list'`

`curl --location 'http://localhost:8080/books/list'`

````
curl --location 'http://localhost:8080/books' \
--header 'Content-Type: application/json' \
--data '{
"name": "Star wars",
"genres": [
    "513716f3-eda5-437a-a320-37278e7e4a89",
    "d7c37b53-9572-42d0-b58f-087b153d3db4"
    ]
}'
````


````
curl --location --request PUT 'http://localhost:8080/books' \
--header 'Content-Type: application/json' \
--data '{
    "id": "e009800f-4bd7-4c6b-b97a-c18a6049ccec",
    "name": "War of the worlds - extended",
    "genres": [
        "1d6ca063-1fcf-4876-a659-3433211b2e11",
        "513716f3-eda5-437a-a320-37278e7e4a89",
        "d7c37b53-9572-42d0-b58f-087b153d3db4"
        ]
}'
````


````
curl --location --request PATCH 'http://localhost:8080/books' \
--header 'Content-Type: application/json' \
--data '{
    "id": "e009800f-4bd7-4c6b-b97a-c18a6049ccec",
    "genres": [
        "1d6ca063-1fcf-4876-a659-3433211b2e11",
        "d7c37b53-9572-42d0-b58f-087b153d3db4",
        "513716f3-eda5-437a-a320-37278e7e4a89"
        ]
}'
````