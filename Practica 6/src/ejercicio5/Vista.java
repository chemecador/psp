package ejercicio5;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Clase Vista. Hereda de JFrame y crea el tablero de juego.
 */
public class Vista extends JFrame{
    private JPanel panel;
    //Botones del 1 al 9
    private JButton b00;
    private JButton b10;
    private JButton b20;
    private JButton b01;
    private JButton b11;
    private JButton b21;
    private JButton b02;
    private JButton b12;
    private JButton b22;
    //ArrayList de los 9 botones
    ArrayList<JButton> botones;

    /**
     * Constructor
     */
    public Vista(){
        //ventana
        new JFrame();
        this.setTitle("Tres en raya");
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setBackground(Color.GREEN);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        //añadimos los botones al arraylist
        botones = new ArrayList<>();
        botones.add(b00);
        botones.add(b10);
        botones.add(b20);
        botones.add(b01);
        botones.add(b11);
        botones.add(b21);
        botones.add(b02);
        botones.add(b12);
        botones.add(b22);
    }

}
