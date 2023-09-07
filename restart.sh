#!/bin/bash

# 今用于测试环境

git pull

sh mvnw clean
sh mvnw -DskipTests=true install

ps -elf | grep biz | tr -s ' ' | cut -d' ' -f4 | xargs -i kill -9 {}

rm -rf *.nohup

nohup java -jar --add-opens java.base/java.lang=ALL-UNNAMED \
            -DMINIO_HOST=10.1.140.144 \
            -DKEYCLOAK_HOST=10.1.140.144 \
            ./biz-service/target/biz-service.jar > biz-service.nohup &

sleep 20

nohup java -jar --add-opens java.base/java.lang=ALL-UNNAMED ./biz-batch-service/target/biz-batch-service.jar > biz-batch-service.nohup &

sleep 5

RESULT=`ps -elf | grep biz | wc -l`

if [[ $RESULT -eq '3' ]]
then
    echo '启动成功！'
else
    echo '启动失败！！！'
fi