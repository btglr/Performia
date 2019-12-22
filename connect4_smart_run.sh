#!/bin/sh

java -Dfile.encoding=UTF-8 -Djava.util.logging.config.file="$PWD/logging.properties" -classpath "$PWD/out:$PWD/src/libs/json.jar:$PWD/src/libs/junit.jar:$PWD/src/libs/mysql-connector.jar" ai.ai_connect4_smart.Connect4
