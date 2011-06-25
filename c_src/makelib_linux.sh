#!/bin/bash
g++ -Wall -fno-common -O0 -fPIC *.c -I/usr/local/include -c -m64
g++ -shared -fno-common -Wall -fPIC -O0 -lstdc++ -o ../lib/libJBloom.so *.o -m64 

