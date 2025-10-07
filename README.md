# Projeto de Conclusão - Orientações para desenvolvimento do Back-end.


A seguir vocês encontrarão um conjunto de requisitos funcionais e não funcionais organizados de forma clara e objetiva para orientar o desenvolvimento do Projeto de Conclusão da nossa formação, no que diz respeito ao Back-end.

Nem todos os grupos verão nesses requisitos sentido, devido à natureza do seu projeto. Portanto, cada equipe deverá entrar em contato comigo para adaptar estes itens ao escopo do trabalho e escolher as ferramentas mais adequadas.

Usem este material como referência para estruturar, planejar e documentar suas entregas.

Requisitos Funcionais:

* Autenticação e Segurança

  > Login social (OAuth2) e cadastro próprio
  > Emissão de token JWT com expiração e refresh
  > Fluxo de recuperação de senha por e-mail (link único, validade limitada)
  > 2FA opcional via OTP para ações críticas

* Gestão de Usuário
  > Entidade “Perfil” com dados pessoais (nome, e-mail) e preferências (tema, notificações)
  > Consulta e edição de perfil e configurações

* CRUD de Entidades
  > Criação, leitura, atualização e exclusão para todas as entidades de domínio
  > Filtros dinâmicos e paginação em listagens

* Detalhes de Item
  > Consulta detalhada incluindo relacionamentos e metadados
  > Cálculo de métricas simples (contagem, soma, média)

* Pesquisa Avançada
  > Busca full-text com filtros em múltiplos campos
  > Registro de histórico de buscas para sugestões e análise
  > Requisitos Não Funcionais:

* Segurança
  > Proteção de APIs via JWT e HTTPS
  > Mitigação de SQL Injection, XSS e CSRF
  > Cabeçalhos de segurança (CSP, HSTS, X-Frame-Options)

* Performance
  > Cache distribuído (Redis ou equivalente)
  > Lazy loading, índices adequados e otimização de queries

* Escalabilidade
  > Arquitetura modular ou microsserviços
  > Componentes auxiliares em linguagens diversas, conforme necessidade

* Manutenibilidade e Deploy
  > Containerização com Docker
  > Pipeline de CI/CD (GitHub Actions, GitLab CI ou Jenkins)

* Documentação
  > Guia de configuração do ambiente local
  > Exemplos de chamadas de API (Postman/Insomnia)
  > Diagramas de modelo de dados (ER) e fluxos principais

Tecnologias (sugestões; a definir por projeto):
  > Back-end: Java (Spring Boot ou similar)
  > Componentes auxiliares: serviços em outras linguagens conforme necessidade
  > Banco de Dados: MySQL, PostgreSQL (relacional) ou MongoDB (NoSQL)
  > Cache: Redis (cache distribuído)
  > Containerização: Docker
  > CI/CD: GitHub Actions, GitLab CI ou Jenkins
Documentação de API: Swagger / OpenAPI
