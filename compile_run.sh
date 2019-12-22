#!/bin/sh

mkdir out
javac -d out src/**/*.java -cp "src/libs/json.jar:src/libs/junit.jar:src/libs/mysql-connector.jar"
javac -d out src/Performia.java -cp "src/libs/json.jar:src/libs/junit.jar:src/libs/mysql-connector.jar:out"
java -Dfile.encoding=UTF-8 -Djava.util.logging.config.file="$PWD/logging.properties" -classpath "$PWD/out:$PWD/src/libs/json.jar:$PWD/src/libs/junit.jar:$PWD/src/libs/mysql-connector.jar" Performia