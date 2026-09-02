#! /usr/bin/env bash

set -euxo pipefail

# User too young
curl -i -w '\n' -H 'content-type: application/json' -H 'accept: application/json' -d '{ "username": "joe", "name": "Joseph", "address": { "country": "NL", "postalCode": "2011JV", "streetAddress": "Bakenessergracht 79" }, "dateOfBirth": "2010-01-09", "accountType": "SAVINGS" }' 'http://localhost:8080/register'
sleep 1
# User in wrong country
curl -i -w '\n' -H 'content-type: application/json' -H 'accept: application/json' -d '{ "username": "joe", "name": "Joseph", "address": { "country": "GB", "postalCode": "2011JV", "streetAddress": "Bakenessergracht 79" }, "dateOfBirth": "2000-01-09", "accountType": "SAVINGS" }' 'http://localhost:8080/register'
sleep 1
# User valid; complete registration
curl -i -w '\n' -H 'content-type: application/json' -H 'accept: application/json' -d '{ "username": "joe", "name": "Joseph", "address": { "country": "NL", "postalCode": "2011JV", "streetAddress": "Bakenessergracht 79" }, "dateOfBirth": "2000-01-09", "accountType": "SAVINGS" }' 'http://localhost:8080/register'
sleep 1
# Same user was already created
curl -i -w '\n' -H 'content-type: application/json' -H 'accept: application/json' -d '{ "username": "joe", "name": "Joseph", "address": { "country": "NL", "postalCode": "2011JV", "streetAddress": "Bakenessergracht 79" }, "dateOfBirth": "2000-01-09", "accountType": "SAVINGS" }' 'http://localhost:8080/register'
sleep 1
# Log in with incorrect username
curl -i -w '\n' -H 'content-type: application/json' -H 'accept: application/json' 'http://localhost:8080/login?username=alife&password=1234'
sleep 1
# Log in with incorrect password
curl -i -w '\n' -H 'content-type: application/json' -H 'accept: application/json' 'http://localhost:8080/login?username=alice&password=1235'
sleep 1
# Log in successfully
curl -i -w '\n' -H 'content-type: application/json' -H 'accept: application/json' 'http://localhost:8080/login?username=alice&password=1234'
