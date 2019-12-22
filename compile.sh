#!/bin/sh

mkdir out
javac -d out src/**/**/*.java -cp "out:src/libs/json.jar:src/libs/junit.jar:src/libs/mysql-connector.jar"
javac -d out src/**/*.java -cp "src/libs/json.jar:src/libs/junit.jar:src/libs/mysql-connector.jar"
javac -d out src/Performia.java -cp "src/libs/json.jar:src/libs/junit.jar:src/libs/mysql-connector.jar:out"
