package ejercicio1;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.sql.SQLOutput;


/**
 * Clase de prueba, que cifra un texto con el algoritmo de cifrado AES.
 * AdemÃ¡s del texto cifrado se aÃ±ade la contraseÃ±a del cifrado (AES)
 * dentro del texto cifrado con el Algoritmo RSA.
 * Posteriormente se obtiene la contraseÃ±a de cifrado (AES) y se desencripta
 * el texto
 */
public class PruebaRSAconAES {

    public static void main(String[] args){

        try {
            //Se crean las claves publicas y privadas
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair par = keyGen.generateKeyPair();
            PrivateKey clavePriv = par.getPrivate();//Clave Privada
            PublicKey clavePub = par.getPublic();//Clave pÃºblica

            //Se crea la clave secreta AES
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);
            SecretKey claveSecreta = kg.generateKey();//Clave AES

            //Se encripta la clave secreta con la clave RSA
            Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c.init(Cipher.WRAP_MODE, clavePub);
            byte claveenvuelta[] = c.wrap(claveSecreta);


            //Se cifra el texto con la clave secreta
            c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE,claveSecreta);
            byte textoPlano[] = "hola".getBytes();
            byte textoCifrado[] = c.doFinal(textoPlano);

            //Se muestra por pantalla el texto
            String cifrado = new String(textoCifrado);
            System.out.println("Texto Encriptado => "+cifrado);

            /*
            Para desencriptar el texto primero se necesita
            desencriptar la clave Secreta con clave privada y a
            continuaciÃ³n desencriptar el texto con esa clave;

            Para ello se utiliza el mÃ©todo unwrap()
             */



            //Se desencripta la clave secretacon la clave privada (RSA)
            Cipher c2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            c2.init(Cipher.UNWRAP_MODE, clavePriv);
            Key clavedesenvuelta = c2.unwrap(claveenvuelta,"AES",Cipher.SECRET_KEY);

            //Desciframos el texto conla clave desenvuelta
            c2 = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c2.init(Cipher.DECRYPT_MODE, clavedesenvuelta);
            byte desencriptado[] =c2.doFinal(textoCifrado);
            String descifrado = new String(desencriptado);
            System.out.println("Texto descifrado => "+ descifrado);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
