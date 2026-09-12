FROM azul-zulu:8
WORKDIR /app
ENV TZ=Asia/Shanghai \
    JAVA_OPTS="-Xms2g -Xmx4g"
COPY target/demoJ-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
