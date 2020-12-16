![GitHub language count](https://img.shields.io/github/languages/count/Mickael843/user-api)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/Mickael843/user-api)
![GitHub repo size](https://img.shields.io/github/repo-size/Mickael843/user-api)
![GitHub last commit](https://img.shields.io/github/last-commit/Mickael843/user-api)
![GitHub](https://img.shields.io/github/license/Mickael843/user-api)

## Sistema de gestão de usuários

## Sobre a API

Sistema de gestão de usuários com as principais funções para controle dos usuários de um sistema, possui 
autenticação por JWT e um sistema de criação de relatórios otimizados. Também é possível definir restrições para
os usuários do sistema. Ex: `ROLE_USER`, `ROLE_ADMIN`.

## Funcionalidades

Esta API fornece endpoints HTTP e ferramentas para o seguinte:

#### Não é necessária autenticação

- Registrar um usuário: `POST/user-api/register`
- ‘Login’ do usuário: `POST/user-api/login`
- Recuperar a senha do usuário: `POST/user-api/recovery`
  
#### É necessária autenticação

- Criar um usuário: `POST/user-api/v1/users`
- Atualizar um usuário: `PUT/user-api/v1/users/{externalId}`
- Retornar um usuário com base no ‘id’ externo: `GET/user-api/v1/users/{externalId}`
- Retornar os usuários paginados: `GET/user-api/v1/users/page/{page}`
- Retornar os usuários com base no `FIRSTNAME`: `GET/user-api/v1/users/search/{firstname}`
- Retornar os usuários paginados com base no `FIRSTNAME`: `GET/user-api/v1/users/search/{firstname}/page/{page}`
- Retorna um relatório de usuários do sistema: `GET/user-api/v1/users/report`
- Deleta um usuário com base no ‘id’ externo: `DELETE/user-api/v1/users/{externalId}`
- Deleta um telefone de um usuário: `DELETE/user-api/v1/users/{userExternalId}/phones/{phoneExternalId}`
  
### Detalhes

`POST/user-api/register`

Esse Endpoint é responsável por salvar um usuário padrão no sistema. Não é necessário fazer a autenticação
para utilizar esse Endpoint.

**Body**
```json
{
    "externalId": "842b4e00-d36b-4b8b-85fc-c81791f97e2c",
    "firstname": "Joãozinho",
    "lastname": "Silva",
    "email": "joaozinho@gmail.com",
    "username": "joãozin",
    "password": "joaozinho33"
}
```

**Onde**

`(*) - Campos Obrigatórios.`

- `*externalId` - id externo du usuário (unique universal id).
- `*firstname` — Nome do usuário.
- `lastname` — Sobrenome do usuário.
- `*email` — Email do usuário.
- `*username` — username utilizado para realizar o ‘login’ no sistema.
- `*password` — Senha do usuário.

---------------------------------------------------------------------

`POST/user-api/login`

Esse Endpoint é responsável por efetuar o ‘login’ do usuário no sistema e devolver o token de autenticação.

**Body**
```json
{
    "username": "joãozin",
    "password": "joaozinho33"
}
```

**Onde**

`(*) - Campos Obrigatórios.`

- `*username` — username utilizado para realizar o ‘login’ no sistema.
- `*password` — Senha do usuário.

---------------------------------------------------------------------

`POST/user-api/recovery`

Esse endpoint é responsável pela recuperação da senha do usuário. Um email será enviado 
para o usuário informando a nova senha.

**Body**
```json
{
    "username": "joãozin"
}
```

**Onde**

- `username` — username utilizado para realizar o ‘login’ no sistema.

---------------------------------------------------------------------

`POST/user-api/v1/users`

Esse Endpoint é chamado para criar um usuário no sistema. Os usuários criados já vem com o acesso padrão 
adicionado. `ROLE_USER`

**Body**
```json
{
    "externalId": "842b4e00-d36b-4b8b-85fc-c81791f97e2c",
    "firstname": "Joãozinho",
    "lastname": "Silva",
    "phone": [
      {
        "externalId": "923d97d7-a73a-47ec-aa90-a5fc3b39d625",
        "type": "celular",
        "number": "(38) 9 9943-8876"
      }
    ],
    "email": "joaozinho@gmail.com",
    "username": "joãozin",
    "password": "joaozinho33"
}
```

**Onde**

`(*) - Campos Obrigatórios.`

- `*externalId` - id externo du usuário (unique universal id).
- `*firstname` — Nome do usuário.
- `lastname` — Sobrenome do usuário.
- `phones` - Lista de telefones do usuário.
  - `type` — Tipo do telefone. Ex: `FIXO`, `CELULAR`.
  - `number` — número do telefone.
- `*email` — Email do usuário.
- `*username` — username utilizado para realizar o ‘login’ no sistema.
- `*password` — Senha do usuário.

---------------------------------------------------------------------

`PUT/user-api/v1/users/{externalId}`

Endpoint responsável por atualizar os campos de um usuário. A inclusão dos seguintes dados na requisição
são opcionais: `phones`, `email`, `username`, `password`

**Body**
```json
{
    "externalId": "842b4e00-d36b-4b8b-85fc-c81791f97e2c",
    "firstname": "Joãozinho",
    "lastname": "Silva"
}
```

**Onde**

`(*) - Campos Obrigatórios.`

- `*externalId` - id externo du usuário (unique universal id).
- `*firstname` — Nome do usuário.
- `lastname` — Sobrenome do usuário.

---------------------------------------------------------------------

`GET/user-api/v1/users/{externalId}`

Esse endpoint retornar um usuário com base no ‘id’ externo.

**Retorno**

```json
{
    "externalId": "842b4e00-d36b-4b8b-85fc-c81791f97e2c",
    "firstname": "Joãozinho",
    "lastname": "Silva",
    "phone": [
      {
        "externalId": "923d97d7-a73a-47ec-aa90-a5fc3b39d625",
        "type": "celular",
        "number": "(38) 9 9943-8876"
      }
    ],
    "email": "joaozinho@gmail.com",
    "username": "joãozin",
    "roles": [
      {
        "authority": "ROLE_USER"
      }
    ]
}
```

---------------------------------------------------------------------

`GET/user-api/v1/users/page/{page}`

Esse endpoint retornar os usuários paginados.

**Retorno**

```json
{
    "content": [
        {
            "externalId": "6219d73e-d031-4387-9fd8-957aba3c0784",
            "firstname": "Jonas",
            "lastName": "estranho",
            "username": "teste",
            "email": "teste321@gmail.com",
            "roles": [
                {
                    "authority": "ROLE_USER"
                }
            ]
        },
      {
        "externalId": "842b4e00-d36b-4b8b-85fc-c81791f97e2c",
        "firstname": "Joãozinho",
        "lastname": "Silva",
        "phone": [
          {
            "externalId": "923d97d7-a73a-47ec-aa90-a5fc3b39d625",
            "type": "celular",
            "number": "(38) 9 9943-8876"
          }
        ],
        "email": "joaozinho@gmail.com",
        "username": "joãozin",
        "roles": [
          {
            "authority": "ROLE_USER"
          }
        ]
      }
    ],
    "pageable": "INSTANCE",
    "totalPages": 1,
    "last": true,
    "totalElements": 2,
    "size": 2,
    "number": 0,
    "sort": {
        "unsorted": true,
        "sorted": false,
        "empty": true
    },
    "numberOfElements": 2,
    "first": true,
    "empty": false
}
```

---------------------------------------------------------------------

`GET/user-api/v1/users/search/{firstname}`

Retornar os usuários com base no `FIRSTNAME`.

**Retorno**

```json
{
    "content": [
      {
        "externalId": "842b4e00-d36b-4b8b-85fc-c81791f97e2c",
        "firstname": "Joãozinho",
        "lastname": "Silva",
        "phone": [
          {
            "externalId": "923d97d7-a73a-47ec-aa90-a5fc3b39d625",
            "type": "celular",
            "number": "(38) 9 9943-8876"
          }
        ],
        "email": "joaozinho@gmail.com",
        "username": "joãozin",
        "roles": [
          {
            "authority": "ROLE_USER"
          }
        ]
      }
    ],
    "pageable": "INSTANCE",
    "totalPages": 1,
    "last": true,
    "totalElements": 2,
    "size": 2,
    "number": 0,
    "sort": {
        "unsorted": true,
        "sorted": false,
        "empty": true
    },
    "numberOfElements": 2,
    "first": true,
    "empty": false
}
```

---------------------------------------------------------------------

`GET/user-api/v1/users/search/{firstname}/page/{page}`

Retornar os usuários paginados com base no `FIRSTNAME`.

**Retorno**

```json
{
    "content": [
      {
        "externalId": "842b4e00-d36b-4b8b-85fc-c81791f97e2c",
        "firstname": "Joãozinho",
        "lastname": "Silva",
        "phone": [
          {
            "externalId": "923d97d7-a73a-47ec-aa90-a5fc3b39d625",
            "type": "celular",
            "number": "(38) 9 9943-8876"
          }
        ],
        "email": "joaozinho@gmail.com",
        "username": "joãozin",
        "roles": [
          {
            "authority": "ROLE_USER"
          }
        ]
      }
    ],
    "pageable": "INSTANCE",
    "totalPages": 1,
    "last": true,
    "totalElements": 2,
    "size": 2,
    "number": 0,
    "sort": {
        "unsorted": true,
        "sorted": false,
        "empty": true
    },
    "numberOfElements": 2,
    "first": true,
    "empty": false
}
```

---------------------------------------------------------------------

`GET/user-api/v1/users/report`

Retorna um relatório de usuários do sistema.

**Retorno**

```
data:application/pdf;base64,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ODU3OWQ5MDliNzk0M2I2NDY5MGM2NWM3MWUwN2UwYjgwOTgxOTI4KS9DcmVhdGlvbkRhdGUoRDoyMDIwMTIxNjIwMzMxOC0wMycwMCcpL1Byb2R1Y2VyKGlUZXh0IDIuMS43IGJ5IDFUM1hUKT4+CmVuZG9iagp4cmVmCjAgMTAKMDAwMDAwMDAwMCA2NTUzNSBmIAowMDAwMDAxMDk0IDAwMDAwIG4gCjAwMDAwMDEzNzAgMDAwMDAgbiAKMDAwMDAwMDAxNSAwMDAwMCBuIAowMDAwMDAxNDU4IDAwMDAwIG4gCjAwMDAwMDEzMzUgMDAwMDAgbiAKMDAwMDAwMTUyMSAwMDAwMCBuIAowMDAwMDAxNTc1IDAwMDAwIG4gCjAwMDAwMDE2MDcgMDAwMDAgbiAKMDAwMDAwMTcxMCAwMDAwMCBuIAp0cmFpbGVyCjw8L0luZm8gOSAwIFIvSUQgWzxhZjQyYmJkZWUyMDA4Y2IxZDFlZjE1YzhkYjBlM2Y0OT48N2FiNDJmOTliNWUyMWIyNDk5OGM1OTg0NzdmYjVhYjY+XS9Sb290IDggMCBSL1NpemUgMTA+PgpzdGFydHhyZWYKMTkxOQolJUVPRgo=
```

---------------------------------------------------------------------

`DELETE/user-api/v1/users/{externalId}`

Deleta um usuário com base no ‘id’ externo.

**Where**

- `externalId` - identificador do usuário; uuid (universal unique id)

---------------------------------------------------------------------

`DELETE/user-api/v1/users/{userExternalId}/phones/{phoneExternalId}`

Deleta um telefone de um usuário

**Onde**

- `userExternalId` - identificador do usuário; uuid (universal unique id)
- `phoneExternalId` - identificador do telefone; uuid (universal unique id)

---------------------------------------------------------------------

### Como rodar o projeto:

1 - Levantar o bando de dados **POSTGRES database**:
```
docker-compose up -d
```

2 — Levantar a aplicação **SPRING-BOOT**:
```
mvn spring-boot:run
```

### Tecnologias usadas

Este projeto foi desenvolvido com:

* **Java 11 (Java Development Kit - JDK: 11.0.7)**
* **Spring Boot 2.3.4**
* **Maven**
* **PostgreSQL 12**
* **Flyway 6.4.4**
* **Swagger 3.0.0**
* **Model Mapper 2.3.8**

### Documentação

* Swagger (development environment): [http://localhost:8080/user-api/swagger-ui/index.html](http://localhost:8080/user-api/swagger-ui/index.html)
