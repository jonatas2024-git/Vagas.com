# Vagas.com
Projeto final +PraTi

Planejamento do MVP - Candidato/Usuário.


Funcionalidades do Usuário (Candidato)

Cadastro e Login de Candidato:
Descrição: Permite que o usuário crie uma conta com e-mail e senha, e faça login.

Backlog: 

Criação da tela de cadastro.
Criação da tela de login.
Lógica para registrar novo usuário no banco de dados.
Lógica para autenticar o usuário (login).
Validação de campos (e-mail, senha).

Busca de Vagas:
Descrição: O usuário pode pesquisar vagas por palavra-chave e localização.

Backlog: 

Criação de campo de busca na página principal.
Lógica de busca para filtrar vagas com base em texto.
Exibir lista de vagas encontradas em um formato de cartão.


Visualização e Candidatura a Vagas:
Descrição: Ao clicar em uma vaga, o candidato vê os detalhes e pode se candidatar.

Backlog:

Criação da página de detalhes da vaga.
Exibir título, descrição, requisitos, e informações da empresa.
Botão "Candidatar-se".
Lógica para registrar a candidatura no banco de dados.


Planejamento do MVP - Empresa/Recrutador.

Funcionalidades da Empresa (Recrutador):

Cadastro e Login de Empresa:
Descrição: Permite que uma empresa crie uma conta e faça login.

Backlog: 

Criação da tela de cadastro de empresa (diferente da de candidato).
Lógica para registrar a empresa no banco de dados.


Publicação de Vagas:
Descrição: A empresa pode criar e publicar novas vagas.

Backlog: 

Criação de formulário para publicação de vaga (título, descrição, etc.).
Lógica para salvar a vaga no banco de dados, associada à empresa.

Visualização de Candidatos por Vaga:
Descrição: A empresa pode ver a lista de candidatos que se candidatam a uma vaga específica.

Backlog: 

Criação da página de "minhas vagas".
Exibir a lista de vagas publicadas pela empresa.
Ao clicar em uma vaga, mostrar a lista de candidatos que se aplicam.



Planejamento do MVP - Integração (Frontend / Backend).


O frontend irá fazer chamadas HTTP (usando fetch ou bibliotecas como Axios) para os endpoints do backend.


O backend irá receber essas requisições, processar a lógica de negócio e interagir com o banco de dados.


Os dados serão retornados em formato JSON.

