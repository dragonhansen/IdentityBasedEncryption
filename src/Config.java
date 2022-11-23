import org.miracl.core.BN254.*;
import org.miracl.core.RAND;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.Arrays;

public class Config {
    private final int messageLength;
    private final String ID;
    private final RAND rand;
    private BIG s;
    private ECP2 P;
    private ECP2 pk;
    private ECP sk;
    public Config(String ID, int messageLength) {
        this.messageLength = messageLength;
        this.ID = ID;
        rand = new RAND();
        generateMasterKey();
        generatePublicKey();
        generatePrivateKey();
    }

    private void generateMasterKey() {
        rand.sirand(3454);
        //for some reason s becomes 0 if this extra call is not made
        BIG.random(rand);
        s = BIG.random(rand);
    }
    private void generatePublicKey() {
        BIG randomNumber = BIG.random(rand);
        P = ECP2.generator().mul(randomNumber);
        pk = P.mul(s);
    }

    private void generatePrivateKey() {
        byte[] IDByteArray = new BigInteger(ID).toByteArray();
        ECP QID = BLS.bls_hash_to_point(IDByteArray);
        sk = QID.mul(s);
    }

    public CipherText Encrypt(String message) {
        String binaryMessage = convertToBinary(message);
        BIG r = BIG.random(rand);
        ECP2 rP = P.mul(r);
        byte[] IDByteArray = new BigInteger(ID).toByteArray();
        ECP QID = BLS.bls_hash_to_point(IDByteArray);
        FP12 gID = PAIR.ate(pk, QID);
        FP12 gIDr = PAIR.fexp(gID).pow(r);
        String hashVal = hashFunctionH(gIDr);
        return new CipherText(rP, XORBinaryString(hashVal, binaryMessage, messageLength));
    }

    public String Decrypt(CipherText c) {
        FP12 pair = PAIR.ate(c.getrP(), sk);
        pair = PAIR.fexp(pair);
        String hashVal = hashFunctionH(pair);
        String binaryMessage = XORBinaryString(hashVal, c.getXORVal(), messageLength);
        return convertToText(binaryMessage);
    }

    private String hashFunctionH(FP12 input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] inputBytes = input.toString().getBytes();
            byte[] byteHashVal = digest.digest(inputBytes);
            BigInteger hashNumber = new BigInteger(1, byteHashVal);
            StringBuilder binaryString = new StringBuilder(hashNumber.toString(2));
            String outputHash = binaryString.substring(0, messageLength);
            return outputHash;
        }
        catch (NoSuchAlgorithmException e){
            System.out.println("NO SHA-256");
            throw new RuntimeException(e);
        }
    }

    public static class CipherText {
        private final ECP2 rP;
        private final String XORVal;

        private CipherText(ECP2 rP, String XORVal) {
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

    private String XORBinaryString(String a, String b, int n){
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

    private String convertToBinary(String message) {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        if(messageBytes[0] == 0 && message.charAt(0) == '1') messageBytes = Arrays.copyOfRange(messageBytes, 1, messageBytes.length);
        String binaryMessage = new BigInteger(messageBytes).toString(2);
        return binaryMessage;
    }

    private String convertToText(String binaryMessage) {
        byte[] messageBytes = new BigInteger(binaryMessage, 2).toByteArray();
        String message = new String(messageBytes, StandardCharsets.UTF_8);
        return message;
    }

}
