import org.miracl.core.BN254.*;
import org.miracl.core.RAND;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class Dealer {
    private final String ID;
    private final RAND rand;
    private BIG s;
    private ECP2 P;
    private ECP2 pk;
    private ECP sk;
    public Dealer(String ID) {
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
        byte[] IDByteArray = new BigInteger(Util.convertToBinary(ID)).toByteArray();
        ECP QID = BLS.bls_hash_to_point(IDByteArray);
        sk = QID.mul(s);
    }
    public static String hashFunctionH(FP12 input, int messageLength) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] inputBytes = input.toString().getBytes();
            byte[] byteHashVal = digest.digest(inputBytes);
            BigInteger hashNumber = new BigInteger(1, byteHashVal);
            StringBuilder binaryString = new StringBuilder(hashNumber.toString(2));
            String outputHash = binaryString.substring(0, messageLength);
            return outputHash;
        }
        catch (NoSuchAlgorithmException e){
            System.out.println("NO SHA-512");
            throw new RuntimeException(e);
        }
    }

    public ECP2 getP() {
        return P;
    }

    public ECP2 getPk() {
        return pk;
    }

    public ECP getSk() {
        return sk;
    }
}
