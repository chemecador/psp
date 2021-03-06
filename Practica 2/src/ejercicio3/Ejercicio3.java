package ejercicio3;

import java.util.Scanner;

//Clase que realiza la primera operación
class Hilo1 extends Thread {

    private int x;
    private int y;
    private int z;
    private int resultado; //entero que almacena el resultado de la operación

    //constructor
    public Hilo1(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.resultado = 0;
    }

    //getter
    public int getResultado() {
        return resultado;
    }


    public void run() {
        try {
            Thread.currentThread().sleep(2000); //duerme el hilo durante 2s
            this.resultado = (x + y) * 2 - (4 * x + 3 * y - 2 * z); //realizar la operación
            System.out.println("El resultado 1 es: " + this.resultado);
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            System.err.println("Error en el sleep de la primera operación");
        }
    }

}

//Clase que realiza la segunda operación
class Hilo2 extends Thread {
    private int x;
    private int y;
    private int z;
    private int resultado;

    public Hilo2(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getResultado() {
        return resultado;
    }

    public void run() {
        try {
            Thread.currentThread().sleep(2000);
            this.resultado = (5 * z - 7 * y + x * 2) * 2 - (4 * x + 3 * y - 8 * z) * 2;
            System.out.println("El resultado 2 es " + this.resultado);
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            System.err.println("Error en el sleep de la segunda operación");
        }
    }

}
//Clase que realiza la suma de las dos operaciones
class Hilo3 extends Thread {

    private int a;
    private int b;

    public Hilo3(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public void run() {
        System.out.println("El resultado es: " + (a + b));
    }
}

//Clase principal
public class Ejercicio3 {
    public static void main(String[] args) {
        int x = 0, y = 0, z = 0;
        Scanner in = new Scanner(System.in);
        boolean ok = false; //booleano que controla el fin del bucle
        while (!ok) {
            try {
                System.out.println("Introduce el valor x (1-10): ");
                x = in.nextInt();
                System.out.println("Introduce el valor y (1-10): ");
                y = in.nextInt();
                System.out.println("Introduce el valor z (1-10): ");
                z = in.nextInt();
            } catch (Exception e) {
                System.err.println("Eso no es un entero.");
            }
            if (x >= 1 && x <= 10 && y >= 1 && y <= 10 && z >= 1 && z <= 10) {
                ok = true; //se cumplen todas las condiciones, fin del bucle
            } else {
                System.err.println("Valores incorrectos.");
            }
        }

        //declaración de hilos
        Hilo1 t1 = new Hilo1(x, y, z);
        Hilo2 t2 = new Hilo2(x, y, z);
        Hilo3 t3;
        //inicio de hilos
        t1.start();
        t2.start();
        try {
            //esperar a que terminen los hilos 1 y 2
            t1.join();
            t2.join();

            //cuando han terminado, comienza el hilo 3
            t3 = new Hilo3(t1.getResultado(), t2.getResultado());
            t3.start();
        } catch (InterruptedException e) {
            System.err.println("Error en los hilos");
        }
    }
}
