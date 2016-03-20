javac *.java
javah TEALibrary
gcc -Wall -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -shared -fpic -o libtea.so lib_tea.c
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.
