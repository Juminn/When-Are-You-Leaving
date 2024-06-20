FROM azul/zulu-openjdk:11
ADD build/libs/costCalculrator-0.0.1-SNAPSHOT.jar costCalculrator-0.0.1-SNAPSHOT.jar
EXPOSE  8080

# 환경 변수로 Java 옵션 설정
ENV JAVA_OPTS="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/heap_dumps/heapdump.hprof"

# 엔트리 포인트에 환경 변수 추가
ENTRYPOINT java -jar /costCalculrator-0.0.1-SNAPSHOT.jar $JAVA_OPTS