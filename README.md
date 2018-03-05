API Boletos Fácil - Doc
=======

Aqui você encontrará informações relevantes a respeito da API para gerenciamento de boletos.

Esta API tem o objetivo de fornecer uma arquitetura livre, que possibilite a criação de Sistemas ou Aplicativos para o gerenciamento de Boletos. A ideia é possibilitar o usuário registrar categorias de boletos e boletos para então, ter controle sobre os mesmos.

Funcionamento
---------------
Basicamente, a API terá a seguinte estrutura:

 * Users -> São usuários da aplicação;
 * Markings -> Marcações de Boletos, isto é, cada usuário poderá cadastrar suas marcações, para então categorizar os boletos como desejar;
 * Tickets -> São os Boletos que o usuário poderá registrar, vinculado a uma marcação;

HTTP Status Code
----------------
|    Code    |             Description                                                     |
|------------|-----------------------------------------------------------------------------|
|    200     | Solicitação realizada com sucesso.                                          |
|    406     | Erro de validação, verifique se os dados a serem enviados, estão corretos.  |
|    500     | Erro interno do servidor.                                                   |
|    511     | Autenticação é necessária para completar a requisição.                      |


Consumindo a API
---------------
** A URL da API (endpoint) é: https://apiboletos.herokuapp.com **

### Users (Usuários)

|  Rosource  |      Request       |           Description                                                  |  Header  |
|------------|--------------------|------------------------------------------------------------------------|    -     |
|    GET     |   /api/users       | Retorna a lista de usuários registrado no sistema.                     |    -     |
|    POST    |   /api/users       | Registra um novo usuário no banco de dados.                            |    -     |
|    POST    |   /api/users/login | Realiza autenticação do usuário, de acordo com os parâmetros enviados. |  token   |
|    GET     |   /api/users/auth  | Verifica se o usuário está autenticado, através do token.              |  token   |

* /api/users (POST)
``` json
{
    "username": "fulano",
    "password": 1234,
    "email": "fulano@gmail.com"
}
```
* /api/users/login (POST)
``` json
{
    "username": "fulano",
    "password": 1234
}
```

### Markings (Marcações)

|  Rosource  |      Request                |           Description                                            |  Header  |
|------------|-----------------------------|------------------------------------------------------------------|    -     |
|    GET     |/api/markings/situations     | Retorna a lista de situações que uma marcação poderá ter.        |    -     |
|    GET     |/api/markings                | Retorna as marcações.                                            |  token   |
|    GET     |/api/markings/{id}           | Retorna uma marcação a partir do seu id.                         |  token   |
|    POST    |/api/markings                | Registra uma nova marcação para o usuário.                       |  token   |
|    PUT     |/api/markings/{id}           | Atualiza informações da marcação.                                |  token   |
|    PUT     |/api/markings/{id}/{situacao}| Altera a situação da marcação 0->Inactive ou 1->Active.          |  token   |
|    DELETE  |/api/markings/{id}           | Exclui uma marcação a partir do seu id.                          |  token   |

* /api/markings (POST)
``` json
{
  "name": "Marcação Exemplo",
  "situation": "Active"
}
```
* /api/markings/{id} (PUT)
``` json
{
  "name": "Transporte",
  "situation": "Active"
}
```

### Tickets (Boletos)

|  Rosource  |      Request               |           Description                          |  Header  |
|------------|----------------------------|------------------------------------------------|    -     |
|    GET     |/api/tickets                | Retorna a lista de boletos do usuário.         |  token   |
|    GET     |/api/tickets/situations     | Retorna as situações que podem ter um boleto.  |  token   |
|    GET     |/api/tickets/{id}           | Retorna um boleto a partir do seu id.          |  token   |
|    POST    |/api/tickets                | Registra um novo boleto para o usuário.        |  token   |
|    PUT     |/api/tickets/{id}           | Atualiza informações de um boleto.             |  token   |     
|    PUT     |/api/tickets/{id}/{situacao}| Altera a situação de um boleto (0/1/2)         |  token   | 
|    DELETE  |/api/tickets/{id}           | Exclui um boleto a partir do seu id.           |  token   |

* /api/tickets (POST)
``` json
{
  "title": "BOLETO C DE TALZ EXEMPLO",
  "barcode": 4534534534534334534534,
  "dateDue": "2018-10-01",
  "situation": "AwaitingPayment",
  "marking":{
    "id":5
  }
}
```
* /api/tickets/{id} (PUT)
``` json
{
  "title": "BOLETO TROCADO POR D",
  "barcode": 4534534534534334534534,
  "dateDue": "2018-01-02",
  "situation": "AwaitingPayment"
}
```

Desenvolvimento
---------------

* Deverá ter o postgresql com versão atual;
* Versão do java deverá ser 1.8 (verifique no terminal **java -version**)
### Banco de Dados ###

Na primeira execução, deverá habilitar o generation, para assim, criar as tabelas.
Descomente a linha no persistence.xml (<property name="eclipselink.ddl-generation" value="drop-and-create-tables"/> e comente o outro bloco)
Depois de rodar a primeira vez e notar que as tabelas foram criadas corretamente, comente o drop-and-create e descomente a linha (<property name="eclipselink.ddl-generation" value="create-or-extend-tables"/>)

Para executar a aplicação corretamente, basta seguir os tópicos a seguir:
### Baixar dependências do Maven e gerar .jar da app  ###

No terminal, execute: **./mvnw package**
No Windows não necessita do **./**
### Pronto, só executar a aplicação ###

No terminal, execute: **java -jar ./target/boletos-1.0-SNAPSHOT.jar**