# Estoque (Spring Boot + Thymeleaf)

Aplicação web simples para gerenciamento de usuários e perfil, construída com Spring Boot, Thymeleaf e Tailwind CSS. O projeto já vem com autenticação via formulário, páginas de login/registro, perfil do usuário (edição de dados e troca de senha), layout responsivo e banco de dados H2 em memória.

## Sumário
- Visão geral e funcionalidades
- Requisitos
- Como baixar o projeto
- Instalação e preparo do ambiente
- Executando a aplicação
- Scripts úteis (Frontend/CSS)
- Estrutura do projeto
- Funcionamento interno (arquitetura, classes e templates)
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
- Layout e páginas com Thymeleaf + Tailwind CSS (usando thymeleaf-layout-dialect).
- Banco H2 em memória (dados reiniciam a cada execução) com usuário admin gerado automaticamente.

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
├─ src/
│  ├─ main/
│  │  ├─ java/com/danielschmitz/estoque/
│  │  │  ├─ EstoqueDeProdutosApplication.java   # Classe principal (Spring Boot)
│  │  │  ├─ config/
│  │  │  │  ├─ SecurityConfig.java              # Configuração do Spring Security
│  │  │  │  └─ DataInitializer.java             # Seed de dados (usuário admin)
│  │  │  ├─ controller/
│  │  │  │  ├─ HomeController.java              # Rota “/” (index)
│  │  │  │  ├─ AuthenticationController.java    # Login e Registro
│  │  │  │  └─ ProfileController.java           # Perfil (GET/POST) e troca de senha
│  │  │  ├─ service/
│  │  │  │  ├─ UserService.java                 # Regras de negócio de usuário
│  │  │  │  ├─ AuthenticationService.java       # (suporte à autenticação)
│  │  │  │  └─ TokenService.java                # (utilitários de token, se aplicável)
│  │  │  ├─ repository/
│  │  │  │  └─ UserRepository.java              # Acesso a dados (JPA)
│  │  │  └─ model/
│  │  │     └─ User.java                        # Entidade e UserDetails
│  │  └─ resources/
│  │     ├─ application.properties              # Configuração da aplicação
│  │     ├─ static/
│  │     │  └─ css/                             # input.css / output.css (Tailwind)
│  │     └─ templates/
│  │        ├─ layout/base.html                 # Layout base (thymeleaf-layout-dialect)
│  │        ├─ index.html                       # Página inicial
│  │        ├─ login.html                       # Login
│  │        ├─ register.html                    # Registro
│  │        └─ profile.html                     # Perfil (dados e senha)
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

### Camadas principais
- Controller (Web):
  - `HomeController` – GET `/` retorna `index`.
  - `AuthenticationController` – GET `/login` e `/register` mostram páginas; POST `/register` cria usuário (via `UserService`) e redireciona com mensagens de sucesso/erro.
  - `ProfileController` – GET `/profile` retorna `profile` com o usuário autenticado; POST `/profile` atualiza nome/e-mail; POST `/profile/password` altera senha.
- Service (Regras de negócio):
  - `UserService` –
    - `register(User)` aplica validação de e-mail único, codifica senha e salva.
    - `save(User, newName, newEmail)` atualiza dados com verificação de e-mail único.
    - `savePassword(User, password)` codifica e atualiza a senha.
- Repository (Persistência):
  - `UserRepository` – interface Spring Data JPA para `User`, com `findByEmail`.
- Model (Domínio):
  - `User` – entidade JPA e implementação de `UserDetails` (utilizada pelo Spring Security). Campos: `id`, `name`, `email` (único), `password` (hash). Autoridades padrão: `ROLE_USER`.

### Templates e layout
- `layout/base.html` – define o layout padrão (cabeçalho, navegação, container principal) usando thymeleaf-layout-dialect.
- `login.html` / `register.html` – formulários de autenticação e cadastro.
- `profile.html` – formulário para editar nome/e-mail e outro para trocar senha.
- `index.html` – página inicial que exibe mensagens de sucesso e boas-vindas.

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

Observação: Por padrão, o Spring Security protege todas as rotas exceto as explicitamente liberadas em `SecurityConfig`.

## Dados iniciais (seed)
Na primeira execução, se não existir um usuário com o e-mail do admin, será criado automaticamente:
- Email: `admin@email.com`
- Senha: `123`

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
