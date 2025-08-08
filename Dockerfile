# Estágio 1: Construir os Assets do Frontend (Tailwind CSS)
# Usamos uma imagem Node.js para ter acesso ao npm
FROM node:18-alpine AS frontend-builder

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia os arquivos de gerenciamento de pacotes primeiro para aproveitar o cache do Docker
COPY package.json package-lock.json ./

# Instala as dependências do npm
RUN npm install

# Copia o resto dos arquivos do projeto necessários para o build do CSS
COPY . .

# Executa o script de build para gerar o arquivo CSS final
RUN npm run build:css

# ---

# Estágio 2: Construir o Backend (Aplicação Spring Boot)
# Usamos uma imagem do Gradle que já inclui o JDK 17
FROM gradle:8.5-jdk17-alpine AS backend-builder

# Define o diretório de trabalho
WORKDIR /app

# Copia todo o projeto da sua máquina local
COPY . .

# Copia o arquivo CSS gerado do estágio anterior (frontend-builder)
# Isso garante que o JAR final contenha o CSS compilado
COPY --from=frontend-builder /app/src/main/resources/static/css/output.css ./src/main/resources/static/css/output.css

# Executa o build do Gradle para criar o JAR executável
# A flag '-x test' pula a execução dos testes, o que é comum para builds em Docker
RUN ./gradlew build -x test

# ---

# Estágio 3: Imagem Final de Execução
# Usamos uma imagem JRE leve para a aplicação final, muito menor que um JDK completo
FROM eclipse-temurin:17-jre-jammy

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo JAR executável do estágio de build do backend
# O caminho para o JAR é geralmente build/libs/*.jar
COPY --from=backend-builder /app/build/libs/*.jar app.jar

# Expõe a porta em que a aplicação roda
EXPOSE 8080

# Define o comando para executar a aplicação quando o contêiner iniciar
ENTRYPOINT ["java", "-jar", "app.jar"]
