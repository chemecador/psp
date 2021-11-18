package ejercicio4;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServidorEJ4 {

    public static void main(String[] args) {
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
            ArrayList<Socket> clientes = new ArrayList<Socket>();
            while (true) {
                Socket client = ss.accept();
                clientes.add(client);
                new ClientHandler(client,clientes,areatexto).start();
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }

    }

    private JTextArea areatexto;
}

