FROM eclipse-temurin:21-jre

WORKDIR /app

COPY ./build/libs/optpractice-0.0.1-SNAPSHOT.jar app.jar

# (2) scouter agent (agent.java 폴더를 이미지에 포함)
#    빌드 컨텍스트에 agent.java가 있어야 함(다운로드 후 복사하거나, repo에 포함하거나)
COPY agent.java /scouter/agent.java

# (3) Scouter 설정파일 위치 지정 + javaagent 장착
#    -Dscouter.config 로 설정 파일 경로 지정
ENV JAVA_TOOL_OPTIONS="-javaagent:/scouter/agent.java/scouter.agent.jar -Dscouter.config=/scouter/agent.java/conf/scouter.conf"

# EXPOSE 8080

# (4) 실행
ENTRYPOINT ["sh","-c","java $JAVA_TOOL_OPTIONS -jar /app/app.jar"]