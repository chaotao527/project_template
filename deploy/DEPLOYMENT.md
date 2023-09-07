# 打包前注意

确保 subsitute-env-variables.sh 换行符为 LF

# 部署步骤

1. 将 deploy.zip 文件拷贝到部署目录下解压，参考指令 `unzip deploy.zip`
2. 进入解压后的 deploy 文件夹，参考指令 `cd deploy`
3. 执行脚本 deploy-prepare.sh 进行部署前的准备和检查，参考指令 `sh deploy-prepare.sh -i HOST_IP -a AI_IP -e ETL_IP -x XTS_IP`
4. 检查端口占用情况，上一步执行后，屏幕上会打印出端口占用情况，默认情况下，本系统会使用如下端口：

* AI_IP 192.168.0.212
* ETL_IP 192.168.0.211
* XTS_IP 10.10.10.101
* minio 使用 9000，9001
* tpweb 使用 80

5. 检查生成的 docker-compose.yml 文件中各环境变量是否的赋值，比如 XXX_HOST= 是否都赋值正确，注意
   ETL_HOST 要带端口，比如：localhost:9420
6. 启动指令，`docker-compose up -d`

# 注意事项

1. keycloak 启动后，登录 http://${IP}/auth，设置 master 的 realm setttings 的 ssl required 为
   none，然后保存，在此之前
   bizservice 会启动失败并不断重试，等到设置完成后会自动启动成功。
2. 全新部署前要删除现有的卷，只删除 tp-postgres-data
   即可，参考命令 `docker volume rm tp-postgres-data`，再执行 deploy-prepare.sh

# 启动后的检查

1. 浏览器登录 http://${IP}:9000，使用 minio-root-user:minio-root-password 登录，查看 bucket 的 events
   中是否已经注册了事件，若有则正常，否则不正常，可能是 bizservice 还没有启动成功导致的。
2. 浏览器登录 http://${IP}/adminer，系统选择 PostgreSQL，服务器填 postgres，用户名 root， 密码
   example，数据库 tp，登录后查看表中包含 batch_ 和 biz 开头的表则说明数据库初始化正常。
3. 浏览器登录 http://${IP}/nacos，用户名 nacos，密码 nacos，左侧选择服务管理中的服务列表，能看到
   biz-service 和 biz-batch-service 说明应用启动正常。
4. 浏览器登录 http://${IP}/auth，能正常打开网页，则说明 keycloak 正常。