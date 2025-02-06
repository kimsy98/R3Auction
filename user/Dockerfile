# JDK 19 기반 이미지 사용 (user.jar이 JDK 19에서 빌드되었으니까)
FROM openjdk:19-jdk-slim

# 컨테이너 내에서 작업할 디렉토리 설정
WORKDIR /app

# 호스트의 user.jar을 컨테이너 내부로 복사
COPY user.jar /app/user.jar

# 실행 명령어 (JAR 실행)
CMD ["java", "-jar", "user.jar"]
