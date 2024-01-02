FROM ubuntu:latest

# 어떤 image를 사용할 것인가
FROM openjdk:17-jdk-alpine
# jar 파일로 만들어라
ARG JAR_FILE=build/libs/*.jar
# 실행
COPY ${JAR_FILE} demo-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["top", "-b"]