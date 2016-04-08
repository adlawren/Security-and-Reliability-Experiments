public class LoginPair {
    private String userId = null;
    private long[] key = null;

    public LoginPair(String id, long[] k) {
        userId = id;
        key = k;
    }

    public String getUserId() {
        return userId;
    }

    public long[] getKey() {
        return key;
    }
}
