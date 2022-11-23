import org.miracl.core.BN254.*;
import org.miracl.core.RAND;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class Config {
    private int messageLength;
    private String ID;
    private RAND rand;
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
        BIG r = BIG.random(rand);
        ECP2 rP = P.mul(r);
        byte[] IDByteArray = new BigInteger(ID).toByteArray();
        ECP QID = BLS.bls_hash_to_point(IDByteArray);
        FP12 gIDr = PAIR.ate(pk, QID);
        gIDr = PAIR.fexp(gIDr).pow(r);;
        System.out.println("Encrypt pair: " + gIDr);
        String hashVal = hashFunctionH(gIDr);
        return new CipherText(rP, XORBinaryString(hashVal, message, messageLength));
    }

    public String Decrypt(CipherText c) {
        FP12 pair = PAIR.ate(c.getrP(), sk);
        pair = PAIR.fexp(pair);
        System.out.println("Decrypt pair: " + pair);
        String hashVal = hashFunctionH(pair);
        return XORBinaryString(hashVal, c.getXORVal(), messageLength);
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

    public class CipherText {
        private ECP2 rP;
        private String XORVal;

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
}