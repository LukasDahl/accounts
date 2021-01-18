set -e

mvn clean package -Dquarkus.package.type=uber-jar -Dmaven.test.skip=true

docker build -t accounts .

docker-compose up -d --build

sleep 10s

mvn test

docker-compose down