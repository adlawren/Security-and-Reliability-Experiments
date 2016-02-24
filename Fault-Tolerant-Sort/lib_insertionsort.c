#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include "NativeInsertionSort.h"

/* Sort function */
JNIEXPORT jintArray JNICALL Java_MySort_sort
(JNIEnv *env, jobject object, jintArray buf){
  jsize len;
  jint *myCopy;
  jboolean *is_copy = 0;

  len = (*env)->GetArrayLength(env, buf);
  myCopy = (jint *) (*env)->GetIntArrayElements(env, buf, is_copy);
  if (myCopy == NULL){
    printf("ERROR: Array could not be retrieved from JVM.\n");
    exit(0);
  }

  jintArray sortedBuf = (*env)->NewIntArray(env, len);
  jint *sortedCopy = (jint *) (*env)->GetIntArrayElements(env, sortedBuf, is_copy);
  if (sortedCopy == NULL){
    printf("ERROR: Array could not be retrieved from JVM.\n");
    exit(0);
  }

  // Duplicate array contents
  int idx = -1;
  for (idx = 0; idx < len; ++idx) {
    sortedCopy[idx] = myCopy[idx];
  }

  // Sort the array using the insertion sort method
  int i = -1;
  for (i = 1; i < len; ++i) {

    int j = -1;
    for (j = i - 1; j >= 0; --j) {

      if (sortedCopy[j] > sortedCopy[j + 1]) {

        jint temp = sortedCopy[j];
        sortedCopy[j] = sortedCopy[j + 1];
        sortedCopy[j + 1] = temp;
      } else {
        break;
      }
    }
  }

  (*env)->ReleaseIntArrayElements(env, sortedBuf, sortedCopy, 0);

  return sortedBuf;
}
