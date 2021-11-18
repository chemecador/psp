package ejercicio4;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread{

    private final Socket conn;
    private JTextArea jta;

    public ClientHandler(Socket conn, JTextArea areatexto) {
        this.conn = conn;
        this.jta = areatexto;
    }

    @Override
    public void run() {
        try {

            ObjectInputStream in = new ObjectInputStream(conn.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(conn.getOutputStream());

            while (true) {
                String nick, ip, mensaje;
                Paquete paqueteRecibido = (Paquete) in.readObject();
               System.out.println(paqueteRecibido.getMensaje());
                nick = paqueteRecibido.getNick();
                ip = paqueteRecibido.getIp();
                mensaje = paqueteRecibido.getMensaje();
                jta.append("\n" + nick + ": " + mensaje + " para " + ip);
                System.out.println("servidor comienza a escribir");
                out.writeObject(paqueteRecibido);
                System.out.println("servidor ya ha escrito");

            }
            //conn.close();

        } catch (IOException ex) {
            System.out.println(ex.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
