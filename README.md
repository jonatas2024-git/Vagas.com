# 🎓 Projeto de Conclusão — Orientações para Desenvolvimento do Back-end


Bem-vindo(a) ao repositório do **Projeto de Conclusão**.  

Este documento reúne as **orientações, requisitos e boas práticas** para o desenvolvimento da camada **Back-end**, servindo como guia para estruturação, planejamento e documentação do sistema.

---

## Visão Geral

O objetivo deste projeto é demonstrar a capacidade de planejar, desenvolver e documentar um **sistema Back-end completo**, seguro, escalável e aderente às boas práticas de engenharia de software.

Cada equipe deverá adaptar os requisitos abaixo ao **escopo do seu projeto**, escolhendo as tecnologias e abordagens mais adequadas, em alinhamento com o(a) orientador(a).

---

## Requisitos Funcionais

### Autenticação e Segurança

- Login social (**OAuth2**) e cadastro próprio  
- Emissão de **token JWT** com expiração e refresh  
- Fluxo de **recuperação de senha por e-mail** (link único, validade limitada)  
- **Autenticação de dois fatores (2FA)** opcional via OTP para ações críticas  

### Gestão de Usuário

- Entidade **Perfil** com dados pessoais (nome, e-mail) e preferências (tema, notificações)  
- Endpoints para **consulta e edição de perfil** e configurações do usuário  

### CRUD de Entidades

- Criação, leitura, atualização e exclusão (**CRUD**) para todas as entidades de domínio  
- **Filtros dinâmicos e paginação** nas listagens  
- Tratamento de exceções padronizado (404, 400, 403, etc.)

### Detalhes de Item

- Consulta detalhada de entidades, incluindo **relacionamentos e metadados**  
- Cálculo de **métricas simples** (contagem, soma, média, etc.)

### Pesquisa Avançada

- **Busca full-text** com filtros em múltiplos campos  
- Registro de **histórico de buscas** para sugestões e análise  

---

## Requisitos Não Funcionais

###  Segurança

- Proteção de APIs via **JWT e HTTPS**  
- Mitigação de **SQL Injection**, **XSS** e **CSRF**  
- Uso de **cabeçalhos de segurança** (CSP, HSTS, X-Frame-Options)

### Performance

- Uso de **cache distribuído** (Redis ou equivalente)  
- **Lazy loading**, índices adequados e queries otimizadas  

### Escalabilidade

- **Arquitetura modular ou microsserviços**  
- Componentes auxiliares em outras linguagens, conforme necessidade  

###  Manutenibilidade e Deploy

- **Containerização com Docker**  
- **Pipeline CI/CD** (GitHub Actions, GitLab CI ou Jenkins)  

# Tecnologias Recomendadas

| Categoria | Ferramentas Sugeridas |
|------------|----------------------|
| **Back-end** | Java + Spring Boot |
| **Banco de Dados** | MySQL / PostgreSQL / MongoDB |
| **Cache** | Redis |
| **Documentação de API** | Swagger / OpenAPI 3 |
| **Containerização** | Docker |
| **CI/CD** | GitHub Actions / GitLab CI / Jenkins |
| **Testes** | JUnit, Mockito, Testcontainers |
| **Autenticação** | Spring Security + OAuth2 / JWT |

##  Padrões e Boas Práticas

- Seguir **Clean Architecture / Hexagonal Architecture** quando possível  
- Nomear pacotes por domínio (`controller`, `service`, `repository`, `model`, `dto`, `exception`)  
- Adotar **DTOs** para comunicação com o front-end  
- Utilizar **Lombok** para reduzir boilerplate (`@Getter`, `@Setter`, `@Builder`, etc.)  
- Validar inputs com **Jakarta Validation** (`@NotNull`, `@Email`, etc.)  
- Tratar erros via **ControllerAdvice** e respostas padronizadas  

## 📚 Documentação e Entregáveis

Cada equipe deve entregar:

1. **Guia de configuração do ambiente local**
   - Pré-requisitos (Java, Docker, banco de dados, etc.)
   - Passos para rodar o projeto (`docker-compose up`)

2. **Documentação da API**
   - Endpoints descritos via **Swagger / OpenAPI**
   - Exemplos de requisições e respostas (Postman ou Insomnia)

3. **Diagramas**
   - Diagrama de **Entidade-Relacionamento (ER)**  
   - Diagramas de **fluxo principais** (autenticação, CRUD, busca)

4. **Testes**
   - Cobertura mínima recomendada: **>70%**
   - Testes unitários e de integração automatizados

---

## ▶Como Executar o Projeto

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/nome-do-projeto.git

# Acessar o diretório
cd nome-do-projeto

# Rodar com Docker
docker-compose up --build

Após inicializar, a API estará disponível em:

http://localhost:8080


Documentação Swagger:

http://localhost:8080/swagger-ui.html


👥 Autores e Contato

Equipe de Desenvolvimento

  > Nome do(a) Aluno(a) 1 — [email@exemplo.com
  > Nome do(a) Aluno(a) 2 — [email@exemplo.com
  > Nome do(a) Aluno(a) 3 — [email@exemplo.com

Orientador(a): Nome do Professor(a)