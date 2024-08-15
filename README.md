# Controle Fácil

Controle Fácil é um aplicativo de gerenciamento financeiro desenvolvido para ajudar estudantes, estagiários e jovens profissionais a gerenciar suas finanças pessoais de maneira eficiente. O aplicativo oferece funcionalidades para categorizar despesas, registrar receitas, definir metas financeiras e muito mais.

## Funcionalidades

- **Cadastro e Login:** Autenticação de usuários com segurança.
- **Gerenciamento de Saldo:** Registro e acompanhamento do saldo disponível.
- **Registro de Receitas:** Armazenamento e visualização de receitas.
- **Metas Financeiras:** Definição e acompanhamento de metas financeiras pessoais.
- **Categorias de Despesas:** Criação e uso de categorias para despesas.
- **Registro de Despesas:** Registro e gerenciamento de despesas, com opção de categorização e tipo (fixa/variável).

## Estrutura do Banco de Dados

### Tabelas

- **Usuario**  
  Armazena informações básicas dos usuários.

- **Saldo**  
  Registra os saldos dos usuários.

- **Renda**  
  Armazena as receitas dos usuários.

- **MetaSonhos**  
  Detalha as metas financeiras dos usuários.

- **CategoriaDespesa**  
  Armazena categorias para despesas.

- **Despesa**  
  Registra as despesas dos usuários.

### Relações entre Tabelas

- **Usuario** and **Saldo:** One-to-Many
- **Usuario** and **Renda:** One-to-Many
- **Usuario** and **MetaSonhos:** One-to-Many
- **Usuario** and **Despesa:** One-to-Many
- **CategoriaDespesa** and **Despesa:** One-to-Many

## Tecnologias Utilizadas

- **Backend:** Java, Spring Boot
- **Frontend:** React
- **Banco de Dados:** MySQL

## Configuração

### Requisitos

- JDK 17 ou superior
- MySQL 8.0 ou superior
- Maven ou Gradle para gerenciamento de dependências
