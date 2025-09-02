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



## ⚙️ Instalação e Configuração

Este projeto foi desenvolvido com **React 18** e utiliza o **Vite** como bundler. A estrutura segue o padrão **Feature-Sliced Design (FSD)** para garantir escalabilidade e organização.

### 📦 Dependências obrigatórias

Antes de iniciar, certifique-se de ter o **Node.js** instalado (versão recomendada: 18+).

Instale as dependências principais com:

```bash
npm install
```

Caso esteja configurando o projeto do zero, certifique-se de instalar:

```bash
npm install react@18.2.0 react-dom@18.2.0
npm install react-router-dom@6
npm install axios
```
