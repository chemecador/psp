package ejercicio4;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorEJ4 {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MarcoServidor mimarco = new MarcoServidor();

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}

class MarcoServidor extends JFrame/* implements Runnable*/ {

    public MarcoServidor() {

        setBounds(1200, 300, 280, 350);

        JPanel milamina = new JPanel();

        milamina.setLayout(new BorderLayout());

        areatexto = new JTextArea();

        milamina.add(areatexto, BorderLayout.CENTER);

        add(milamina);

        setVisible(true);

        try (ServerSocket ss = new ServerSocket(9999)) {
            while (true) {
                Socket client = ss.accept();

                new ClientHandler(client,areatexto).start();
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }

        /*Thread miHilo = new Thread(this);
        miHilo.start();*/
    }

    private JTextArea areatexto;
}
/*
    @Override
    public void run() {






        /*try {
            ServerSocket servidor = new ServerSocket(9999);
            String nick, ip, mensaje;
            PaqueteEnvio paquete_recibido;


            while (true) {
                System.out.println("entro al while");
                Socket miSocket = servidor.accept();
                System.out.println("acepto");
                ObjectInputStream paquete_datos = new ObjectInputStream(miSocket.getInputStream());
                System.out.println("estoy a punto de leer");
                paquete_recibido = (PaqueteEnvio) paquete_datos.readObject();
                System.out.println("he terminado de leer");
                nick = paquete_recibido.getNick();
                ip = paquete_recibido.getIp();
                mensaje = paquete_recibido.getMensaje();
                areatexto.append("\n" + nick + ": " + mensaje + " para " + ip);

                ObjectOutputStream paquete_reenvio = new ObjectOutputStream(miSocket.getOutputStream());
                paquete_reenvio.writeObject(paquete_recibido);
                paquete_reenvio.close();
                miSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}*/
