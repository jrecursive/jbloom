#!/bin/bash
echo cleaning...
rm -rf bin ; mkdir bin ; mkdir lib
echo javac...
javac -d bin -cp src src/jbloom/*.java
echo javah...
javah -jni -classpath bin -d c_src jbloom.JBloom
echo build native lib...
cd c_src ; . makelib_linux.sh ; cd ..
echo run jbloom.TestJBloom
java -d64 -Djava.library.path=./lib -cp bin jbloom.TestJBloom

