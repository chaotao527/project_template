::call npm --prefix=./tp-web run build:deploy

::docker pull maven:3.8.6-eclipse-temurin-17
::docker pull bitnami/kafka:3.3.1-debian-11-r5
::docker pull bitnami/zookeeper:3.8.0-debian-11-r49
::docker pull bitnami/keycloak:20.0.3-debian-11-r13
::docker pull postgres:alpine3.16
::docker pull bitnami/minio:2022.10.8-debian-11-r0
::docker pull nginx:1.23.2-alpine
::docker pull dpage/pgadmin4:6.20

set TAG=%date:~0,4%%date:~5,2%%date:~8,2%%time:~0,2%%time:~3,2%%time:~6,2%
set TAG=%TAG: =0%

::docker run -it --rm --name build -v %cd%:/usr/src/mymaven -v %HOMEDRIVE%\%HOMEPATH%\.m2:/root/.m2 -w /usr/src/mymaven maven:3.8.6-eclipse-temurin-17 mvn -DskipTests=true clean install
docker build . --build-arg JAR_FILE=./biz-service/target/biz-service.jar -t tpservice/bizservice:%TAG%
docker build . --build-arg JAR_FILE=./biz-batch-service/target/biz-batch-service.jar -t tpservice/bizbatchservice:%TAG%
::docker build . --build-arg JAR_FILE=./biz-shell/target/biz-shell.jar -t tpservice/bizshell:%TAG%
::docker build ./tp-web -t tpweb:%TAG%

mkdir deploy\images

del deploy\images\tpservice_bizservice_*
del deploy\images\tpservice_bizbatchservice_*
::del deploy\images\tpservice_bizshell_*
::del deploy\images\tpweb_*

docker save -o ./deploy/images/tpservice_bizservice_%TAG%.tar tpservice/bizservice:%TAG%
docker save -o ./deploy/images/tpservice_bizbatchservice_%TAG%.tar tpservice/bizbatchservice:%TAG%
::docker save -o ./deploy/images/tpweb_%TAG%.tar tpweb:%TAG%

::docker save -o ./deploy/images/kafka.tar bitnami/kafka:3.3.1-debian-11-r5
::docker save -o ./deploy/images/zookeeper.tar bitnami/zookeeper:3.8.0-debian-11-r49
::docker save -o ./deploy/images/keycloak.tar bitnami/keycloak:20.0.3-debian-11-r13
::docker save -o ./deploy/images/postgres.tar postgres:alpine3.16
::docker save -o ./deploy/images/minio.tar bitnami/minio:2022.10.8-debian-11-r0
::docker save -o ./deploy/images/nginx.tar nginx:1.23.2-alpine
::docker save -o ./deploy/images/pgadmin4.tar dpage/pgadmin4:6.20
::docker save -o ./deploy/images/java17.tar eclipse-temurin:17-jre-alpine