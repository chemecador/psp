package ejercicio1;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Observable;
import java.util.Observer;

class Observado extends Observable {
    private static Observado notificacion = new Observado(); //instancia única
    String titulo; //título de la entrada del blog

    //constructor vacío
    private Observado() {}

    //sobrecarga del constructor
    private Observado(String titulo) {
        this.titulo = titulo;
    }
    //getter
    public static Observado getInstance() {
        if (notificacion == null) {
            notificacion = new Observado();
        }
        return notificacion;
    }
    //método que notifica cuando hay una nueva entrada en el blog
    public void notificar(String titulo) {
        this.titulo = titulo;
        setChanged();
        notifyObservers(titulo);
    }


}

class Observador implements Observer {

    //método que salta cuando hay un cambio en el objeto observado
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Nuevo título de la entrada del blog: \"" + arg + "\", actualizado el " + LocalDate.now() +
                " a las " + LocalTime.now());
    }
}

public class Ejercicio1 {
    public static void main(String[] args) {

        Observado observado = Observado.getInstance(); //se obtiene la instancia única

        //se crean 3 observadores
        Observador cliente1 = new Observador();
        Observador cliente2 = new Observador();
        Observador cliente3 = new Observador();

        //se añaden 3 observadores al objeto observado
        observado.addObserver(cliente1);
        observado.addObserver(cliente2);
        observado.addObserver(cliente3);

        //se notifica una nueva entrada del blog
        observado.notificar("Cómo aprobar PSP");

    }


}
