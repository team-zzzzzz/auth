FROM openjdk:21-slim

# jar파일 복사
COPY build/libs/projectAuth-1.0.jar auth.jar
ENTRYPOINT ["java","-jar","auth.jar"]