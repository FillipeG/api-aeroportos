# API Rest: Gerenciamento de Aeroportos (OpenFlights)

## 1. Objetivo
Este projeto consiste no desenvolvimento de uma API REST completa para gerenciar o cadastro de aeroportos globais. O sistema utiliza o conjunto de dados do projeto OpenFlights e permite operações de criação, leitura, atualização e exclusão (CRUD) de aeroportos em um banco de dados relacional.

A API segue o padrão arquitetural REST e expõe endpoints para manipulação de recursos seguindo as melhores práticas de mercado.

## 2. Tecnologias Utilizadas
* **Linguagem:** Java 17+
* **Framework:** Spring Boot 3.x
* **Gerenciamento de Dependências:** Maven
* **Banco de Dados:** MySQL (Com ambientes isolados para Produção e Testes)
* **Testes:** JUnit 5, Mockito
* **Plugins Maven:**
    * `maven-surefire-plugin`: Execução de testes de unidade.
    * `maven-failsafe-plugin`: Execução de testes de integração.

## 3. Configuração do Ambiente e Instalação

### Pré-requisitos
* Java JDK 17 ou superior.
* Maven 3.8+ (ou utilizar o wrapper `mvnw` incluso no projeto).
* Git.

### Passo a passo
1.  Clone o repositório:
    ```bash
    git clone [https://github.com/FillipeG/api-aeroportos.git](https://github.com/FillipeG/api-aeroportos.git)
    cd api-aeroportos
    ```

2.  Instale as dependências via Maven:
    ```bash
    ./mvnw clean install
    ```

## 4. Como Executar a Aplicação
Execute o comando abaixo na raiz do projeto:
```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: http://localhost:8080/api/v1/aeroportos

## 5. Endpoints principais

| Método | URL                         | Descrição                         |
| ------ | --------------------------- | --------------------------------- |
| GET    | `/api/v1/aeroportos`        | Listar todos os aeroportos        |
| GET    | `/api/v1/aeroportos/{iata}` | Buscar aeroporto pelo código IATA |
| POST   | `/api/v1/aeroportos`        | Cadastrar novo aeroporto          |
| PUT    | `/api/v1/aeroportos/{iata}` | Atualizar dados de um aeroporto   |
| DELETE | `/api/v1/aeroportos/{iata}` | Remover um aeroporto              |


## 6. Executando os testes

Testes de Unidade
Executa testes isolados (Services/Models):
```bash
./mvnw test
```

Testes de Integração
Executa o fluxo completo (Controller → DB):
```bash
./mvnw verify
```

## 7. Adendo: Carga Automática de Dados

Para atender ao requisito de popular o banco de dados com o arquivo airports.csv, decidi ir além da importação manual e pesquisar como automatizar esse processo via código.
A Solução: Implementei uma classe que roda automaticamente assim que o sistema inicia (usando CommandLineRunner). Ela lê o arquivo CSV, processa as linhas e salva no banco de dados.



