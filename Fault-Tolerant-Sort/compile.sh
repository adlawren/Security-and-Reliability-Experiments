javac *.java
javah MySort
gcc -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -shared -fpic -o libsort.so lib_sort.c
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.
