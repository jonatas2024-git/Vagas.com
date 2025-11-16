# =========================
# STAGE 1: BUILD
# =========================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copia arquivos e resolve dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código-fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests

# =========================
# STAGE 2: RUNTIME
# =========================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia o JAR compilado do estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Define porta exposta (Spring Boot padrão)
EXPOSE 8080

# Inicia aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]



#FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
#WORKDIR /app
#COPY pom.xml .
#RUN mvn dependency:go-offline
#COPY src ./src
#RUN mvn clean install -DskipTests

# Estágio 2: Runtime
#FROM eclipse-temurin:17-jre-alpine
#WORKDIR /app
#COPY --from=build /app/target/vagas-backend-1.0.0.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]