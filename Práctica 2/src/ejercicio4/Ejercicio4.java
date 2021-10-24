package ejercicio4;

import java.io.*;


class ContarPalabras extends Thread {


    public void run() {
        BufferedReader br = Ejercicio4.leerFichero();
        int contador = 0;
        String linea;
        try {
            while ((linea = br.readLine()) != null) {  //leemos cada línea hasta el final del fichero
                String[] palabras = linea.split(" "); //separamos cada línea por los espacios (vector de palabras)
                if (palabras.length > 2) { //quitamos las líneas que solo contienen un salto de línea
                    contador += palabras.length; //sumamos la palabra al contador
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer la línea en ContarPalabras");
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Hay un total de " + contador + " palabras");
    }

}

class ContarLineas extends Thread {


    public void run() {

        BufferedReader br = Ejercicio4.leerFichero();
        int contador = 0;
        String linea;
        try {
            while ((linea = br.readLine()) != null) {
                contador++;
            }
        } catch (IOException e) {
            System.out.println("Error al leer la línea en ContarLineas");
        } finally{
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Hay un total de " + contador + " líneas");
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

    public static BufferedReader leerFichero() {
        FileReader fichero = null;
        try {
            fichero = new FileReader("Contitucion1812.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(fichero);
        return br;
    }

    public static void main(String[] args) {
        FileReader fichero = null;
        try {
            fichero = new FileReader("Contitucion1812.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //BufferedReader br1 = new BufferedReader(fichero);
        //BufferedReader br2 = new BufferedReader(fichero);
        ContarPalabras cp = new ContarPalabras();
        ContarLineas cl = new ContarLineas();
        cp.start();
        try {
            cp.join();
            cl.start();
            cl.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /*
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
*/
}
