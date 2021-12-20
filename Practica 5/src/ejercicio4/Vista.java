package ejercicio4;

import javax.swing.*;
import java.util.ArrayList;

public class Vista extends JFrame{
    JPanel panel;
    JButton b00;
    JButton b10;
    JButton b20;
    JButton b01;
    JButton b11;
    JButton b21;
    JButton b02;
    JButton b12;
    JButton b22;
    ArrayList<JButton> botones;
    public Vista(){
        new JFrame();
        setTitle("Tres en raya");
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(750,300,500,500);
        setVisible(true);

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
