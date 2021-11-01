package ejercicio2;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

class Observado extends Observable {
    private static Observado calculadora = new Observado();
    private int operacion;
    private double resultado;
    private Observado() {

    }
    public static Observado getInstance() {
        if (calculadora == null) {
            calculadora = new Observado();
        }
        return calculadora;
    }

    public int getOperacion() {
        return operacion;
    }

    public void setOperacion(int operacion) {
        this.operacion = operacion;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public void calcular(int operacion){

        this.operacion = operacion;
        Scanner in = new Scanner (System.in);
        int n1 = 0;
        int n2 = 0;
        switch (operacion){
            case 1: //suma
                System.out.println("Primer sumando: ");
                n1 = in.nextInt();
                System.out.println("Segundo sumando: ");
                n2 = in.nextInt();
                this.resultado =  n1+n2;
                break;
            case 2: //resta
                System.out.println("Minuendo: ");
                n1 = in.nextInt();
                System.out.println("Sustraendo: ");
                n2 = in.nextInt();
                this.resultado =  n1-n2;
                break;
            case 3: //multiplicacion
                System.out.println("Primera potencia: ");
                n1 = in.nextInt();
                System.out.println("Segunda potencia: ");
                n2 = in.nextInt();
                this.resultado =  n1*n2;
                break;
            case 4: //potencia
                System.out.println("Base: ");
                n1 = in.nextInt();
                System.out.println("Exponente: ");
                n2 = in.nextInt();
                this.resultado =  Math.pow(n1,n2);
                break;
            case 5: //raiz cuadrada
                System.out.println("Número del que quieres conocer su raíz cuadrada: ");
                n1 = in.nextInt();
                this.resultado =  Math.sqrt(n1);
                break;
            default:
                System.out.println("La operación debe ser:\n" +
                        "1 (Suma), 2 (Resta), 3 (Multiplicación), 4 (Potencia), 5 (Raíz Cuadrada)");
                break;
        }
        in.close();
        //Marcamos el objeto observable como objeto que ha cambiado
        setChanged();
        //Notificamos a los observadores y le enviamos el nuevo valor
        //notifyObservers(operacion);
        notifyObservers(resultado);
        //notifyObservers(); Este metodo solo notifica que hubo cambios en el objeto
    }
}

class Observador implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("La operacion ha sido: " + o.getClass() + ", y el resultado: " +  arg);
    }
}
public class Ejercicio2 {

    public static void main(String[] args) {
        Observado observado = Observado.getInstance();

        Observador observador1 = new Observador();
        Observador observador2 = new Observador();
        Observador observador3 = new Observador();
        Observador observador4 = new Observador();

        observado.addObserver(observador1);
        observado.addObserver(observador2);
        observado.addObserver(observador3);
        observado.addObserver(observador4);

        observado.calcular(1);
    }

}
