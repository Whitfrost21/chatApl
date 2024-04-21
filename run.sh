#!/bin/sh

mkdir -p ./bin
cd ./messenger/
javac -d ../bin/ "Client.java"
javac -d ../bin/ "Server.java"
java -cp ../bin/ "Server" $* &
java -cp ../bin/ "Client" $*
