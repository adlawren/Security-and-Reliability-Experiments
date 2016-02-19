javac *.java
javah MyBinarySearch
gcc -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -shared -fpic -o libbinsearch.so lib_binsearch.c
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.
