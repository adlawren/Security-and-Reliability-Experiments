public class TEALibrary {
    public native char[] encrypt(char[] buffer, long[] key);
    public native char[] decrypt(char[] buffer, long[] key);
}
