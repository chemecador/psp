package ejercicio2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Clase Cliente. Gestiona el cliente
 * */
public class Cliente {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MarcoCliente mimarco=new MarcoCliente();

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}

/**
 * Clase MarcoCliente. Gestiona la ventana.
 * */
class MarcoCliente extends JFrame{

    public MarcoCliente(){

        setBounds(600,300,280,350);

        LaminaMarcoCliente milamina=new LaminaMarcoCliente();

        add(milamina);

        setVisible(true);
    }

}
/**
 * Clase LaminaMarcoCliente. Gestiona la interfaz y sus campos.
 *
 * */
class LaminaMarcoCliente extends JPanel{

    public LaminaMarcoCliente(){

        nick = new JTextField(5);
        add(nick);
        JLabel texto=new JLabel("-CHAT-");
        add(texto);
        ip = new JTextField(8);
        add(ip);
        campoChat = new JTextArea(12,20);
        add(campoChat);
        campo1=new JTextField(20);

        add(campo1);

        miboton=new JButton("Enviar");
        EnviaTexto miEvento = new EnviaTexto();
        miboton.addActionListener(miEvento);
        add(miboton);

    }
    /**
     * Clase EnviaTexto. Se encarga de enviar el contenido de los diferentes campos de texto
     * cuando se pulsa el botón Enviar.
     * */
    private class EnviaTexto implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //creamos un nuevo socket, la ip es local y el puerto 9999
                Socket miSocket = new Socket("localhost",9999);
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
/**
 * Clase PaqueteEnvia. Se encarga de crear una clase (paquete) con los diferentes datos
 * obtenidos en los campos de texto.
 * */
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