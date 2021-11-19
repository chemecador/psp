package ejercicio1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
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

        JLabel texto=new JLabel("CLIENTE");

        add(texto);

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
                //creamos un flujo de salida
                DataOutputStream flujo_salida = new DataOutputStream(miSocket.getOutputStream());
                //mandamos por el flujo el contenido que hay en el campo de text
                flujo_salida.writeUTF(campo1.getText());
                //cerramos el flujo
                flujo_salida.close();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }





    private JTextField campo1;

    private JButton miboton;

}