package rezar;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Clase Servidor.
 */
public class Servidor {

    private ServerSocket ss; //serversocket que escucha continuamente la llegada de nuevos clientes

    /**
     * Constructor de la clase
     */
    public Servidor() {
        //se crea un try catch que cierra automáticamente el socket al final

        try {
            ss = new ServerSocket(9999);
            //se crea un ArrayList que contiene un socket por cada cliente que ejecuta la aplicación
            while (true) {
                //se crea un socket que espera a que llegue un cliente
                Socket cliente = ss.accept();
                //se lanza un hilo de la clase ClientHandler que gestiona la conexión de manera independiente a esta clase
                new ClientHandler(cliente).start();
            }
        } catch (SocketException se) {
            System.out.println("Conexión con el cliente cerrada.");
        } catch (IOException ex) {
            System.out.println("IOException en rezar.Servidor\n" + ex.toString());
        }
    }

    /**
     * Método principal.
     *
     * @param args
     */
    public static void main(String[] args) {
        new Servidor();
    }
}
