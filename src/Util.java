import org.miracl.core.BN254.ECP2;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Util {

    public static String xorBinaryString(String a, String b, int n){
        String ans = "";

        // Loop to iterate over the
        // Binary Strings
        for (int i = 0; i < n; i++)
        {
            // If the Character matches
            if (a.charAt(i) == b.charAt(i))
                ans += "0";
            else
                ans += "1";
        }
        return ans;
    }

    public static String convertToBinary(String message) {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        if(messageBytes[0] == 0 && message.charAt(0) == '1') messageBytes = Arrays.copyOfRange(messageBytes, 1, messageBytes.length);
        return new BigInteger(messageBytes).toString(2);
    }

    public static String convertToText(String binaryMessage) {
        byte[] messageBytes = new BigInteger(binaryMessage, 2).toByteArray();
        return new String(messageBytes, StandardCharsets.UTF_8);
    }

    static int findRandom()
    {

        // Generate the random number
        int num = (1 + (int)(Math.random() * 100)) % 2;

        // Return the generated number
        return num;
    }

    // Function to generate a random
    // binary string of length N
    static String generateBinaryString(int N)
    {

        // Stores the empty string
        String S = "";

        // Iterate over the range [0, N - 1]
        for(int i = 0; i < N; i++)
        {

            // Store the random number
            int x = findRandom();

            // Append it to the string
            S = S + String.valueOf(x);
        }

        return S;
    }

    public static class CipherText {
        private final ECP2 rP;
        private final String randomString;
        private final String messageString;

        CipherText(ECP2 rP, String randomString, String messageString) {
            this.rP = rP;
            this.randomString = randomString;
            this.messageString = messageString;
        }

        public ECP2 getrP() {
            return rP;
        }

        public String getRandomString() {
            return randomString;
        }

        public String getMessageString() {
            return messageString;
        }
    }
}
