# syntax=docker/dockerfile:1

# ---- Build: compila o jar com o Maven Wrapper do projeto ----
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Dependências primeiro: esta camada só é refeita quando o pom.xml muda
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -q dependency:go-offline

COPY src/ src/
# Os testes rodam fora da imagem (./mvnw test): o contextLoads precisa de banco e das variáveis de ambiente
RUN ./mvnw -B -q package -DskipTests

# ---- Runtime: só a JRE e o jar, rodando sem root ----
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
USER app

ENV TZ=America/Sao_Paulo \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
