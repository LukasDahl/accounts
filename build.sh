set -e

mvn clean package -Dquarkus.package.type=uber-jar -Dmaven.test.skip=true

docker build -t accounts .

docker-compose up -d --build

sleep 2s

mvn test

docker-compose down