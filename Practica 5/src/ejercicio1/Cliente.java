package ejercicio1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    private final int PUERTO = 5555;
    private final String HOST = "localhost";
    private Socket conn;
    private DataInputStream in;
    private DataOutputStream out;
    private String mensaje;

    public Cliente() {
        //se define el socket
        try {
            conn = new Socket(HOST, PUERTO);
            in = new DataInputStream(conn.getInputStream());
            out = new DataOutputStream(conn.getOutputStream());
            while (true) {
                //se guarda en el mensaje
                mensaje = in.readUTF();
                System.out.println(mensaje);
                String año = pedirDatos();
                out.writeUTF(año);
                mensaje = in.readUTF();
                System.out.println(mensaje);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String pedirDatos() {
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    public static void main(String[] args) {
        new Cliente();
    }
}