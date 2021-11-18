package ejercicio4;

import java.io.*;

//Clase que cuenta el número de palabras del fichero "Contitucion1812.txt"
class ContarPalabras extends Thread {

    public void run() {

        //intentamos leer del fichero y cierra el BufferedReader cuando lo consigue
        try (BufferedReader br = new BufferedReader(new FileReader("Contitucion1812.txt"));) {
            int contador = 0; //inicializar el contador de palabras
            String linea;
            while ((linea = br.readLine()) != null) {  //leemos cada línea hasta el final del fichero
                String[] palabras = linea.split(" "); //separamos cada línea por los espacios (vector de palabras)
                if (palabras.length > 2) { //quitamos las líneas que solo contienen un salto de línea
                    contador += palabras.length; //sumamos la palabra al contador
                }
            }
            System.out.println("Hay un total de " + contador + " palabras");
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer la línea en ContarPalabras");
        }

    }

}

//Clase que cuenta el número de líneas del fichero "Contitucion1812.txt"
class ContarLineas extends Thread {

    public void run() {
        //intentamos leer del fichero y cierra el BufferedReader cuando lo consigue
        try (BufferedReader br = new BufferedReader(new FileReader("Contitucion1812.txt"));) {
            int contador = 0; //inicializar el contador de palabras
            String linea;
            while ((linea = br.readLine()) != null) {
                contador++; //sumamos 1 al contador por cada línea que lee
            }
            System.out.println("Hay un total de " + contador + " líneas");
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer la línea en ContarLíneas");
        }
    }

}

//Clase que cuenta el número de títulos del fichero "Contitucion1812.txt"
class ContarTitulos extends Thread {

    public void run() {

        //intentamos leer del fichero y cierra el BufferedReader cuando lo consigue
        try (BufferedReader br = new BufferedReader(new FileReader("Contitucion1812.txt"));) {
            int contador = 0; //inicializar el contador de palabras
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" ");
                for (int i = 0; i < palabras.length; i++) { //recorremos el vector de palabras
                    if (palabras[i].equalsIgnoreCase("TITULO")) {
                        contador++; //suma 1 al contador cada vez que encuentra la palabra "titulo"
                    }
                }
            }
            System.out.println("Hay un total de " + contador + " títulos");
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer la línea en ContarTítulos");
        }
    }
}

//Clase que cuenta el número de artículs del fichero "Contitucion1812.txt"
class ContarArticulos extends Thread {

    public void run() {

        //intentamos leer del fichero y cierra el BufferedReader cuando lo consigue
        try (BufferedReader br = new BufferedReader(new FileReader("Contitucion1812.txt"));) {
            int contador = 0; //inicializar el contador de palabras
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" ");
                for (int i = 0; i < palabras.length; i++) {
                    if (palabras[i].equalsIgnoreCase("ARTICULO")) {
                        contador++; //suma 1 al contador cada vez que encuentra la palabra "articulo"
                    }
                }
            }
            System.out.println("Hay un total de " + contador + " artículos");
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer la línea en ContarArtículos");
        }

    }
}

//Clase que cuenta el número de capítulos del fichero "Contitucion1812.txt"
class ContarCapitulos extends Thread {

    public void run() {
        //intentamos leer del fichero y cierra el BufferedReader cuando lo consigue
        try (BufferedReader br = new BufferedReader(new FileReader("Contitucion1812.txt"));) {
            int contador = 0; //inicializar el contador de palabras
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" ");
                for (int i = 0; i < palabras.length; i++) {
                    if (palabras[i].equalsIgnoreCase("CAPITULO")) {
                        contador++; //suma 1 al contador cada vez que encuentra la palabra "capitulo"
                    }
                }
            }
            System.out.println("Hay un total de " + contador + " capítulos");
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer la línea en ContarCapítulos");
        }
    }
}

//Clase que cuenta el número de veces que aparece la palabra "Constitucion" en el fichero "Contitucion1812.txt"
class ContarConstitucion extends Thread {

    public void run() {
        //intentamos leer del fichero y cierra el BufferedReader cuando lo consigue
        try (BufferedReader br = new BufferedReader(new FileReader("Contitucion1812.txt"));) {
            int contador = 0; //inicializar el contador de palabras
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" ");
                for (int i = 0; i < palabras.length; i++) {
                    if (palabras[i].equalsIgnoreCase("CONSTITUCION")) {
                        contador++; //suma 1 al contador cada vez que encuentra la palabra "constitucion"
                    }
                }
            }
            System.out.println("La palabra \"constitucion\" se nombra " + contador + " veces");
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer la línea en ContarConstitucion");
        }
    }
}

//Clase que cuenta el número de veces que aparece la palabra "Constitucion" en el fichero "Contitucion1812.txt"
class ContarRey extends Thread {

    public void run() {
        //intentamos leer del fichero y cierra el BufferedReader cuando lo consigue
        try (BufferedReader br = new BufferedReader(new FileReader("Contitucion1812.txt"));) {
            int contador = 0; //inicializar el contador de palabras
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" ");
                for (int i = 0; i < palabras.length; i++) {
                    if (palabras[i].equalsIgnoreCase("REY")) {
                        contador++; //suma 1 al contador cada vez que encuentra la palabra "rey"
                    }
                }
            }
            System.out.println("La palabra \"rey\" se nombra " + contador + " veces");
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer la línea en ContarRey");
        }
    }
}

public class Ejercicio4 {

    public static void main(String[] args) {

        //creación los hilos
        ContarPalabras cp = new ContarPalabras();
        ContarLineas cl = new ContarLineas();
        ContarTitulos ct = new ContarTitulos();
        ContarArticulos ca = new ContarArticulos();
        ContarCapitulos cca = new ContarCapitulos();
        ContarConstitucion cco = new ContarConstitucion();

        ContarRey cr = new ContarRey();

        //inicio de hilos
        cp.start();
        cl.start();
        ct.start();
        ca.start();
        cca.start();
        cco.start();
        cr.start();
        try {
            //esperar a que acaben para que se muestre ordenado
            cp.join();
            cl.join();
            ct.join();
            ca.join();
            cca.join();
            cco.join();
            cr.join();
        } catch (InterruptedException e) {
            System.err.println("Error en el join");
        }
    }
}
