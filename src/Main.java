public class Main {
    public static void main(String[] args) {
        Config config = new Config("1010011", 32);
        String message = "10011101011111001001000110010011";
        Config.CipherText c = config.Encrypt(message);
        String decryptedMessage = config.Decrypt(c);
        System.out.println("Input message : " + message);
        System.out.println("Output message: " + decryptedMessage);
    }
}