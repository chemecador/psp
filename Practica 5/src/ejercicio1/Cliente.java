package ejercicio1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Clase Cliente.
 */
public class Cliente {
    private final int PUERTO = 5555; //puerto del servidor
    private final String HOST = "localhost"; //dirección del servidor
    private Socket conn; //socket para conectarse al servidor
    private DataInputStream in; //flujo de entrada de datos del servidor
    private DataOutputStream out; //flujo de salida de datos del servidor
    private String año; //año que queremos consultar

    public Cliente() {
        //se define el socket
        try {
            //inicializamos la conexión
            conn = new Socket(HOST, PUERTO);
            in = new DataInputStream(conn.getInputStream());
            out = new DataOutputStream(conn.getOutputStream());
            while (true) {
                //lo leemos en pantalla el mensaje recibido
                System.out.println(in.readUTF());
                //pedimos por teclado el año que queremos consultar
                pedirDatos();
                //se lo enviamos al servidor
                out.writeUTF(this.año);
                System.out.println(in.readUTF());
                if (año.equals("salir")){
                    break;
                }
            }
            //cerramos la conexión
            conn.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Método que recoge de teclado el año que queremos consultar
     */
    private void pedirDatos() {
        //scanner para leer de teclado
        Scanner in = new Scanner(System.in);
        //lo guardamos en el atributo año
        this.año = in.nextLine();
    }

    /**
     * Método principal
     * @param args
     */
    public static void main(String[] args) {
        new Cliente();
    }
}