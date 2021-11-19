package ejercicio2;

import javax.swing.*;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
class MarcoServidor extends JFrame implements Runnable {

    public MarcoServidor() {

        setBounds(1200, 300, 280, 350);

        JPanel milamina = new JPanel();

        milamina.setLayout(new BorderLayout());

        areatexto = new JTextArea();

        milamina.add(areatexto, BorderLayout.CENTER);

        add(milamina);

        setVisible(true);

        Thread miHilo = new Thread(this);
        miHilo.start();
    }

    private JTextArea areatexto;

    @Override
    public void run() {
        try {
            //creamos un serversocket con el puerto 9999
            ServerSocket servidor = new ServerSocket(9999);
            String nick, ip, mensaje;
            //creamos un paquete
            PaqueteEnvio paquete_recibido;

            while (true) {
                //creamos un socket que espera a que le llegue una conexión
                Socket miSocket = servidor.accept();
                //creamos un flujo de entrada
                ObjectInputStream paquete_datos = new ObjectInputStream(miSocket.getInputStream());
                //leemos del flujo de entrada y lo guardamos en un Paquete
                paquete_recibido = (PaqueteEnvio) paquete_datos.readObject();
                nick = paquete_recibido.getNick();
                ip = paquete_recibido.getIp();
                mensaje = paquete_recibido.getMensaje();
                //lo escribimos en el área de texto
                areatexto.append("\n" + nick + ": " + mensaje + " para " + ip);
                //cerramos el socket
                miSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
