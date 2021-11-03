package ejercicio3;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

class Observado extends Observable {

    private static Observado termostato = new Observado(); //instancia única
    int temperatura; //temperatura general

    //getter
    public static Observado getInstance() {
        if (termostato == null) {
            termostato = new Observado();
        }
        return termostato;
    }

    //método que permite cambiar la temperatura
    public void cambiarTemperatura() {
        Scanner in = new Scanner(System.in);
        System.out.println("Introduce la temperatura: ");
        this.temperatura = in.nextInt();
        //Marcamos el objeto observable como objeto que ha cambiado
        setChanged();
        //Notificamos a los observadores y le enviamos el nuevo valor
        notifyObservers(temperatura);
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

        //se crean 4 observadores y se añaden a la lista de observadores
        for (int i = 1; i <= 4; i++) {
            Observador observador = new Observador(i);
            observadores.add(observador);
        }
        //para cada clase observada, se añaden los 4 observadores
        for (Observador observador : observadores) {
            observado.addObserver(observador);
        }

        observado.cambiarTemperatura(); //se invoca el método para cambiar la temperatura
    }
}
