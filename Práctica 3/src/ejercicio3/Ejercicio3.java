package ejercicio3;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


class Slider extends Observable{
    static JSlider slider; /*se deben instanciar antes para poder usarlos en el método creado mas abajo */
    static JLabel label; /*y a la vez estaticos (static) también para poder ser usados en nuestro método creado. */
    static JButton aceptar;
    public int getValue(){
        return slider.getValue();
    }

    public Slider() {

        JFrame ventana = new JFrame("Termostato");
        ventana.setSize(400, 400);
        JPanel panel = new JPanel();
        aceptar = new JButton("OK");
        aceptar.addActionListener(this::actionPerformed);
        slider = new JSlider(JSlider.VERTICAL, 15, 25, 21);
        slider.setInverted(false);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.addChangeListener(new Termostato());
        label = new JLabel("Introduce la temperatura");
        panel.add(label);
        panel.add(aceptar);
        panel.add(slider);
        ventana.add(panel);
        ventana.setVisible(true);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==aceptar) {
            System.exit(0);
        }
    }

    public class Termostato implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            int grados = slider.getValue();
            String s = Integer.toString(grados);
            label.setText(s);
        }
    }

}

class Observado extends Observable {

    private static Observado termostato = new Observado(); //instancia única
    private static Slider termoSlider;
    int temperatura; //temperatura general
    Slider slider;

    private Observado() {
        this.termoSlider = new Slider();
        this.temperatura = 0;
    }

    //getter
    public static Observado getInstance() {
        if (termostato == null) {
            termostato = new Observado();
        }
        return termostato;
    }
}

class Observador implements Observer {

    private int i; //índice del observador

    //constructor
    public Observador(int i) {
        this.i = i;
    }

    //método que salta cuando hay un cambio en el objeto observado
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Aparato " + i + ": " + " la temperatura es de " + arg);
    }
}

public class Ejercicio3 {
    public static void main(String[] args) {
        Observado observado = Observado.getInstance(); //se obtiene la instancia única
        ArrayList<Observador> observadores = new ArrayList<>(); //se crea una lista de observadores vacía
        Slider miSlider = new Slider();
        //se crean 4 observadores y se añaden a la lista de observadores
        for (int i = 1; i <= 4; i++) {
            Observador observador = new Observador(i);
            observadores.add(observador);
        }
        //para cada clase observada, se añaden los 4 observadores
        for (Observador observador : observadores) {
            observado.addObserver(observador);
            miSlider.addObserver(observador);
        }

        //observado.cambiarTemperatura(); //se invoca el método para cambiar la temperatura
    }
}
