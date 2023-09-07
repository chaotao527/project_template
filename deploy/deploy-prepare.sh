#!/bin/bash

helpFunction() {
  echo ""
  echo "Usage: $0 -i binding_ip"
  echo -e "\t-i BINDING_IP to bind"
  exit 1
}

#读取参数
while getopts "i:" arg
do
  case $arg in
  i) BINDING_IP=$OPTARG ;;
  ?) helpFunction ;;
  esac
done

echo BINDING_IP:$BINDING_IP

#检测参数
if [ -z "$BINDING_IP" ]
then
  echo "No IP to bind was supplied!"
  helpFunction
fi

# 加载 Docker 镜像
ls images | xargs -i docker load --input images/{}

# 离线安装 docker-compose 若必要
if [ ! -f /usr/local/bin/docker-compose ]; then
  cp docker-compose-linux-x86_64 docker-compose
  mv docker-compose /usr/local/bin
  chmod +x /usr/local/bin/docker-compose
fi

# 创建卷
docker volume create --name=tp-minio-data
docker volume create --name=tp-postgres-data

# 替换 docker-compose.yml 内的 tag
TAG=`ls images | grep tpweb | sed -En 's/tpweb_(.*).tar/\1/p'`
cp docker-compose.yml.template docker-compose.yml
sed -i "s/tpweb:[0-9]\+$/tpweb:$TAG/g" docker-compose.yml
sed -i "s/bizservice:[0-9]\+$/bizservice:$TAG/g" docker-compose.yml
sed -i "s/bizbatchservice:[0-9]\+$/bizbatchservice:$TAG/g" docker-compose.yml

# 替换 HOST_IP
# ETH0_IP=`ifconfig eth0 | sed -En 's/127.0.0.1//;s/.*inet (addr:)?(([0-9]*\.){3}[0-9]*).*/\2/p' | head -n 1`
# [[ -z ETH0_IP ]] && echo '没有发现 eth0 的 IP 地址！'
 sed -i "s/HOST_IP/$BINDING_IP/g" docker-compose.yml

# 确认服务器端口占用情况
PORT_80=`netstat -lntup | tr -s ' ' | cut -d ' ' -f 4 | sed -En 's/.*:([0-9]+)/\1/p' | sort | uniq | grep -e '^80$' | wc -l`
PORT_9000=`netstat -lntup | tr -s ' ' | cut -d ' ' -f 4 | sed -En 's/.*:([0-9]+)/\1/p' | sort | uniq | grep -e '^9000$' | wc -l`
PORT_9001=`netstat -lntup | tr -s ' ' | cut -d ' ' -f 4 | sed -En 's/.*:([0-9]+)/\1/p' | sort | uniq | grep -e '^9001$' | wc -l`

if [ "$PORT_80" -gt 0 ]; then
  echo "80端口被占用了！"
fi

if [ "$PORT_9000" -gt 0 ]; then
  echo "9000 端口被占用了！"
fi

if [ "$PORT_9001" -gt 0 ]; then
  echo "9001 端口被占用了！"
fi
