#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include "MySort.h"

/* C source for the native sorting mehtod */

int sort(jint *);

/* Sort function */
JNIEXPORT void JNICALL Java_MySort_sort
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

  sort(myCopy);
}

/* Sort subroutine */
int sort(jint *buf) {

  printf("Hello!\n");
}
