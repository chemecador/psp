package ejercicio1;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.sql.SQLOutput;

public class Hash {
    public static void main(String[] args) {
    }

    public static boolean verificarHash(String hash, String s) {
        String sHasheado = hashear(s);
        return sHasheado.equalsIgnoreCase(hash);

    }

    private static String hashear(String s) {
        //Se crea el objeto MessageDigest
        MessageDigest md = null;
        //SHA-512
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(s.getBytes());
            //Se realiza el Hashing
            byte[] mb = md.digest();
            //Se muestra por pantalla
            return new String(Hex.encodeHex(mb));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
