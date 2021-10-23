package ejercicio4;

import java.io.*;
import java.util.Scanner;

class ContarPalabras extends Thread {
    private FileReader fichero; //String que almacena la constitución
    private int contador;

    public ContarPalabras(FileReader f) {
        this.fichero = f;
        this.contador = 0;
    }

    public int getContador() {
        return contador;
    }

    public void run() {
        BufferedReader br = new BufferedReader(fichero);
        System.out.println("Hola");
        synchronized (getClass()) {
            try {
                System.out.println("Todo bien");
                String linea = br.readLine();
                while (linea != null) {
                    System.out.println("bucle");
                    this.contador++;
                }
            } catch (IOException e){
                System.out.println("Error al leer la línea");
            }
            getClass().notifyAll();
            try {
                getClass().wait();
            } catch (InterruptedException e) {
                System.err.println("Error en el wait");
            }
            getClass().notifyAll();
        }
        System.out.println("Hay un total de " + this.contador + " palabras");
        System.out.println("Debería dar 17237");
    }
}

class ContarLineas extends Thread {
    private FileReader fichero;
    private int contador;

    public ContarLineas(FileReader f) {
        this.fichero = f;
    }

    public int getContador() {
        return contador;
    }

    public void run() {

    }

}

class ContarTitulos extends Thread {
    private String c;

    public ContarTitulos(String c) {
        this.c = c;
    }

    public void run() {

    }
}

class ContarArticulos extends Thread {
    private String c;

    public ContarArticulos(String c) {
        this.c = c;
    }

    public void run() {

    }
}

class ContarCapitulos extends Thread {
    private String c;

    public ContarCapitulos(String c) {
        this.c = c;
    }

    public void run() {

    }
}

class ContarConstitucion extends Thread {
    private String c;

    public ContarConstitucion(String c) {
        this.c = c;
    }

    public void run() {

    }
}

class ContarRey extends Thread {
    private String c;

    public ContarRey(String c) {
        this.c = c;
    }

    public void run() {
        int contador = 0;

    }
}

public class Ejercicio4 {

    public static String leerFichero() {
        String c = "";
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("Contitucion1812.txt"));
            String linea;
            while ((linea = br.readLine()) != null) {
                c += linea;
                c += '\n';
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error al leer el archivo");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error al leer la línea");
            e.printStackTrace();
        }
        System.out.println(c);
        return c;
    }

    public static void main(String[] args) {
        FileReader fichero = null;
        try {
            fichero = new FileReader("Contitucion1812.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ContarPalabras cp = new ContarPalabras(fichero);
        ContarLineas cl = new ContarLineas(fichero);
        cp.start();
        try {
            cp.join();
            cl.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}
