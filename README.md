# Authentication-Server
Server application for login and register user.


# Usage
* Register

```
POST: ../api/register
media type: application/x-www-form-urlencoded
key: email, password
```
Response HTTP 201
```json
```
 
* request access token with credential

```
POST: ../api/oauth/token
media type: application/x-www-form-urlencoded
key: email, password
```
Response HTTP 200
```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxLCJpc3N1ZV9kYXRlIjoxNjAyMzk3NTQzMTI4LCJleHBpcmVfZGF0ZSI6MTYwMjQ4Mzk0MzEyOH0=.O8XHwzSNWU7nigh+kcRd5zMZivb1fdF26JcJ33HL7GI=",
    "refresh_token": "Mi5TTmoydVEwOXExUmJJcUJlTkxydmdqOUNWclNPVkdQdlR2UDdLSmVMc3hGZmNCSmI3ZnJJOU5iNjBhMUlod1Zab2ZBandsTVFubHROK1dDS0RheTdXUHNwSk1QN3VxdVZURlJza3djZXpDTVVaemd0LzVjemhSbmdiTnFWNXVia1l2aHBGUWxlcjlDTm9rWjdlNWw3TTh6b0lmQjZTWjQzaWRKTzVBVlRka2s9",
    "expired_date": "1602483943128"
}
```

* Refresh Token
```
POST: ../api/oauth/token
media type: application/x-www-form-urlencoded
key: grant_type(value: refresh_token), refresh_token
```
Response HTTP 200
```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxLCJpc3N1ZV9kYXRlIjoxNjAyMzk3NTQzMTI4LCJleHBpcmVfZGF0ZSI6MTYwMjQ4Mzk0MzEyOH0=.O8XHwzSNWU7nigh+kcRd5zMZivb1fdF26JcJ33HL7GI=",
    "refresh_token": "Mi5TTmoydVEwOXExUmJJcUJlTkxydmdqOUNWclNPVkdQdlR2UDdLSmVMc3hGZmNCSmI3ZnJJOU5iNjBhMUlod1Zab2ZBandsTVFubHROK1dDS0RheTdXUHNwSk1QN3VxdVZURlJza3djZXpDTVVaemd0LzVjemhSbmdiTnFWNXVia1l2aHBGUWxlcjlDTm9rWjdlNWw3TTh6b0lmQjZTWjQzaWRKTzVBVlRka2s9",
    "expired_date": "1602483943128"
}
```

* Reset password
sent reset link to user with email. link contain token
```
POST: ../api/password/reset
media type: application/x-www-form-urlencoded
key: email
```
Response HTTP 200
```json
```

* Update password by credential
```
POST: ../api/password/update
media type: application/x-www-form-urlencoded
key: email, password
```
Response HTTP 200
```json
```

* Update password by token
```
POST: ../api/password/update
media type: application/x-www-form-urlencoded
key: newpassword, token
```
Response HTTP 200
```json
```

* Verify email
```
POST: ../api/user/emailverification/
media type: application/x-www-form-urlencoded
key: token
```
Response HTTP 200
```json
```

* Get User
```
POST: ../api/user/get/
header key: Authorization(value= access token)
```
Response HTTP 200
```json
{
    "user_id": 1,
    "email": "jefryjacky@gmail.com",
    "email_verified": false
}
```

# Enviroment

```
DATABASE_SERVER_URL = your database url
DATABASE_USERNAME= your database username
DATABASE_PASSWORD= your database password

JWT_SECRET= your secret key for generate jwt token

MAIL_HOST= your email hosting
MAIL_USERNAME= your email username
MAIL_PASSWORD= your email password
MAIL_PORT= your email port

EMAIL_VERIFICATION_HOST= link for verify email
FORGOT_PASSWORD_HOST= link for forgot password
```


### License
<pre>
Copyright 2020 Jefry Jacky

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>

