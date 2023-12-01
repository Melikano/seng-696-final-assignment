import java.util.Base64;

public class Cryptography {

    public static String encrypt(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public static String decrypt(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }
}
