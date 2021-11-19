package ejercicio4;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Clase Servidor. Gestiona el servidor.
 * */
public class Servidor {

    public static void main(String[] args) {
        MarcoServidor mimarco = new MarcoServidor();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
/**
 * Clase MarcoServidor. Gestiona la ventana.
 * */
class MarcoServidor extends JFrame/* implements Runnable*/ {

    public MarcoServidor() {

        setBounds(1200, 300, 280, 350);
        JPanel milamina = new JPanel();
        milamina.setLayout(new BorderLayout());
        areatexto = new JTextArea();
        milamina.add(areatexto, BorderLayout.CENTER);
        add(milamina);
        setVisible(true);

        //se crea un try catch que cierra automáticamente el socket al final
        try (ServerSocket ss = new ServerSocket(9999)) {
            //se crea un ArrayList que contiene un socket por cada cliente que ejecuta la aplicación
            ArrayList<Socket> clientes = new ArrayList<Socket>();
            while (true) {
                //se crea un socket que espera a que llegue un cliente
                Socket client = ss.accept();
                //se añade al ArrayList de sockets
                clientes.add(client);
                //se lanza un hilo de la clase ClientHandler que gestiona la conexión de manera independiente a esta clase
                new ClientHandler(client,clientes,areatexto).start();
            }
        } catch (IOException ex) {
            System.out.println("IOException en Servidor\n" + ex.toString());
        }
    }
    private JTextArea areatexto;
}

