package ejercicio4;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Clase Cliente. Gestiona el cliente
 * */
public class Cliente {

    public static void main(String[] args) {
       MarcoCliente mimarco = new MarcoCliente();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}

/**
 * Clase MarcoCliente. Gestiona la ventana.
 * */
class MarcoCliente extends JFrame {

    public MarcoCliente() {
        setBounds(600, 300, 280, 350);
        LaminaMarcoCliente milamina = new LaminaMarcoCliente();
        add(milamina);
        setVisible(true);
    }

}
/**
 * Clase LaminaMarcoCliente. Gestiona la interfaz y sus campos.
 *
 * */
class LaminaMarcoCliente extends JPanel implements Runnable {

    //se crea un socket como atributo de la clase LaminaMarcoCliente
    Socket conn;

    public LaminaMarcoCliente() {
        //se declara y se lanza el hilo
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
    @Override
    public void run() {
        try {
            //se define el socket
            conn = new Socket("localhost", 9999);
            //se crea un paquete
            Paquete paqueteRecibido;
            while (true) {
                //se crea un flujo de entrada
                ObjectInputStream in = new ObjectInputStream(conn.getInputStream());
                //se guarda en el paquete el contenido recibido a través del flujo de entrada
                paqueteRecibido = (Paquete) in.readObject();
                //se añade el contenido al textArea campoChat
                campoChat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());

            }
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private class EnviaTexto implements ActionListener {

        //se crea un socket privado
        private Socket conn;
        private ObjectOutputStream out;
        public EnviaTexto(Socket conn) {
            this.conn = conn;
            try {
                this.out = new ObjectOutputStream(conn.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //se crea un objeto de tipo Paquete y se rellenan sus campos
                Paquete datos = new Paquete();
                datos.setNick(nick.getText());
                datos.setIp(ip.getText());
                datos.setMensaje(campo1.getText());
                if (datos.getMensaje().equals("salir")){
                    //si el cliente escribe salir, se muestra en el campo del chat "Chat finalizado"
                    campoChat.append("\n" + "Chat finalizado.");
                    //manda el mensaje de salir al servidor
                    out.writeObject(datos);
                }
                else {
                    //si no, manda el mensaje normal y vacía el campo de texto tras hacerlo
                    out.writeObject(datos);
                    campo1.setText(null);
                }



            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    private JTextField campo1, nick, ip;
    private JTextArea campoChat;
    private JButton miboton;

}