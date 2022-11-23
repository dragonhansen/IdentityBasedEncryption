import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String message = "itWorksWithText";
        int messageLenth = message.getBytes(StandardCharsets.UTF_8).length * 8 - 1;
        Config config = new Config("1010011", messageLenth);
        Config.CipherText c = config.Encrypt(message);
        String decryptedMessage = config.Decrypt(c);
        System.out.println("Input message : " + message);
        System.out.println("Output message: " + decryptedMessage);
    }
}