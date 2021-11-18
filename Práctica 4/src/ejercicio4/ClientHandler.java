package ejercicio4;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread{

    private final Socket conn;
    private ArrayList<Socket> clientes;
    private JTextArea jta;

    public ClientHandler(Socket conn, ArrayList<Socket> clientes, JTextArea areatexto) {
        this.conn = conn;
        this.clientes = clientes;
        this.jta = areatexto;
    }

    public void enviarATodos(Paquete paquete) throws IOException {
        for (Socket conexion : clientes) {

            ObjectOutputStream out = new ObjectOutputStream(conexion.getOutputStream());

            out.writeObject(paquete);
        }
    }

    @Override
    public void run() {
        try {

            ObjectInputStream in = new ObjectInputStream(conn.getInputStream());

            while (true) {
                String nick, ip, mensaje;
                Paquete paqueteRecibido = (Paquete) in.readObject();
               System.out.println(paqueteRecibido.getMensaje());
                nick = paqueteRecibido.getNick();
                ip = paqueteRecibido.getIp();
                mensaje = paqueteRecibido.getMensaje();
                jta.append("\n" + nick + ": " + mensaje + " para " + ip);
                enviarATodos(paqueteRecibido);

            }
            //conn.close();

        } catch (IOException ex) {
            System.out.println(ex.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
