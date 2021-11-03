package ejercicio2;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

abstract class Observado extends Observable {
    String excepcion; //excepción que ha saltado
    private double resultado; //resultado de la operación

    //getters
    public double getResultado() {
        return resultado;
    }

    //setters
    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    //método que deben implementar las clases hijas
    public abstract void calcular();
}

class Suma extends Observado {

    //se crea una instancia
    private static Suma suma = new Suma();

    //constructor
    private Suma() {
        excepcion = ""; //por defecto, no hay excepción
    }

    //único método accesible desde fuera de la clase, devuelve la instancia
    public static Suma getInstance() {
        if (suma == null) { //si no existe...
            suma = new Suma(); //...se crea
        }
        return suma; //se devuelve la instancia
    }


    //método que realiza el cálculo de la suma
    @Override
    public void calcular() {
        try {
            Scanner in = new Scanner(System.in); //lector de teclado
            System.out.println("Primer sumando: ");
            int n1 = in.nextInt();
            System.out.println("Segundo sumando: ");
            int n2 = in.nextInt();
            super.setResultado(n1 + n2); //establece el resultado de la operación
            setChanged(); //marca el objeto observable como objeto que ha cambiado
            notifyObservers(super.getResultado()); //notifica a los observadores y le envía el nuevo valor
        } catch (Exception e) {
            this.excepcion = String.valueOf(e); //si ha saltado una excepción, la guarda en el String excepcion
            setChanged(); //marca el objeto observable como objeto que ha cambiado
            notifyObservers(excepcion); //notifica a los observadores y le envía el nuevo valor
        }
    }
}

class Resta extends Observado {

    //ídem que la suma
    private static Resta resta = new Resta();

    private Resta() {
        excepcion = "";
    }

    public static Resta getInstance() {
        if (resta == null) {
            resta = new Resta();
        }
        return resta;
    }


    @Override
    public void calcular() {

        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Minuendo: ");
            int n1 = in.nextInt();
            System.out.println("Sustraendo: ");
            int n2 = in.nextInt();
            super.setResultado(n1 - n2);
            setChanged();
            notifyObservers(super.getResultado());
        } catch (Exception e) {
            this.excepcion = String.valueOf(e);
            setChanged();
            notifyObservers(excepcion);
        }
    }
}

class Multiplicacion extends Observado {
    //ídem que las anteriores
    private static Multiplicacion multiplicacion = new Multiplicacion();

    private Multiplicacion() {
        excepcion = "";
    }

    public static Multiplicacion getInstance() {
        if (multiplicacion == null) {
            multiplicacion = new Multiplicacion();
        }
        return multiplicacion;
    }


    @Override
    public void calcular() {

        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Primer producto: ");
            int n1 = in.nextInt();
            System.out.println("Segundo producto: ");
            int n2 = in.nextInt();
            super.setResultado(n1 * n2);
            setChanged();
            notifyObservers(super.getResultado());
        } catch (Exception e) {
            this.excepcion = String.valueOf(e);
            setChanged();
            notifyObservers(excepcion);
        }
    }
}

class Potencia extends Observado {
    //ídem que las anteriores
    private static Potencia potencia = new Potencia();

    private Potencia() {
        excepcion = "";
    }

    public static Potencia getInstance() {
        if (potencia == null) {
            potencia = new Potencia();
        }
        return potencia;
    }


    @Override
    public void calcular() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Base: ");
            int n1 = in.nextInt();
            System.out.println("Exponente: ");
            int n2 = in.nextInt();
            super.setResultado(Math.pow(n1, n2));
            setChanged();
            notifyObservers(super.getResultado());
        } catch (Exception e) {
            this.excepcion = String.valueOf(e);
            setChanged();
            notifyObservers(excepcion);
        }
    }
}

class RaizCuadrada extends Observado {
    //ídem que las anteriores
    private static RaizCuadrada raiz = new RaizCuadrada();

    private RaizCuadrada() {
        excepcion = "";
    }

    public static RaizCuadrada getInstance() {
        if (raiz == null) {
            raiz = new RaizCuadrada();
        }
        return raiz;
    }


    @Override
    public void calcular() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Raíz cuadrada de: ");
            try {
                int n1 = in.nextInt();
                if (n1 < 0) { //comprueba que la raíz no sea negativa
                    excepcion = "La raíz cuadrada no puede ser negativa";
                    setChanged();
                    notifyObservers(excepcion);
                } else {
                    super.setResultado(Math.sqrt(n1));
                    setChanged();
                    notifyObservers(super.getResultado());
                }
            } catch (Exception e) {
                this.excepcion = e.getMessage();
                setChanged();
                notifyObservers(excepcion);
            }
        } catch (Exception e) {
            this.excepcion = String.valueOf(e);
            setChanged();
            notifyObservers(excepcion);
        }
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

        System.out.print("Observador " + i + ": ");
        if (arg instanceof String || arg == null) { // si ha habido una excepción...
            System.out.println("¡Error! Ha saltado esta excepción:\n" + arg + "\n");
            System.exit(0);
        } else { //si no ha habido excepciones
            System.out.println("la operación ha sido " + o.getClass().getSimpleName().toLowerCase() + ", y el resultado " + arg + "\n");
        }
    }
}

public class Ejercicio2 {

    public static void main(String[] args) {

        //se obtiene las instancias de las diferentes clases
        Suma suma = Suma.getInstance();
        Resta resta = Resta.getInstance();
        Multiplicacion multiplicacion = Multiplicacion.getInstance();
        Potencia potencia = Potencia.getInstance();
        RaizCuadrada raiz = RaizCuadrada.getInstance();

        //se crea una lista de observadores vacía
        ArrayList<Observador> observadores = new ArrayList<>();

        //se crean 4 observadores y se añaden a la lista de observadores
        for (int i = 1; i <= 4; i++) {
            Observador observador = new Observador(i);
            observadores.add(observador);
        }
        //para cada clase observada, se añaden los 4 observadores
        for (Observador observador : observadores) {
            suma.addObserver(observador);
            resta.addObserver(observador);
            multiplicacion.addObserver(observador);
            potencia.addObserver(observador);
            raiz.addObserver(observador);
        }
        /***************************************************************
         * ¡IMPORTANTE!
         * DESCOMENTAR EL MÉTODO DEL QUE SE QUIERA REALIZAR LA OPERACIÓN
         *
         ****************************************************************/

        suma.calcular();
        //resta.calcular();
        //multiplicacion.calcular();
        //potencia.calcular();
        //raiz.calcular();
    }

}
