#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include "NativeInsertionSort.h"

/* Sort function */
JNIEXPORT jintArray JNICALL Java_NativeInsertionSort_insertionSort
(JNIEnv *env, jobject object, jintArray buf){
  jsize len;
  jint *myCopy;
  jboolean *is_copy = 0;

  jint memory_access_count = 0;

  len = (*env)->GetArrayLength(env, buf);
  myCopy = (jint *) (*env)->GetIntArrayElements(env, buf, is_copy);
  if (myCopy == NULL){
    printf("ERROR: Array could not be retrieved from JVM.\n");
    exit(0);
  }

  jintArray sortedBuf = (*env)->NewIntArray(env, len + 1);
  jint *sortedCopy = (jint *) (*env)->GetIntArrayElements(env, sortedBuf, is_copy);
  if (sortedCopy == NULL){
    printf("ERROR: Array could not be retrieved from JVM.\n");
    exit(0);
  }

  // Duplicate array contents
  int idx = -1;
  memory_access_count += 1;

  for (idx = 0; idx < len; ++idx) {
    sortedCopy[idx] = myCopy[idx];
    memory_access_count += 2;
  }

  // Sort the array using the insertion sort method
  int i = -1;
  memory_access_count += 1;

  for (i = 1; i < len; ++i) {

    int j = -1;
    for (j = i - 1; j >= 0; --j) {

      if (sortedCopy[j] > sortedCopy[j + 1]) {

        jint temp = sortedCopy[j];
        sortedCopy[j] = sortedCopy[j + 1];
        sortedCopy[j + 1] = temp;
        memory_access_count += 6;
      } else {
        break;
      }
    }
  }

  // Append memory access count to the end of the array
  sortedCopy[ len ] = memory_access_count + 1;

  (*env)->ReleaseIntArrayElements(env, sortedBuf, sortedCopy, 0);

  return sortedBuf;
}
