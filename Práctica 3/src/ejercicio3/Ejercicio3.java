package ejercicio3;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Clase que contiene un Slider (interfaz gráfica del termostato).
 * Es observable por los objetos observadores (los 4 aparatos).
 */
class Slider extends Observable {
    //se crean tres instancias, deben ser estáticas para poder ser accesibles desde fuera de la clase
    static JSlider slider;
    static JLabel label;
    static JButton aceptar;

    //getter para obtener el valor final de la temperatura
    public int getValue() {
        return slider.getValue();
    }

    //constructor
    public Slider() {
        JFrame ventana = new JFrame("Termostato"); //se declara una nueva ventana
        JPanel panel = new JPanel(); //se declara un nuevo panel
        ventana.setSize(400, 400); //se establece el tamaño
        slider = new JSlider(JSlider.VERTICAL, 15, 25, 21); //se define la forma que tiene la ventana
        slider.setInverted(true); //temperaturas frías arriba, cálidas abajo
        slider.setPaintTicks(true); //se pintan los ticks intermedios
        slider.setMajorTickSpacing(5); //se dejan 5 ticks entre número y número (15-20-25)
        slider.setMinorTickSpacing(1); //cada tick representa un grado
        slider.setPaintLabels(true); //pintar las etiquetas
        slider.addChangeListener(new Termostato()); //añade el listener que escuche cada vez que se realiza un cambio en el slider
        label = new JLabel(); //etiqueta vacía que mostrará posteriormente el valor de la temperatura
        aceptar = new JButton("OK"); //botón de aceptar
        aceptar.addActionListener(this::actionPerformed); //listener del botón de aceptar

        //se añaden la etiqueta, el slider y el botón de aceptar al panel
        panel.add(label);
        panel.add(slider);
        panel.add(aceptar);

        ventana.add(panel); //se añade el panel a la ventana
        ventana.setVisible(true); //se muestra
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //se cierra el programa al darle a la X
    }
    //se ha realizado una acción (evento e)
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == aceptar) { //si se ha pulsado el botón de aceptar...
            setChanged(); //establece el objeto observable como objeto que ha cambiado
            notifyObservers(slider.getValue()); //notifica a los observadores que ha cambiado el objeto observado (el valor de la temperatura)
            System.exit(0); //se cierra el programa
        }
    }
    /**
     * Clase que implementa el listener que avisará cada vez que se produzca un cambio en el slider.
     * Mostrará en una etiqueta el valor de la temperatura.
     * */
    public class Termostato implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            int grados = slider.getValue(); //obtiene el valor de la temperatura
            String s = Integer.toString(grados); //lo convierte a string
            label.setText(s); //lo muestra en la etiqueta de la ventana
        }
    }

}
/**
 * Clase que define al objeto observado. Contiene una única instancia del termostato y su temperatura.
 * No pueden crearse más termostatos (patrón de diseño Singleton).
 *
 * */
class Observado extends Observable {

    private static Observado termostato = new Observado(); //instancia única
    int temperatura; //temperatura general

    //constructor
    private Observado() {
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
/**
 * Clase observadora que contiene un único índice para distinguir a los aparatos.
 *
 * */
class Observador implements Observer {

    private int i; //índice del observador

    //constructor
    public Observador(int i) {
        this.i = i;
    }

    //método que salta cuando hay un cambio en el objeto observado
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Aparato " + i + ": la temperatura ahora es de " + arg);
    }
}
/**
 * Clase principal. Contiene el main.
 *
 * */
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
    }
}
