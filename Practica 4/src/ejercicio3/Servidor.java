package ejercicio3;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * Clase Servidor. Gestiona el servidor.
 * */
public class Servidor {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

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
        //se crea un nuevo hilo y se lanza
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

                //creamos otro socket
                Socket enviaDestinatario = new Socket(ip,9090);
                //creamos un flujo de salida
                ObjectOutputStream paquete_reenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
                //enviamos la información a través del flujo
                paquete_reenvio.writeObject(paquete_recibido);
                //cerramos los sockets
                paquete_reenvio.close();
                enviaDestinatario.close();
                miSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
class PaqueteEnvio implements Serializable {
    private String nick, ip, mensaje;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
