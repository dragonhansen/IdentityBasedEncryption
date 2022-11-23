import org.miracl.core.BN254.BIG;
import org.miracl.core.BN254.CONFIG_CURVE;
import org.miracl.core.BN254.ROM;

import java.math.BigInteger;
import java.security.spec.ECField;
import java.security.spec.ECFieldFp;
import java.security.spec.ECPoint;
import java.util.Random;
import java.security.spec.EllipticCurve;
import java.security.MessageDigest;

public class Setup {
    private int k;
    private Random rand;
    private BigInteger p;
    private BigInteger q;
    private EllipticCurve curve;
    private BigInteger s;
    private ECPoint pub;

    public Setup(int k) {
        this.k = k;
        rand = new Random();
        generatePrimes();
        generateCurve();
        generateMasterKey();
        generatePublicKey();
    }

    private void generatePrimes() {
        do {

            p = new BigInteger(k, 100, rand);
            q = p.add(BigInteger.ONE).divide(BigInteger.valueOf(6));
            var aval = new BIG(2);
        }
        //Check the that the conditions on p and q are satisfied
        while(!p.remainder(BigInteger.valueOf(3)).equals(BigInteger.valueOf(2)) | !q.multiply(BigInteger.valueOf(6)).subtract(BigInteger.ONE).remainder(p).equals(BigInteger.ZERO) | !q.isProbablePrime(100));
    }

    private void generateCurve() {
        ECFieldFp field = new ECFieldFp(p);
        curve = new EllipticCurve(field, BigInteger.ZERO, BigInteger.ONE);
    }

    private void generateMasterKey() {
        do {
            s = new BigInteger(q.bitLength(), 0, rand);
        }
        //check that s has order q and that it is coprime to q
        while(s.compareTo(q) > -1 && !s.gcd(q).equals(BigInteger.ONE));
    }


    private void generatePublicKey() {
        //Generate a random point on the curve
        BigInteger y;
        BigInteger x;
        do{
            y = new BigInteger(q.bitLength(), 0, rand);
            x = y.pow(2).subtract(BigInteger.ONE).pow(p.multiply(BigInteger.valueOf(2)).subtract(BigInteger.ONE).divide(BigInteger.valueOf(3)).intValue());
        }
        while(y.compareTo(q) > -1 | !x.mod(BigInteger.ONE).equals(BigInteger.ZERO));
        BigInteger xPub = x.multiply(s);
        BigInteger yPub = y.multiply(s);
        pub = new ECPoint(xPub, yPub);
    }





}
