# Remessa API

API para remessas internacionais com conversão de BRL para USD.

## Sobre o projeto

A API permite:

* Criar usuários PF e PJ.
* Criar uma carteira para cada usuário.
* Consultar a carteira.
* Realizar remessas de BRL para USD.
* Consultar a cotação do dólar.
* Atualizar a cotação através da API do Banco Central.
* Aplicar limite diário de transferência:

    * PF: R$ 10.000,00
    * PJ: R$ 50.000,00

## Arquitetura

Projeto organizado em camadas:

```text
Controller -> Service -> Repository -> MySQL
```

Integrações externas:

```text
ExchangeRateService: Redis e Banco Central do Brasil
```

Os Controllers recebem as requisições, os Services possuem as regras de negócio e os Repositories acessam o banco.

## Tecnologias

* Java
* Spring Boot
* Spring Data JPA
* Spring Web
* Spring Validation
* MySQL
* Redis
* Flyway
* Docker / Docker Compose
* Swagger / OpenAPI
* JUnit 5
* Mockito
* Maven

## Pré-requisitos

Para executar localmente:

* Java
* Maven
* Docker
* Docker Compose

## Como executar

Clone o projeto:

```bash
git clone <URL_DO_REPOSITORIO>
cd remessa-api
```

Execute:

```bash
./mvnw spring-boot:run
```

Ou utilize o Docker Compose:

```bash
docker compose up --build
```

## Docker Compose

O Docker Compose sobe os serviços necessários:

```text
mysql
redis
```

Para parar:

```bash
docker compose down
```

## Banco de dados / Flyway

O banco utilizado é o MySQL.

As alterações do banco são controladas pelo Flyway.

As migrations ficam em:

```text
src/main/resources/db/migration
```

Ao iniciar a aplicação, as migrations pendentes são executadas automaticamente.

## Redis

O Redis é utilizado como cache da cotação do dólar.

A aplicação:

1. Consulta o Redis.
2. Caso exista uma cotação, utiliza o valor armazenado.
3. Caso não exista, consulta o MySQL.
4. Armazena a cotação no Redis.

A cotação também é atualizada pelo scheduler através da API do Banco Central.

## Swagger

Com a aplicação em execução:

```text
http://localhost:8080/swagger-ui.html
```

Documentação OpenAPI:

```text
http://localhost:8080/v3/api-docs
```

## Endpoints

### Usuários

```http
POST /users
```

Cria um usuário e sua carteira.

```http
GET /users
```
Retorna a lista de usuários cadastrados.

### Carteiras

```http
GET /wallets/{userId}
```

Consulta a carteira de um usuário.

```http
PUT /wallets/{userId}
```

Atualiza os valores da carteira.

### Transferências

```http
POST /transfers
```

Realiza uma remessa internacional de BRL para USD.

## Exemplos de requisição

### Criar usuário

```http
POST /users
Content-Type: application/json
```

```json
{
  "fullName": "João Silva",
  "email": "joao@email.com",
  "password": "123456",
  "personType": "PF",
  "cpfCnpj": "12345678900"
}
```

### Criar transferência

```http
POST /transfers
Content-Type: application/json
```

```json
{
  "senderId": 1,
  "receiverId": 2,
  "amountBrl": 100.00
}
```

A cotação utilizada na transferência é obtida pelo sistema e o valor correspondente em USD é calculado automaticamente.

### IMPORTANTE
```
Arquivo 'Remessa.postman_collection.json' disponível para importação no Postman e manipulação dos endpoints.
```