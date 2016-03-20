public class TEALibrary {

    // public native char[] encrypt(char[] buffer);
    // public native void encrypt(String buffer);
    public native String encrypt(String buffer);

    public native char[] decrypt(char[] buffer);
}
