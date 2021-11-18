package ejercicio4;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClienteEJ4 {

    public static void main(String[] args) {
       MarcoCliente mimarco = new MarcoCliente();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}


class MarcoCliente extends JFrame {

    public MarcoCliente() {
        setBounds(600, 300, 280, 350);
        LaminaMarcoCliente milamina = new LaminaMarcoCliente();
        add(milamina);
        setVisible(true);
    }

}

class LaminaMarcoCliente extends JPanel implements Runnable {

    Socket conn;

    public LaminaMarcoCliente() {
        Thread miHilo = new Thread(this);
        miHilo.start();
        nick = new JTextField(5);
        add(nick);
        JLabel texto = new JLabel("-CHAT-");
        add(texto);
        ip = new JTextField("localhost");
        add(ip);
        campoChat = new JTextArea(12, 20);
        add(campoChat);
        campo1 = new JTextField(20);
        add(campo1);
        miboton = new JButton("Enviar");
        EnviaTexto miEvento = new EnviaTexto(conn);
        miboton.addActionListener(miEvento);
        add(miboton);

    }

    public void run() {
        try {
            conn = new Socket("localhost", 9999);
            Paquete paqueteRecibido;
            while (true) {
                ObjectInputStream in = new ObjectInputStream(conn.getInputStream());
                paqueteRecibido = (Paquete) in.readObject();
                campoChat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private class EnviaTexto implements ActionListener {

        private Socket conn;

        public EnviaTexto(Socket conn) {
            this.conn = conn;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Paquete datos = new Paquete();
                datos.setNick(nick.getText());
                datos.setIp(ip.getText());
                datos.setMensaje(campo1.getText());
                ObjectOutputStream out = new ObjectOutputStream(conn.getOutputStream());
                out.writeObject(datos);

                campo1.setText(null);
                //miSocket.close();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    private JTextField campo1, nick, ip;
    private JTextArea campoChat;

    private JButton miboton;

}