public class Main {
    public static void main(String[] args) {
        String ID = "au618478@uni.au.dk";
        Dealer dealer = new Dealer(ID);
        Encrypter encrypter = new Encrypter();
        Decrypter decrypter = new Decrypter();
        String message = "ThisMessageIsEncryptedUsingIBE";
        Util.CipherText c = encrypter.encrypt(message, ID, dealer.getP(), dealer.getPk());
        String decryptedMessage = decrypter.decrypt(c, dealer.getSk(), dealer.getP());
        System.out.println("Input message : " + message);
        System.out.println("Output message: " + decryptedMessage);
    }
}