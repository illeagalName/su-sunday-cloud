#指定基础镜像，在其上进行定制
FROM java:8

#复制上下文目录下的jar 到容器里
COPY sunday-job-service-1.0-SNAPSHOT.jar sunday-job-service.jar
#运行参数,包括jmx连接监控
ENV JAVA_OPTS="\
-server \
-Xms1024m \
-Xmx1024m \
-Xss1024k \
-XX:SurvivorRatio=8 \
-XX:+UseConcMarkSweepGC"

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

#指定容器启动程序及参数
ENTRYPOINT java -jar ${JAVA_OPTS} sunday-job-service.jar

