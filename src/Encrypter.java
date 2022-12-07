import org.miracl.core.BN254.*;
import org.miracl.core.RAND;
import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;

public class Encrypter {

    private RAND rand;
    private Random random;

    public Encrypter() {
        rand = new RAND();
        random = new Random();
    }

    public Util.CipherText encrypt(String message, String ID, ECP2 P, ECP2 pk) {
        String binaryMessage = Util.convertToBinary(message);
        int messageLength = message.length()*8-1;
        String sigma = Util.generateBinaryString(messageLength);
        BIG r = Dealer.hashFunctionH3(sigma, binaryMessage);
        ECP2 rP = P.mul(r);
        byte[] IDByteArray = new BigInteger(Util.convertToBinary(ID)).toByteArray();
        ECP QID = BLS.bls_hash_to_point(IDByteArray);
        FP12 gID = PAIR.ate(pk, QID);
        FP12 gIDr = PAIR.fexp(gID).pow(r);
        String randomString = Util.xorBinaryString(sigma, Dealer.hashFunctionH2(gIDr, messageLength), messageLength);
        String messageString = Util.xorBinaryString(binaryMessage, Dealer.hashFunctionH4(sigma, messageLength), messageLength);
        return new Util.CipherText(rP, randomString, messageString);
    }
}
