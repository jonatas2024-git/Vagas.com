# Vagas.com
Projeto final +PraTi

Planejamento do MVP - Backend

Tecnologias: Java com o framework Spring Boot.

Estrutura: 

Banco de Dados: PostgreSQL - para armazenar as informações de usuários, empresas e vagas.

API RESTful: Criar as APIs que o frontend irá consumir: 

Endpoints para autenticação (/api/auth/login, /api/auth/register).

Endpoints para usuários (/api/users/{id}, /api/users/{id}/profile).

Endpoints para vagas (/api/jobs, /api/jobs/{id}).

Endpoints para candidaturas (/api/applications).

Segurança: Implementar a segurança com JWT (JSON Web Tokens) para autenticação de requisições. Podemos usar o Spring Security para uso no Spring Boot.
