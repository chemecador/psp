package ejercicio4;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {

    //atributos de la clase
    private final Socket conn;
    private ArrayList<Socket> clientes;
    private JTextArea jta;
    private ObjectOutputStream out;
    public ClientHandler(Socket conn, ArrayList<Socket> clientes, JTextArea areatexto) {
        this.conn = conn;
        this.clientes = clientes;
        this.jta = areatexto;
    }

    //clase que envía el paquete a todos los clientes
    public void enviarATodos(Paquete paquete) throws IOException {
        for (Socket conexion : clientes) {
            //se crea un flujo de salida
            out = new ObjectOutputStream(conexion.getOutputStream());
            //se envía el paquete a cada cliente a través del flujo
            out.writeObject(paquete);
        }
    }

    @Override
    public void run() {
        try {
            //se crea un flujo de entrada
            ObjectInputStream in = new ObjectInputStream(conn.getInputStream());
            String nick, ip, mensaje;
            Paquete paqueteRecibido;
            while (true) {
                //se crea un paquete y se rellenan sus campos
                paqueteRecibido = (Paquete) in.readObject();
                nick = paqueteRecibido.getNick();
                ip = paqueteRecibido.getIp();
                mensaje = paqueteRecibido.getMensaje();
                if (mensaje.equalsIgnoreCase("salir")) {
                    //si el cliente ha salido, lo muestra en el textArea del servidor
                    jta.append("\n" + nick + " ha abandonado el chat.");
                    //cierra la conexión con el cliente
                    conn.close();
                    //elimina el socket del ArrayList
                    clientes.remove(conn);
                    //modifica el mensaje para notificar que este cliente ha abandonado el chat
                    paqueteRecibido.setMensaje(nick + " ha abandonado el chat. ");
                    //notifica al resto de usuarios de que ha abandonado el chat
                    enviarATodos(paqueteRecibido);
                    break;
                }
                else {
                    //se añade el contenido del paquete al textArea
                    jta.append("\n" + nick + ": " + mensaje + " para " + ip);
                    //se invoca al método que envía a todos los clientes
                    enviarATodos(paqueteRecibido);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException en Clienthandler\n" + e.toString());
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException en ClientHandler\n" + e.toString());
        }
    }

}
