# Estoque (Spring Boot + Thymeleaf)

Aplicação web para gerenciamento de estoque (Produtos, Categorias e Locais), construída com Spring Boot, Thymeleaf e Tailwind CSS. O projeto inclui autenticação via formulário, CRUDs completos de categorias, locais e produtos (com busca e validações), layout responsivo e banco de dados H2 em memória.

## Sumário
- Visão geral e funcionalidades
- Requisitos
- Como baixar o projeto
- Instalação e preparo do ambiente
- Executando a aplicação
- Executar com Docker
- Scripts úteis (Frontend/CSS)
- Estrutura do projeto
- Funcionamento interno (arquitetura, classes e templates)
- CRUDs: Categorias, Locais e Produtos
- Endpoints principais
- Dados iniciais (seed)
- Configurações
- Dicas de desenvolvimento
- Solução de problemas (FAQ)
- Licença

---

## Visão geral e funcionalidades
- Autenticação com Spring Security (form login):
  - Páginas públicas: `/login`, `/register`, assets estáticos (`/css/**`, `/js/**`).
  - Demais rotas exigem usuário autenticado.
- Registro de novo usuário.
- Perfil do usuário autenticado:
  - Atualização de nome e e-mail (com verificação de e-mail único).
  - Troca de senha (com hashing via BCrypt).
- CRUD de Categorias: criação, listagem, edição e remoção; nome obrigatório e único (case-insensitive).
- CRUD de Locais: criação, listagem, edição e remoção; nome obrigatório e único (case-insensitive).
- CRUD de Produtos: criação, listagem, edição e remoção; campos nome, código de barras (único) e categoria (combo). Tela principal com filtros por nome e categoria.
- Layout e páginas com Thymeleaf + Tailwind CSS (usando thymeleaf-layout-dialect).
- Banco H2 em memória (dados reiniciam a cada execução) com usuário admin e dados fictícios gerados automaticamente (categorias, locais e produtos de exemplo).

## Requisitos
- Git
- Java 17 (JDK 17)
- Node.js 16+ e npm (para compilar o CSS via Tailwind)
- Gradle NÃO precisa ser instalado: o projeto usa o Gradle Wrapper (`gradlew/gradlew.bat`).

## Como baixar o projeto
```bash
# via HTTPS
git clone <URL_DO_REPOSITORIO>
# ou via SSH
git clone git@github.com:danielschmitz/estoque-thymeleaf.git

cd estoque
```

Substitua `<URL_DO_REPOSITORIO>` pela URL real caso esteja usando outra origem.

## Instalação e preparo do ambiente
1) Verifique o Java:
```bash
java -version
```
Deve indicar Java 17.

2) Instale dependências do frontend (Tailwind):
```bash
npm install
```

3) (Opcional) Gere o CSS uma vez:
```bash
npm run build:css
```
Durante o desenvolvimento, use o modo vigilância (`watch`) para recompilar o CSS automaticamente (veja seção “Scripts úteis”).

## Executando a aplicação
Você pode rodar via Gradle (recomendado em desenvolvimento) ou gerar o JAR.

- Windows (PowerShell ou Prompt):
```powershell
./gradlew.bat bootRun
```

- Linux/macOS:
```bash
./gradlew bootRun
```

A aplicação iniciará em http://localhost:8080

## Executar com Docker
O projeto possui um `Dockerfile` multi-stage que compila o CSS (Tailwind), constrói o JAR com Gradle e gera uma imagem enxuta com JRE 17.

1) Construir a imagem:
```bash
docker build -t estoque-app .
```
2) Executar o contêiner:
```bash
docker run --rm -p 8080:8080 --name estoque estoque-app
```
3) Acessar: http://localhost:8080

Observações:
- O `.dockerignore` está configurado para reduzir o contexto de build.
- O estágio de frontend roda `npm install` e `npm run build:css` para gerar `static/css/output.css`.

### Gerar o JAR executável
```bash
# Windows
./gradlew.bat clean bootJar

# Linux/macOS
./gradlew clean bootJar
```
O artefato ficará em `build/libs/estoque-0.0.1-SNAPSHOT.jar`. Para executar:
```bash
java -jar build/libs/estoque-0.0.1-SNAPSHOT.jar
```

## Scripts úteis (Frontend/CSS)
Definidos em `package.json`:
- `npm run build:css` – compila o CSS de `src/main/resources/static/css/input.css` para `src/main/resources/static/css/output.css`.
- `npm run watch:css` – compila e acompanha mudanças em tempo real durante o desenvolvimento.

Dica: execute `watch:css` em um terminal separado enquanto trabalha na aplicação.

## Estrutura do projeto
```
estoque/
├─ build.gradle                # Configuração Gradle (Java 17, dependências Spring)
├─ settings.gradle             # Nome do projeto: estoque
├─ gradlew / gradlew.bat       # Gradle Wrapper
├─ package.json                # Scripts npm (Tailwind)
├─ Dockerfile                  # Build multi-stage (Tailwind + Gradle + JRE)
├─ .dockerignore               # Ignora arquivos no contexto de build
├─ src/
│  ├─ main/
│  │  ├─ java/com/danielschmitz/estoque/
│  │  │  ├─ EstoqueDeProdutosApplication.java   # Classe principal (Spring Boot)
│  │  │  ├─ config/
│  │  │  │  ├─ SecurityConfig.java              # Configuração do Spring Security
│  │  │  │  └─ DataInitializer.java             # Seed de dados (admin, categorias, locais, produtos)
│  │  │  ├─ controller/
│  │  │  │  ├─ HomeController.java              # Rota “/” (index)
│  │  │  │  ├─ AuthenticationController.java    # Login e Registro
│  │  │  │  ├─ ProfileController.java           # Perfil (GET/POST) e troca de senha
│  │  │  │  ├─ CategoryController.java          # CRUD de Categorias
│  │  │  │  ├─ LocationController.java          # CRUD de Locais
│  │  │  │  └─ ProductController.java           # CRUD de Produtos + filtros
│  │  │  ├─ service/
│  │  │  │  ├─ UserService.java                 # Regras de negócio de usuário
│  │  │  │  ├─ AuthenticationService.java       # (suporte à autenticação)
│  │  │  │  └─ TokenService.java                # (utilitários de token, se aplicável)
│  │  │  │  ├─ CategoryService.java             # Regras de categorias (validação nome único)
│  │  │  │  ├─ LocationService.java             # Regras de locais (validação nome único)
│  │  │  │  └─ ProductService.java              # Regras de produtos (validação + filtros)
│  │  │  ├─ repository/
│  │  │  │  ├─ UserRepository.java              # Acesso a dados (JPA)
│  │  │  │  ├─ CategoryRepository.java          # existsByNameIgnoreCase
│  │  │  │  ├─ LocationRepository.java          # existsByNameIgnoreCase
│  │  │  │  └─ ProductRepository.java           # Busca por nome/categoria e código de barras único
│  │  │  └─ model/
│  │  │     ├─ User.java                        # Entidade e UserDetails
│  │  │     ├─ Category.java                    # Entidade de Categoria (nome único)
│  │  │     ├─ Location.java                    # Entidade de Local (nome único)
│  │  │     └─ Product.java                     # Entidade de Produto (nome, código de barras, categoria)
│  │  └─ resources/
│  │     ├─ application.properties              # Configuração da aplicação
│  │     ├─ static/
│  │     │  └─ css/                             # input.css / output.css (Tailwind)
│  │     └─ templates/
│  │        ├─ layout/base.html                 # Layout base (thymeleaf-layout-dialect)
│  │        ├─ index.html                       # Página inicial
│  │        ├─ login.html                       # Login
│  │        ├─ register.html                    # Registro
│  │        ├─ profile.html                     # Perfil (dados e senha)
│  │        ├─ categories/                      # Templates de categorias
│  │        │  ├─ index.html                    # Listagem e ações
│  │        │  └─ form.html                     # Formulário (criar/editar)
│  │        ├─ locations/                       # Templates de locais
│  │        │  ├─ index.html
│  │        │  └─ form.html
│  │        └─ products/                        # Templates de produtos
│  │           ├─ index.html                    # Filtros (nome/categoria) + tabela
│  │           └─ form.html                     # Formulário com combo de categorias
│  └─ test/                                     # Testes (se/quando adicionados)
└─ ...
```

## Funcionamento interno
### Fluxo de autenticação e segurança
- `SecurityConfig` define que as rotas `/login`, `/register`, `/css/**` e `/js/**` são públicas; todas as outras exigem autenticação.
- Login por formulário em `/login` com redirecionamento padrão para `/` após sucesso.
- Logout em `/logout` (invalida sessão e remove cookies), redirecionando para `/login?logout`.
- Senhas armazenadas com `BCryptPasswordEncoder`.

### Semente de dados (DataInitializer)
- Ao subir a aplicação, `DataInitializer` cria um usuário admin caso ele ainda não exista:
  - Email: `admin@email.com`
  - Senha: `123` (hash armazenado via BCrypt)
- E popula dados fictícios caso as respectivas tabelas estejam vazias:
  - Categorias: "Perecíveis", "Limpeza", "Bebidas".
  - Locais: "Depósito A", "Depósito B", "Loja 1".
  - Produtos: 3 itens de exemplo (com códigos de barras únicos) vinculados às categorias acima.

### Camadas principais
- Controller (Web):
  - `HomeController` – GET `/` retorna `index`.
  - `AuthenticationController` – GET `/login` e `/register` mostram páginas; POST `/register` cria usuário (via `UserService`) e redireciona com mensagens de sucesso/erro.
  - `ProfileController` – GET `/profile` retorna `profile` com o usuário autenticado; POST `/profile` atualiza nome/e-mail; POST `/profile/password` altera senha.
  - `CategoryController` – CRUD de categorias:
    - Listagem em `/categories`; novo em `/categories/new`; criar via POST `/categories`; editar em `/categories/{id}/edit`; atualizar via POST `/categories/{id}`; excluir via POST `/categories/{id}/delete`.
    - Mensagens de sucesso/erro via RedirectAttributes.
  - `LocationController` – CRUD de locais (rotas análogas a categorias): `/locations` ...
  - `ProductController` – CRUD de produtos e filtros:
    - Listagem com filtros por nome (`name`) e categoria (`categoryId`) em `/products`.
    - Novo `/products/new`, criar POST `/products`, editar `/products/{id}/edit`, atualizar POST `/products/{id}`, excluir POST `/products/{id}/delete`.
- Service (Regras de negócio):
  - `UserService` –
    - `register(User)` aplica validação de e-mail único, codifica senha e salva.
    - `save(User, newName, newEmail)` atualiza dados com verificação de e-mail único.
    - `savePassword(User, password)` codifica e atualiza a senha.
  - `CategoryService` – valida nome obrigatório/único e executa operações CRUD.
  - `LocationService` – valida nome obrigatório/único e executa operações CRUD.
  - `ProductService` – valida campos obrigatórios (nome, código de barras, categoria), garante código de barras único e executa buscas com filtros por nome/categoria.
- Repository (Persistência):
  - `UserRepository` – interface Spring Data JPA para `User`, com `findByEmail`.
  - `CategoryRepository` – `existsByNameIgnoreCase` para verificação de duplicidade.
  - `LocationRepository` – `existsByNameIgnoreCase` semelhante a categorias.
  - `ProductRepository` – métodos de busca:
    - `findByNameContainingIgnoreCase`, `findByCategory_Id`, `findByNameContainingIgnoreCaseAndCategory_Id` e `existsByCodigoDeBarras`.
- Model (Domínio):
  - `User` – entidade JPA e implementação de `UserDetails` (utilizada pelo Spring Security). Campos: `id`, `name`, `email` (único), `password` (hash). Autoridades padrão: `ROLE_USER`.
  - `Category` – entidade JPA com `id` e `name` (único e obrigatório).
  - `Location` – entidade JPA com `id` e `name` (único e obrigatório).
  - `Product` – entidade JPA com `id`, `name`, `codigoDeBarras` (único e obrigatório) e `category` (ManyToOne obrigatório).

### Templates e layout
- `layout/base.html` – define o layout padrão (cabeçalho, navegação, container principal) usando thymeleaf-layout-dialect.
- `login.html` / `register.html` – formulários de autenticação e cadastro.
- `profile.html` – formulário para editar nome/e-mail e outro para trocar senha.
- `index.html` – página inicial que exibe mensagens de sucesso e boas-vindas.
- `categories/index.html` – lista categorias com ações de editar e excluir; exibe mensagens de feedback.
- `categories/form.html` – formulário para criar/editar categoria (nome obrigatório).
- `locations/index.html` – lista locais com ações de editar e excluir.
- `locations/form.html` – formulário para criar/editar local (nome obrigatório).
- `products/index.html` – filtros por nome/categoria e tabela com ID, nome, código de barras e categoria; ações de editar/excluir.
- `products/form.html` – formulário com nome, código de barras e comboBox de categorias.

### Estilos (Tailwind CSS)
- O CSS é gerado a partir de `static/css/input.css` para `static/css/output.css` pelos scripts npm.
- Em desenvolvimento, rode `npm run watch:css` para refletir alterações automaticamente.

## Endpoints principais
- `GET /` – Página inicial (autenticado)
- `GET /login` – Página de login (pública)
- `POST /login` – Processa login (Spring Security)
- `GET /register` – Página de cadastro (pública)
- `POST /register` – Cria usuário
- `GET /profile` – Página de perfil (autenticado)
- `POST /profile` – Atualiza nome/e-mail
- `POST /profile/password` – Troca de senha
- `POST /logout` – Efetua logout

CRUD de Categorias
- `GET /categories` – Listagem
- `GET /categories/new` – Formulário de criação
- `POST /categories` – Criar
- `GET /categories/{id}/edit` – Formulário de edição
- `POST /categories/{id}` – Atualizar
- `POST /categories/{id}/delete` – Excluir

CRUD de Locais
- `GET /locations` – Listagem
- `GET /locations/new` – Formulário de criação
- `POST /locations` – Criar
- `GET /locations/{id}/edit` – Formulário de edição
- `POST /locations/{id}` – Atualizar
- `POST /locations/{id}/delete` – Excluir

CRUD de Produtos (com filtros)
- `GET /products?name={texto}&categoryId={id}` – Listagem com filtros
- `GET /products/new` – Formulário de criação
- `POST /products` – Criar
- `GET /products/{id}/edit` – Formulário de edição
- `POST /products/{id}` – Atualizar
- `POST /products/{id}/delete` – Excluir

Observação: Por padrão, o Spring Security protege todas as rotas exceto as explicitamente liberadas em `SecurityConfig`.

## Dados iniciais (seed)
Na primeira execução, se não existir um usuário com o e-mail do admin, será criado automaticamente:
- Email: `admin@email.com`
- Senha: `123`

Se as tabelas estiverem vazias, também serão criados dados fictícios:
- Categorias: "Perecíveis", "Limpeza", "Bebidas".
- Locais: "Depósito A", "Depósito B", "Loja 1".
- Produtos: 3 itens de exemplo (códigos únicos) vinculados às categorias.

Use essas credenciais para acessar e, se desejar, altere a senha em `/profile`.

## Configurações
- Banco de dados: H2 em memória (configuração padrão via `runtimeOnly 'com.h2database:h2'`). Os dados são voláteis e reiniciam a cada execução.
- Porta: por padrão, Spring Boot usa a porta `8080`. Você pode alterar em `src/main/resources/application.properties`.
- Dependências (principais):
  - Spring Boot Web, Thymeleaf, Security, Data JPA
  - Thymeleaf Layout Dialect
  - H2 Database (runtime)
  - Lombok (compileOnly + annotationProcessor)
  - DevTools (developmentOnly)

## Dicas de desenvolvimento
- DevTools: já incluído. Ao salvar mudanças, a aplicação pode reiniciar automaticamente (hot reload). Em alguns casos, execute novamente `bootRun`.
- Lombok: habilite “annotation processing” no seu IDE para evitar erros de compilação.
- Tailwind: mantenha `npm run watch:css` rodando para atualizar estilos.

## Solução de problemas (FAQ)
- “A porta 8080 já está em uso”
  - Encerre o processo que está usando a porta 8080 ou altere a porta no `application.properties` (ex.: `server.port=8081`).
- “As páginas estão sem estilo (CSS não carrega)”
  - Rode `npm install` e depois `npm run build:css` (ou `npm run watch:css`). Verifique se `static/css/output.css` foi gerado.
- “Erro relacionado ao Lombok”
  - Ative “annotation processing” no IDE (IntelliJ: Settings > Build, Execution, Deployment > Compiler > Annotation Processors).
- “Meu usuário some ao reiniciar a aplicação”
  - O H2 é em memória; os dados não persistem por padrão. Considere configurar um banco persistente (ex.: PostgreSQL) em `application.properties` para produção.

## Licença
Este projeto é fornecido para fins educacionais/demonstração. Ajuste a licença conforme necessário.
