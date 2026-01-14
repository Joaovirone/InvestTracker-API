
#1. Base: Vamos usar uma imagem que já tem o Java 21 instalado

FROM eclipse-temurin:21-jdk


#2. Diretório de trabalho: Cria uma pasta dentro do container para nosso app
WORKDIR /app

#3. Cópia: Pega o arquivo .jar que o Gradle gerou e jgoa dentro do container
#O asterísco (*.jar) serve para pegar qualquer nome de arquivo gerado
COPY build/libs/*.jar app.jar

#4. Execução: O comando que roda o Spring Boot quando o container subir
ENTRYPOINT [ "java", "-jar", "app.jar" ]