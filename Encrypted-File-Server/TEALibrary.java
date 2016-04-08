public class TEALibrary {
    // public native char[] encrypt(char[] buffer, long[] key);
    // public native byte[] encrypt(byte[] buffer, long[] key);
    public native long[] encrypt(long[] buffer, long[] key);

    // public native char[] decrypt(char[] buffer, long[] key);
    // public native byte[] decrypt(byte[] buffer, long[] key);
    public native long[] decrypt(long[] buffer, long[] key);
}
