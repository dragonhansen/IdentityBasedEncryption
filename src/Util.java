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

    public static class CipherText {
        private final ECP2 rP;
        private final String XORVal;

        CipherText(ECP2 rP, String XORVal) {
            this.rP = rP;
            this.XORVal = XORVal;
        }

        public ECP2 getrP() {
            return rP;
        }

        public String getXORVal() {
            return XORVal;
        }
    }
}
