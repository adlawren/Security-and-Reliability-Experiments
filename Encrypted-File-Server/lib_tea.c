#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include "TEALibrary.h"

void encrypt (long *v, long *k) {
    /* TEA encryption algorithm */
	unsigned long y = v[0], z=v[1], sum = 0;
	unsigned long delta = 0x9e3779b9, n=32;

	while (n-- > 0){
		sum += delta;
		y += (z<<4) + k[0] ^ z + sum ^ (z>>5) + k[1];
		z += (y<<4) + k[2] ^ y + sum ^ (y>>5) + k[3];
	}

	v[0] = y;
	v[1] = z;
}

void print_buffer_and_key(jsize bufLen, jchar *bufCopy, jsize keyLen, jlong *keyCopy) {
    printf("Buffer length: %d\n", bufLen);
    printf("Key length: %d\n", keyLen);

    printf("Buffer contents: ");

    int i;
    for (i = 0; i < bufLen; ++i) {
        printf("%c", bufCopy[i]);
    }

    printf("\n");

    printf("Key contents: ");

    for (i = 0; i < keyLen; ++i) {
        printf("%lu ", keyCopy[i]);
    }

    printf("\n");
}

void print_values(long *values) {
    printf("Values: ");
    printf("%lx ", values[0]);
    printf("%lx\n", values[1]);
}

/* Encrypt function */
JNIEXPORT jcharArray JNICALL Java_TEALibrary_encrypt
(JNIEnv *env, jobject object, jcharArray buf, jlongArray key) {
    jsize bufLen;
    jchar *bufCopy;

    jsize keyLen;
    jlong *keyCopy;

    jboolean *is_copy = 0;

    bufLen = (*env)->GetArrayLength(env, buf);
    bufCopy = (jchar *) (*env)->GetCharArrayElements(env, buf, is_copy);
    if (bufCopy == NULL) {
      printf("ERROR: Array could not be retrieved from JVM.\n");
      exit(0);
    }

    keyLen = (*env)->GetArrayLength(env, key);
    keyCopy = (jlong *) (*env)->GetLongArrayElements(env, key, is_copy);
    if (bufCopy == NULL) {
      printf("ERROR: Array could not be retrieved from JVM.\n");
      exit(0);
    }

    // TODO: pad values
    // ...

    jcharArray encryptedBuf = (*env)->NewCharArray(env, bufLen);
    jchar *encryptedCopy = (jchar *) (*env)->GetCharArrayElements(env, encryptedBuf, is_copy);
    if (encryptedCopy == NULL) {
      printf("ERROR: Array could not be retrieved from JVM.\n");
      exit(0);
    }

    // Duplicate array contents
    int i;
    for (i = 0; i < bufLen; ++i) {
      encryptedCopy[i] = bufCopy[i];
    }

    printf("Test: %lu\n", sizeof(jchar)); // ???

    int block_size = 2 * sizeof(long), block_idx = 0;

    long values[2];
    values[0] = 0;
    values[1] = 0;

    for (i = 0; i < bufLen; ++i) {
        int idx_mod = i % block_size;

        long next_value = (long) encryptedCopy[i];
        if (idx_mod < block_size / 2) {
            values[0] |= (next_value << idx_mod * 8);
        } else {
            values[1] |= (next_value << idx_mod * 8);
        }

        if ((i + 1) % block_size == 0) {
            encrypt(values, keyCopy);

            // Replace contents of string with encrypted contents
            printf("Values to characters: ");
            int k;
            for (k = 0; k < block_size; ++k) {
                long next_encrypted_value;

                if (k < block_size / 2) {
                    next_encrypted_value = (values[0] >> (k * 8)) & 0xff;
                    printf("%c", (jchar) next_encrypted_value);
                } else {
                    next_encrypted_value = (values[1] >> (k * 8)) & 0xff;
                    printf("%c", (jchar) next_encrypted_value);
                }

                encryptedCopy[ block_idx * block_size + k ] = (jchar) next_encrypted_value;
            }

            printf("\n");

            printf("After encryption: ");
            print_values(values);

            ++block_idx;

            values[0] = 0;
            values[1] = 0;
        }
    }

    printf("C: Encrypted Result in Hex: ");
    for (i = 0; i < bufLen; ++i) {
        printf("%x", encryptedCopy[i]);
    }

    printf("\n");

    printf("C: Encrypted Result: ");
    for (i = 0; i < bufLen; ++i) {
        printf("%c", encryptedCopy[i]);
    }

    printf("\n");

    (*env)->ReleaseCharArrayElements(env, encryptedBuf, encryptedCopy, 0);

    return encryptedBuf;
}

void decrypt (long *v, long *k){
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

/* Decrypt function */
JNIEXPORT jcharArray JNICALL Java_TEALibrary_decrypt
(JNIEnv *env, jobject object, jcharArray buf, jlongArray key) {
    jsize bufLen;
    jchar *bufCopy;

    jsize keyLen;
    jlong *keyCopy;

    jboolean *is_copy = 0;

    bufLen = (*env)->GetArrayLength(env, buf);
    bufCopy = (jchar *) (*env)->GetCharArrayElements(env, buf, is_copy);
    if (bufCopy == NULL) {
      printf("ERROR: Array could not be retrieved from JVM.\n");
      exit(0);
    }

    keyLen = (*env)->GetArrayLength(env, key);
    keyCopy = (jlong *) (*env)->GetLongArrayElements(env, key, is_copy);
    if (bufCopy == NULL) {
      printf("ERROR: Array could not be retrieved from JVM.\n");
      exit(0);
    }

    jcharArray decryptedBuf = (*env)->NewCharArray(env, bufLen);
    jchar *decryptedCopy = (jchar *) (*env)->GetCharArrayElements(env, decryptedBuf, is_copy);
    if (decryptedCopy == NULL) {
      printf("ERROR: Array could not be retrieved from JVM.\n");
      exit(0);
    }

    print_buffer_and_key(bufLen, bufCopy, keyLen, keyCopy);

    // Duplicate array contents
    int i;
    for (i = 0; i < bufLen; ++i) {
      decryptedCopy[i] = bufCopy[i];
    }

    int block_size = 2 * sizeof(long), block_idx = 0;

    long values[2];
    values[0] = 0;
    values[1] = 0;

    for (i = 0; i < bufLen; ++i) {
        int idx_mod = i % block_size;

        long next_value = (long) decryptedCopy[i];
        if (idx_mod < block_size / 2) {
            values[0] |= (next_value << idx_mod * 8);
        } else {
            values[1] |= (next_value << idx_mod * 8);
        }

        if ((i + 1) % block_size == 0) {
            decrypt(values, keyCopy);

            // Replace contents of string with encrypted contents
            printf("Values to characters: ");
            int k;
            for (k = 0; k < block_size; ++k) {
                long next_encrypted_value;

                if (k < block_size / 2) {
                    next_encrypted_value = (values[0] >> (k * 8)) & 0xff;
                    printf("%c", (jchar) next_encrypted_value);
                } else {
                    next_encrypted_value = (values[1] >> (k * 8)) & 0xff;
                    printf("%c", (jchar) next_encrypted_value);
                }

                decryptedCopy[ block_idx * block_size + k ] = (jchar) next_encrypted_value;
            }

            printf("\n");

            printf("After decryption: ");
            print_values(values);

            ++block_idx;

            values[0] = 0;
            values[1] = 0;
        }
    }

    printf("C: Decrypted Result in Hex: ");
    for (i = 0; i < bufLen; ++i) {
        printf("%x", decryptedCopy[i]);
    }

    printf("\n");

    printf("C: Decrypted Result: ");
    for (i = 0; i < bufLen; ++i) {
        printf("%c", decryptedCopy[i]);
    }

    printf("\n");

    (*env)->ReleaseCharArrayElements(env, decryptedBuf, decryptedCopy, 0);

    return decryptedBuf;
}
