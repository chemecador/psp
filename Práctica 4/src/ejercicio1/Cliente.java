package ejercicio1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Cliente {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MarcoCliente mimarco=new MarcoCliente();

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}


class MarcoCliente extends JFrame{

    public MarcoCliente(){

        setBounds(600,300,280,350);

        LaminaMarcoCliente milamina=new LaminaMarcoCliente();

        add(milamina);

        setVisible(true);
    }

}

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

    private class EnviaTexto implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Socket miSocket = new Socket("localhost",9999);
                DataOutputStream flujo_salida = new DataOutputStream(miSocket.getOutputStream());

                flujo_salida.writeUTF(campo1.getText());
                flujo_salida.close();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }





    private JTextField campo1;

    private JButton miboton;

}