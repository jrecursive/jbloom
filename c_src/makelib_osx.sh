#!/bin/bash
g++ -Wall -fno-common -O0 -fPIC *.c -I/usr/local/include -c -arch x86_64 -m64
g++ -dynamiclib -fno-common -Wall -fPIC -O0 -lstdc++ -o ../lib/libJBloom.jnilib *.o -framework JavaVM -m64 -arch x86_64

