FROM openjdk:21-jdk-slim

WORKDIR /app

# Установим зависимости для JasperReports (freetype и fontconfig)
RUN apt-get update && apt-get install -y \
    fontconfig \
    libfreetype6 \
    && rm -rf /var/lib/apt/lists/*

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]