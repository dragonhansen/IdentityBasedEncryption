import org.miracl.core.BN254.*;

public class Decrypter {

    public String decrypt(Util.CipherText c, ECP sk, ECP2 P) {
        int messageLength = c.getMessageString().length();
        FP12 pair = PAIR.ate(c.getrP(), sk);
        pair = PAIR.fexp(pair);
        String hashVal = Dealer.hashFunctionH2(pair, messageLength);
        String sigma = Util.xorBinaryString(c.getRandomString(), hashVal, messageLength);
        String message = Util.xorBinaryString(c.getMessageString(), Dealer.hashFunctionH4(sigma, messageLength), messageLength);
        BIG r = Dealer.hashFunctionH3(sigma, message);
        if(c.getrP().equals(P.mul(r))) {
            return Util.convertToText(message);
        }
        return "Ciphertext rejected";
    }
}
