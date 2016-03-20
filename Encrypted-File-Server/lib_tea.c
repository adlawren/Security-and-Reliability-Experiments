#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include "TEALibrary.h"

/* Encrypt function */
// JNIEXPORT jcharArray JNICALL Java_TEALibrary_encrypt
// (JNIEnv *env, jobject object, jcharArray buf){

// JNIEXPORT void JNICALL Java_TEALibrary_encrypt
// (JNIEnv *env, jobject object, jstring buf){

JNIEXPORT jstring JNICALL Java_TEALibrary_encrypt
(JNIEnv *env, jobject object, jstring buf){
  jsize len;
  char *myCopy;
  jboolean *is_copy = 0;

  len = (*env)->GetStringLength(env, buf); // TODO: Test
  myCopy = (char *) (*env)->GetStringUTFChars(env, buf, is_copy);
  if (myCopy == NULL) {
    printf("ERROR: Array could not be retrieved from JVM.\n");
    exit(0);
  }

  printf("Buffer contents: ");

  int i;
  for (i = 0; i < len; ++i) {
      printf("%c", myCopy[i]);
  }

  printf("\n");

  printf("C: length: %d\n", len);
  char result[len];
  // jstring sortedBuf = (*env)->NewStringUTF(env, result);
  // if (result == NULL){
  //   printf("ERROR: Array could not be retrieved from JVM.\n");
  //   exit(0);
  // }

  // Duplicate array contents in reverse order
  int idx = -1;
  for (idx = 0; idx < len; ++idx) {
    result[len - (idx + 1)] = myCopy[idx];
  }

  // (*env)->ReleaseStringUTFChars(env, sortedBuf, result);
  jstring toReturn = (*env)->NewStringUTF(env, result);
  if (toReturn == NULL){
    printf("ERROR: Array could not be retrieved from JVM.\n");
    exit(0);
  }

  // (*env)->ReleaseStringUTFChars(env, toReturn, result); // Issue

  printf("Within encrypt\n");

  // return sortedBuf;
  return toReturn;
}

/* Decrypt function */
JNIEXPORT jcharArray JNICALL Java_TEALibrary_decrypt
(JNIEnv *env, jobject object, jcharArray buf){
    jsize len;
    jchar *myCopy;
    jboolean *is_copy = 0;

    len = (*env)->GetArrayLength(env, buf);
    myCopy = (jchar *) (*env)->GetCharArrayElements(env, buf, is_copy);
    if (myCopy == NULL) {
      printf("ERROR: Array could not be retrieved from JVM.\n");
      exit(0);
    }

    printf("Buffer contents: ");

    int i;
    for (i = 0; i < len; ++i) {
        printf("%c", myCopy[i]);
    }

    printf("\n");

    jcharArray sortedBuf = (*env)->NewCharArray(env, len);
    jchar *sortedCopy = (jchar *) (*env)->GetCharArrayElements(env, sortedBuf, is_copy);
    if (sortedCopy == NULL){
      printf("ERROR: Array could not be retrieved from JVM.\n");
      exit(0);
    }

    // Duplicate array contents in reverse order
    int idx = -1;
    for (idx = 0; idx < len; ++idx) {
      sortedCopy[len - (idx + 1)] = myCopy[idx];
    }

    (*env)->ReleaseCharArrayElements(env, sortedBuf, sortedCopy, 0);

    printf("Within decrypt\n");

    return sortedBuf;
}
