javac *.java
javah NativeInsertionSort
gcc -I$JAVA_HOME/include -I$JAVA_HOME/include/linux -shared -fpic -o libinsertionsort.so lib_insertionsort.c
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.
