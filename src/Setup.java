import java.math.BigInteger;
import java.security.spec.ECField;
import java.security.spec.ECFieldFp;
import java.security.spec.ECPoint;
import java.util.Random;
import java.security.spec.EllipticCurve;

public class Setup {
    private int k;
    private Random rand;
    private BigInteger p;
    private BigInteger q;
    private EllipticCurve curve;
    private BigInteger s;

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
        }
        //Check that p mod 3 = 2
        while(!p.remainder(BigInteger.valueOf(3)).equals(BigInteger.valueOf(2)));
        do {
            q = p.add(BigInteger.ONE).divide(BigInteger.valueOf(6));
        }
        //Check that p=6q-1
        while(!q.multiply(BigInteger.valueOf(6)).subtract(BigInteger.ONE).remainder(p).equals(BigInteger.ZERO));
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
            x = y.pow(2).subtract(BigInteger.ONE).pow(1/3).mod(p);
        }
        while(y.compareTo(q) > -1);
        if(y.pow(2).mod(p).equals(x.pow(3).add(BigInteger.ONE).mod(p))) {
            System.out.println("Success");
        }
        //System.out.println("y: " + y + ", x: " + x);
    }




}
