import org.miracl.core.BN254.*;
import org.miracl.core.RAND;

import java.math.BigInteger;

public class Encrypter {

    private RAND rand;

    public Encrypter() {
        rand = new RAND();
    }

    public Util.CipherText encrypt(String message, String ID, ECP2 P, ECP2 pk) {
        String binaryMessage = Util.convertToBinary(message);
        BIG.random(rand);
        BIG r = BIG.random(rand);
        ECP2 rP = P.mul(r);
        byte[] IDByteArray = new BigInteger(Util.convertToBinary(ID)).toByteArray();
        ECP QID = BLS.bls_hash_to_point(IDByteArray);
        FP12 gID = PAIR.ate(pk, QID);
        FP12 gIDr = PAIR.fexp(gID).pow(r);
        String hashVal = Dealer.hashFunctionH(gIDr, message.length()*8-1);
        return new Util.CipherText(rP, Util.xorBinaryString(hashVal, binaryMessage, hashVal.length()));
    }
}
