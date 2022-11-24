import org.miracl.core.BN254.ECP;
import org.miracl.core.BN254.FP12;
import org.miracl.core.BN254.PAIR;

public class Decrypter {

    public String decrypt(Util.CipherText c, ECP sk) {
        FP12 pair = PAIR.ate(c.getrP(), sk);
        pair = PAIR.fexp(pair);
        String hashVal = Dealer.hashFunctionH(pair, c.getXORVal().length());
        String binaryMessage = Util.xorBinaryString(hashVal, c.getXORVal(), hashVal.length());
        return Util.convertToText(binaryMessage);
    }
}
