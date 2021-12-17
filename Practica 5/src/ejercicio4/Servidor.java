package ejercicio4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 *
 * enviar a TODOS los clientes su ID
 *
 *
 */


public class Servidor {
    private ServerSocket ss;
    private DataOutputStream out;
    private DataInputStream in;

    public Servidor() {
        try {
            //se inicializa el serversocket
            ss = new ServerSocket(5555);
            ArrayList<Socket> clientes;
            int i = 1;
            while (true) {
                clientes = new ArrayList<>();
                //se crea un socket que espera a que llegue un cliente
                Socket cliente = ss.accept();
                //se lanza un hilo de la clase ClientHandler que gestiona la conexión de manera independiente a esta clase
                new ClientHandler(cliente,clientes,i).start();
                i++;
                if (i == 3){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Servidor();
    }
}
