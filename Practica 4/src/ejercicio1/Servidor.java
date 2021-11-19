package ejercicio1;

import javax.swing.*;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase Servidor. Gestiona el servidor.
 * */
public class Servidor  {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MarcoServidor mimarco=new MarcoServidor();

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
/**
 * Clase MarcoServidor. Gestiona la ventana.
 * */
class MarcoServidor extends JFrame implements Runnable {

    public MarcoServidor(){

        setBounds(1200,300,280,350);

        JPanel milamina= new JPanel();

        milamina.setLayout(new BorderLayout());

        areatexto=new JTextArea();

        milamina.add(areatexto,BorderLayout.CENTER);

        add(milamina);

        setVisible(true);

        Thread miHilo = new Thread(this);
        miHilo.start();
    }

    private	JTextArea areatexto;

    @Override
    public void run() {
        try {
            //creamos un serversocket con el puerto 9999
            ServerSocket servidor = new ServerSocket(9999);
            while(true){
                //creamos un socket que espera a que le llegue una conexión
                Socket miSocket = servidor.accept();
                //creamos un flujo de entrada
                DataInputStream flujo_entrada = new DataInputStream(miSocket.getInputStream());
                //leemos del flujo de entrada y lo guardamos en un string
                String mensaje = flujo_entrada.readUTF();
                //lo escribimos en el área de texto
                areatexto.append("\n" + mensaje);
                //cerramos el closet
                miSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
