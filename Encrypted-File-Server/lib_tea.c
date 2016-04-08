#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <jni.h>
#include "TEALibrary.h"

void duplicate_array_contents(jlong *src, jlong *dest, jsize len) {
    int i;
    for (i = 0; i < len; ++i) {
        dest[i] = src[i];
    }
}

void encrypt (long *v, long *k) {
    /* TEA encryption algorithm */
	unsigned long y = v[0], z=v[1], sum=0;
	unsigned long delta = 0x9e3779b9, n=32;

	while (n-- > 0){
		sum += delta;
		y += (z<<4) + k[0] ^ z + sum ^ (z>>5) + k[1];
		z += (y<<4) + k[2] ^ y + sum ^ (y>>5) + k[3];
	}

	v[0] = y;
	v[1] = z;
}

void decrypt (long *v, long *k) {
	/* TEA decryption routine */
	unsigned long n=32, sum, y=v[0], z=v[1];
	unsigned long delta=0x9e3779b9l;

	sum = delta<<5;
	while (n-- > 0) {
		z -= (y<<4) + k[2] ^ y + sum ^ (y>>5) + k[3];
		y -= (z<<4) + k[0] ^ z + sum ^ (z>>5) + k[1];
		sum -= delta;
	}

	v[0] = y;
	v[1] = z;
}

/* Encrypt function */
JNIEXPORT jlongArray JNICALL Java_TEALibrary_encrypt
(JNIEnv *env, jobject object, jlongArray buf, jlongArray key) {
    jsize bufLen;
    jlong *bufCopy;

    jsize keyLen;
    jlong *keyCopy;

    jboolean *is_copy = 0;

    bufLen = (*env)->GetArrayLength(env, buf);
    bufCopy = (jlong *) (*env)->GetLongArrayElements(env, buf, is_copy);
    if (bufCopy == NULL) {
      printf("ERROR: Array could not be retrieved from JVM.\n");
      exit(0);
    }

    keyLen = (*env)->GetArrayLength(env, key);
    if (keyLen != 4) {
        printf("ERROR: Key must be 256 bits long (4 longs must be provided).\n");
        exit(0);
    }

    keyCopy = (jlong *) (*env)->GetLongArrayElements(env, key, is_copy);
    if (bufCopy == NULL) {
      printf("ERROR: Array could not be retrieved from JVM.\n");
      exit(0);
    }

    jlong paddedSize = bufLen;
    if ((paddedSize % 2) == 0) {
        paddedSize = bufLen + 2;
    } else {
        paddedSize = bufLen + 3;
    }

    jlongArray encryptedBuf = (*env)->NewLongArray(env, paddedSize);
    jlong *encryptedCopy = (jlong *) (*env)->GetLongArrayElements(env, encryptedBuf, is_copy);
    if (encryptedCopy == NULL) {
      printf("ERROR: Array could not be retrieved from JVM.\n");
      exit(0);
    }

    // Randomize array contents
    srand(time(0));
    int i;
    for (i = 0; i < paddedSize; ++i) {
        encryptedCopy[i] = rand();
    }

    duplicate_array_contents(bufCopy, encryptedCopy, bufLen);
    encryptedCopy[ paddedSize - 2 ] = bufLen;

    long values[2] = {0, 0};
    for (i = 0; i < paddedSize; i += 2) {
        values[0] = encryptedCopy[i];
        values[1] = encryptedCopy[i + 1];

        encrypt(values, keyCopy);

        encryptedCopy[i] = values[0];
        encryptedCopy[i + 1] = values[1];
    }

    (*env)->ReleaseLongArrayElements(env, encryptedBuf, encryptedCopy, 0);

    return encryptedBuf;
}

/* Decrypt function */
JNIEXPORT jlongArray JNICALL Java_TEALibrary_decrypt
(JNIEnv *env, jobject object, jlongArray buf, jlongArray key) {
    jsize bufLen;
    jlong *bufCopy;

    jsize keyLen;
    jlong *keyCopy;

    jboolean *is_copy = 0;

    bufLen = (*env)->GetArrayLength(env, buf);
    bufCopy = (jlong *) (*env)->GetLongArrayElements(env, buf, is_copy);
    if (bufCopy == NULL) {
      printf("ERROR: Array could not be retrieved from JVM.\n");
      exit(0);
    }

    keyLen = (*env)->GetArrayLength(env, key);
    if (keyLen != 4) {
        printf("ERROR: Key must be 256 bits long (4 longs must be provided).\n");
        exit(0);
    }

    keyCopy = (jlong *) (*env)->GetLongArrayElements(env, key, is_copy);
    if (bufCopy == NULL) {
      printf("ERROR: Array could not be retrieved from JVM.\n");
      exit(0);
    }

    long values[2] = {0, 0};

    int i;
    for (i = 0; i < bufLen; i += 2) {
        values[0] = bufCopy[i];
        values[1] = bufCopy[i + 1];

        decrypt(values, keyCopy);

        bufCopy[i] = values[0];
        bufCopy[i + 1] = values[1];
    }

    jlong unpaddedSize = bufCopy[ bufLen - 2 ];

    // Create resultant buffer
    jlongArray decryptedBuf = (*env)->NewLongArray(env, unpaddedSize);
    jlong *decryptedCopy = (jlong *) (*env)->GetLongArrayElements(env, decryptedBuf, is_copy);
    if (decryptedCopy == NULL) {
      printf("ERROR: Array could not be retrieved from JVM.\n");
      exit(0);
    }

    // Copy appropriate values
    duplicate_array_contents(bufCopy, decryptedCopy, unpaddedSize);

    (*env)->ReleaseLongArrayElements(env, decryptedBuf, decryptedCopy, 0);

    return decryptedBuf;
}
