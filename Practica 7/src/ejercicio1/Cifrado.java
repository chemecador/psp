package ejercicio1;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Cifrado {
    private KeyGenerator keyGenerator;
    private Key key;
    private Cipher aes;

    public Cifrado() {
        // Generamos una clave de 128 bits adecuada para AES
        keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            key = keyGenerator.generateKey();

            // Alternativamente, una clave que queramos que tenga al menos 16 bytes
            // y nos quedamos con los bytes 0 a 15
            key = new SecretKeySpec("una clave de 16 bytes".getBytes(), 0, 16, "AES");

            // Ver como se puede guardar esta clave en un fichero y recuperarla
            // posteriormente en la clase RSAAsymetricCrypto.java

            // Se obtiene un cifrador AES
            aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public String cifrar(String s) {

        try {
            // Se inicializa para encriptacion y se encripta el texto,
            // que debemos pasar como bytes.
            aes.init(Cipher.ENCRYPT_MODE, key);

            byte[] encriptado = aes.doFinal(Base64.getEncoder().encode(s.getBytes()));

            return new String(encriptado);
        } catch (
                InvalidKeyException e) {
            e.printStackTrace();
        } catch (
                IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (
                BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String descifrar(String s) {

        try {
            // Se iniciliza el cifrador para desencriptar, con la
            // misma clave y se desencripta
            aes.init(Cipher.DECRYPT_MODE, key);
            byte[] desencriptado = aes.doFinal(Base64.getDecoder().decode(s.getBytes()));
            return new String(desencriptado);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {
        Cifrado c = new Cifrado();
        String cifrado = c.cifrar("hola");
        System.out.println(cifrado);
        System.out.println(c.descifrar(cifrado));

    }
}