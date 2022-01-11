package rezar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

/**
 * Clase Cliente.
 */
public class Cliente {
    private Socket conn; //conexión con el servidor
    private DataInputStream in; //flujo de entrada
    private DataOutputStream out; //flujo de salida
    private String s; //string que contiene los mensajes que se envían y reciben
    private static Scanner sc; //scanner para leer de teclado

    /**
     * Constructor de la clase
     */
    public Cliente() {
        try {
            //iniciar conexiones
            conn = new Socket("localhost", 9999);
            in = new DataInputStream(conn.getInputStream());
            out = new DataOutputStream(conn.getOutputStream());
            sc = new Scanner(System.in);

            while (true) {
                //leer pregunta de rezar
                System.out.println("\n" + in.readUTF());
                //enviar respuesta
                out.writeUTF(sc.nextLine());
                //leer la primera línea del rezo
                s = in.readUTF();
                System.out.println("\n" + s);
                //si no hay errores...
                if (!s.substring(0, 6).equalsIgnoreCase("Error.")) {
                    //leo las otras 3
                    System.out.println(in.readUTF());
                    System.out.println(in.readUTF());
                    System.out.println(in.readUTF());
                }
            }
        } catch (SocketException se) {
            System.out.println("Se ha perdido la conexión con el servidor.");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Cliente();
    }
}
