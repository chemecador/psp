package ejercicio3;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {

    /*En base a la base de datos LaLiga.sql, realizar una aplicación realizar dos aplicaciones (Cliente y
servidor) que utilizando sockets, que permitan administrar y consultar la BD. Para ello existirán
dos tipos de perfiles:
• Usuarios: podrán consultar información.
• Administradores: podrán insertar, actualizar y eliminar la información.
Para ello se trabajarán con las siguientes tablas:
• Entrenador
• Jugador
• Estadio
*/

    private ServerSocket ss;

    public Servidor() {
        try {
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

    public static void main(String[] args) {
        new Servidor();
    }

}
