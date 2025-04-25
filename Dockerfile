FROM adoptopenjdk/openjdk11:alpine-jre

# Definir variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=prod \
    TZ=America/Rio_Branco

# Criar diretórios necessários
RUN mkdir -p /app/config /app/logs

# Definir diretório de trabalho
WORKDIR /app

# Copiar o arquivo JAR gerado pelo Maven
COPY target/*.jar /app/app.jar

# Expor a porta utilizada pela aplicação
EXPOSE 8484

# Configurar propriedades do Java
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

# Iniciar a aplicação
ENTRYPOINT java $JAVA_OPTS -jar /app/app.jar
