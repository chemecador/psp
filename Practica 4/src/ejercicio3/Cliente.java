package ejercicio3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase Cliente. Gestiona el cliente
 * */
public class Cliente {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

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

    public LaminaMarcoCliente() {

        nick = new JTextField(5);
        add(nick);
        JLabel texto = new JLabel("-CHAT-");
        add(texto);
        ip = new JTextField(8);
        add(ip);
        campoChat = new JTextArea(12, 20);
        add(campoChat);
        campo1 = new JTextField(20);

        add(campo1);

        miboton = new JButton("Enviar");
        EnviaTexto miEvento = new EnviaTexto();
        miboton.addActionListener(miEvento);
        add(miboton);
        Thread miHilo = new Thread(this);
        miHilo.start();
    }

    @Override
    public void run() {
        try {
            //creamos un nuevo socket, la ip es local y el puerto 9999
            ServerSocket servidor_cliente = new ServerSocket(9090);
            Socket cliente;
            PaqueteEnvio paqueteRecibido;
            while (true) {
                //el socket cliente espera a que le llegue una petición al servidor
                cliente = servidor_cliente.accept();
                //creamos un flujo de entrada
                ObjectInputStream flujo_entrada = new ObjectInputStream(cliente.getInputStream());
                //leemos del flujo el objeto que nos llega
                paqueteRecibido = (PaqueteEnvio) flujo_entrada.readObject();
                //escribimos la información en el campo chat
                campoChat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * Clase PaqueteEnvia. Se encarga de crear una clase (paquete) con los diferentes datos
     * obtenidos en los campos de texto.
     * */
    private class EnviaTexto implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //creamos un nuevo socket, la ip es local y el puerto 9999
                Socket miSocket = new Socket("localhost", 9999);
                //creamos un Paquete con los datos que ha escrito el usuario y rellenamos sus campos
                PaqueteEnvio datos = new PaqueteEnvio();
                datos.setNick(nick.getText());
                datos.setIp(ip.getText());
                datos.setMensaje(campo1.getText());
                //creamos un flujo de salida
                ObjectOutputStream paquete_datos = new ObjectOutputStream(miSocket.getOutputStream());
                //enviamos el paquete a través del flujo
                paquete_datos.writeObject(datos);
                //cerramos el socket
                miSocket.close();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    private JTextField campo1, nick, ip;
    private JTextArea campoChat;

    private JButton miboton;

}