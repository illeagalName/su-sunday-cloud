#!/bin/bash
#基础目录/root/
#基础文件 deploy.sh,docker-compose.yml

#停止服务，删除镜像
docker-compose down
docker rmi -f sunday-gateway
docker rmi -f sunday-auth
docker rmi -f sunday-system-service
docker rmi -f sunday-job-service
docker rmi -f sunday-user-service
docker rmi -f sunday-bot-service
rm -fr /root/webapp
#重新编译包
cd /root/sunday || exit
git pull
mvn clean package -DskipTests

#copy 安装包
mkdir -p /root/webapp/sunday-gateway /root/webapp/sunday-system-service /root/webapp/sunday-auth /root/webapp/sunday-job-service /root/webapp/sunday-user-service /root/webapp/sunday-bot-service

cp  /root/sunday/sunday-gateway/target/sunday-gateway-1.0-SNAPSHOT.jar /root/webapp/sunday-gateway
echo "cp sunday-gateway-1.0-SNAPSHOT.jar to webapp"

cp  /root/sunday/sunday-auth/target/sunday-auth-1.0-SNAPSHOT.jar /root/webapp/sunday-auth
echo "cp sunday-auth-1.0-SNAPSHOT.jar to webapp"

cp  /root/sunday/sunday-modules/sunday-job-service/target/sunday-job-service-1.0-SNAPSHOT.jar /root/webapp/sunday-job-service
echo "cp sunday-job-service-1.0-SNAPSHOT.jar to webapp"

cp  /root/sunday/sunday-modules/sunday-user-service/target/sunday-user-service-1.0-SNAPSHOT.jar /root/webapp/sunday-user-service
echo "cp sunday-user-service-1.0-SNAPSHOT.jar to webapp"

cp  /root/sunday/sunday-modules/sunday-system-service/target/sunday-system-service-1.0-SNAPSHOT.jar /root/webapp/sunday-system-service
echo "cp sunday-system-service-1.0-SNAPSHOT.jar to webapp"

cp  /root/sunday/sunday-modules/sunday-bot-service/target/sunday-bot-service-1.0-SNAPSHOT.jar /root/webapp/sunday-bot-service
echo "cp sunday-bot-service-1.0-SNAPSHOT.jar to webapp"

#copy dockerfile
cp  /root/sunday/sunday-auth/src/main/resources/Dockerfile  /root/webapp/sunday-auth
echo "cp sunday-auth Dockerfile to webapp"
cp  /root/sunday/sunday-gateway/src/main/resources/Dockerfile  /root/webapp/sunday-gateway
echo "cp sunday-gateway Dockerfile to webapp"
cp  /root/sunday/sunday-modules/sunday-job-service/src/main/resources/Dockerfile  /root/webapp/sunday-job-service
echo "cp sunday-job-service Dockerfile to webapp"
cp  /root/sunday/sunday-modules/sunday-user-service/src/main/resources/Dockerfile  /root/webapp/sunday-user-service
echo "cp sunday-user-service Dockerfile to webapp"
cp  /root/sunday/sunday-modules/sunday-system-service/src/main/resources/Dockerfile  /root/webapp/sunday-system-service
echo "cp sunday-system-service Dockerfile to webapp"
cp  /root/sunday/sunday-modules/sunday-bot-service/src/main/resources/Dockerfile  /root/webapp/sunday-bot-service
echo "cp sunday-bot-service Dockerfile to webapp"


#重新打包发布
cd /root || exit
docker-compose up --build -d
