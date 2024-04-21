@echo off
mkdir bin
cd messenger
javac -d ../bin/ Client.java
javac -d ../bin/ Server.java
start java -cp ../bin/ Server %*
start java -cp ../bin/ Client %*
