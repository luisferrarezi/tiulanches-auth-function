# TIU LANCHES - Function
| :placard: Vitrine.Dev |     |
| -------------  | --- |
| :sparkles: Nome        | **Tiu Lanches - App**
| :label: Tecnologias | GitHub Actions, Java, Maven, Spring, MySQL, Azure Functions 
| :rocket: URL         | 
| :fire: Desafio     | Tech Challenge FIAP

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

<!-- Inserir imagem com a #vitrinedev ao final do link -->
![](https://miro.medium.com/v2/resize:fit:640/format:webp/1*DnjQxNSg4jPAe9q0DW76vg.gif#vitrinedev)

# Detalhes do projeto
## Objetivo
Projeto criado para complementar o projeto principal [Tiu Lanches](https://github.com/luisferrarezi/tiulanches)

# Arquitetura
Este repositório é exclusivo para a function que será enviada para a Azure Function.

## Segurança
A branch main está bloqueada para commit direto.

É necessário ser criado um pull request para que após aprovado possa ser realizado o merge para a branch principal

## Automação
Atualmente para esta branch existem 2 níveis de automação, explicado abaixo:

- Pull Request: este é executado no momento que é criado o PR, e nele é realizado um build para garantir que a function não foi quebrada após alteração.
- Push: este é executado somente após o PR ter sido aprovado e executado o merge para a main, primeiro é realizado o build da function, estando tudo ok com a aplicação é então realizado o deploy para a Azure.

## Infraestrutura
Esta aplicação tem como único objetivo validar o token que é recebido após o usuário se autenticar através de sua conta pessoal na microsoft, estando validado é então registrado no banco de dados que aquele cliente está logado.

## Software
### Linguagem
- Java - JDK 17

### Banco de dados
- MySql - 8.0

### Frameworks utilizados 
- Azure Functions - SDK Java
- Lombok
- msal4j - Azure
- Maven 
- Jackson Databind

### Variáveis de Ambiente
Existe a variável de ambiente que é indispensável para que a function funcione corretamente:
- SCM_CREDENTIALS=<SCM_CREDENTIALS> - São credenciais que precisam ser adquiridas através da estrutura criada no Azure Function, sem essas credenciais o deploy não é realizado.
