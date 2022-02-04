package ejercicio4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase Servidor
 */
public class Servidor {

    private ServerSocket ss; //ServerSocket que escucha la llegada de nuevos clientes

    //constructor
    public Servidor() {
        try {
            //se inicializa el serversocket
            ss = new ServerSocket(5555);
            while (true) {
                //se crea un socket que espera a que llegue un cliente
                Socket cliente = ss.accept();
                //se lanza un hilo de la clase ClientHandler que gestiona la conexión de manera independiente a esta clase
                new ClientHandler(cliente).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Método principal
     * @param args
     */
    public static void main(String[] args) {
        new Servidor();
    }

}
