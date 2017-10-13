import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.util.Scanner;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Hex;

public class AsymmetricCipherTest {
    private static byte[] encrypt(byte[] inpBytes, PublicKey key, String xform) throws Exception {
        Cipher cipher = Cipher.getInstance(xform);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(inpBytes);
    }
    private static byte[] decrypt(byte[] inpBytes, PrivateKey key, String xform) throws Exception{
        Cipher cipher = Cipher.getInstance(xform);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(inpBytes);
    }

    public static void main(String[] unused) throws Exception {
        while(true){
            String xform = "RSA/ECB/PKCS1PADDING";
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(512);
            KeyPair kp = kpg.generateKeyPair();
            PublicKey pubk = kp.getPublic();
            PrivateKey prvk = kp.getPrivate();

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Text to Encrypt with Public Key or Q to Exit: ");
            String input = sc.nextLine();

            if(input.equals("q")||input.equals("Q")){
                break;
            }
            else
            {
                byte[] dataBytes = input.getBytes();
                byte[] encBytes = encrypt(dataBytes, pubk, xform);

                String cipherT = String.valueOf(Hex.encodeHex(encBytes));
                System.out.println("Cipher Text : "+cipherT);


                System.out.print("Enter Cipher Text to Decrypt with Private Key : ");
                String cInput = sc.nextLine();
                try{
                    byte[] cipher = Hex.decodeHex(cInput.toCharArray());
                    byte[] decBytes = decrypt(cipher, prvk, xform);

                    String decoded = new String(decBytes, "UTF-8");
                    System.out.println(decoded);

                    boolean expected = java.util.Arrays.equals(dataBytes, decBytes);
                    System.out.println("Decryption " + (expected ? "SUCCEEDED!" : "FAILED!"));
                }
                catch(Exception e)
                {
                    System.out.println("Invalid Cipher Text!");
                    System.out.println("Decryption FAILED!");
                }
            }
        }
    }
}