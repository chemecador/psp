package ejercicio4;

import java.io.*;


class ContarPalabras extends Thread {
    private BufferedReader br; //String que almacena la constitución
    private int contador; //entero que almacena el número de palabras

    //constructor
    public ContarPalabras(BufferedReader br) {
        this.br = br;
        this.contador = 0;
    }

    //getter
    public int getContador() {
        return contador;
    }

    public void run() {
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
        }

        System.out.println("Hay un total de " + this.contador + " palabras");
    }
}

class ContarLineas extends Thread {
    private BufferedReader br;
    private int contador;

    public ContarLineas(BufferedReader br) {
        this.br = br;
        this.contador = 0;
    }

    public int getContador() {
        return contador;
    }

    public void run() {
        String linea;
        try {
            System.out.println(br.readLine());
            while ((linea = br.readLine()) != null) {
                contador++;
                System.out.println(contador);
            }
        } catch (IOException e) {
            System.out.println("Error al leer la línea en ContarLineas");
        }

        System.out.println("Hay un total de " + this.contador + " palabras");
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
        BufferedReader br = new BufferedReader(fichero);
        ContarPalabras cp = new ContarPalabras(br);
        ContarLineas cl = new ContarLineas(br);
        cp.start();
        try {
            cp.join();
            br.mark(1);
            br.reset();
            cl.start();
            cl.join();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            System.out.println("No he podido cerrar el br");
            e.printStackTrace();
        }
    }
}
