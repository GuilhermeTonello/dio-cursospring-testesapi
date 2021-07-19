# Digital Innovation One: Desenvolvimento de testes unitários para validar uma API REST de gerenciamento de estoques de cervejas

Curso realizado a partir do Bootcamp GFT START #2 Java da Digital Innovation One (DIO).

## Tópicos abordados no curso:
- Testes unitários
- JUnit
- Hamcrest
- Mockito
- TDD

### Executando e acessando o projeto:

##### Para executar digite no terminal:
mvnw spring-boot:run

##### Para acessar utilize a URL:
localhost:8080/api/v1/cerveja

## Endpoints da API:
|Objetivo                           | Método HTTP | Endpoint               |
|:---------------------------------:|:-----------:|:----------------------:|
|Consultar todas as cervejas        | GET         | /api/v1/cerveja        |
|Consultar uma cerveja              | GET         | /api/v1/cerveja/{nome} |
|Salvar uma cerveja                 | POST        | /api/v1/cerveja        |
|Atualizar a quantidade de cervejas | PATCH       | /api/v1/cerveja/{id}   |
|Deletar uma cerveja                | DELETE      | /api/v1/cerveja/{id}   |

Observações: 
 - Para o método POST deve-se passar no corpo da requisição os valores dos campos da cerveja
 - Para o método PATCH deve-se passar no copor da requisição a quantidade

## Requisitos para o projeto:
Java 11 ou versões superiores
