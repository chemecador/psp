package ejercicio1;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Observable;
import java.util.Observer;

class Observado extends Observable {
    private static Observado notificacion = new Observado();
    String titulo;

    private Observado() {

    }

    private Observado(String titulo) {
        this.titulo = titulo;
    }

    public static Observado getInstance() {
        if (notificacion == null) {
            notificacion = new Observado();
        }
        return notificacion;
    }

    public void notificar(String titulo) {
        this.titulo = titulo;
        //Marcamos el objeto observable como objeto que ha cambiado
        setChanged();
        //Notificamos a los observadores y le enviamos el nuevo valor
        notifyObservers(titulo);
        //notifyObservers(); Este metodo solo notifica que hubo cambios en el objeto
    }


}

class Observador implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Nuevo título de la entrada del blog: \"" + arg + "\", actualizado el " + LocalDate.now() +
                " a las " + LocalTime.now());
    }
}

public class Ejercicio1 {
    public static void main(String[] args) {
        Observado observado = Observado.getInstance();

        Observador cliente1 = new Observador();
        Observador cliente2 = new Observador();
        Observador cliente3 = new Observador();

        observado.addObserver(cliente1);
        observado.addObserver(cliente2);
        observado.addObserver(cliente3);


        observado.notificar("Cómo aprobar PSP");

    }


}
