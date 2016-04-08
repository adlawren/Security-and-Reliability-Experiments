public class TEALibrary {
    public native long[] encrypt(long[] buffer, long[] key);
    public native long[] decrypt(long[] buffer, long[] key);
}
