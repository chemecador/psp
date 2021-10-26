package ejercicio4;

import java.io.*;


class ContarPalabras extends Thread {


    public void run() {
        int contador = -1; //si al finalizar el programa sigue siendo -1, ha habido un error

        //intentamos leer del fichero y cierra el BufferedReader cuando lo consigue
        try (BufferedReader br = new BufferedReader(new FileReader("Contitucion1812.txt"));) {
            contador = 0; //lo ha leído con éxito, ponemos el contador a 0
            String linea;
            while ((linea = br.readLine()) != null) {  //leemos cada línea hasta el final del fichero
                String[] palabras = linea.split(" "); //separamos cada línea por los espacios (vector de palabras)
                if (palabras.length > 2) { //quitamos las líneas que solo contienen un salto de línea
                    contador += palabras.length; //sumamos la palabra al contador
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer la línea en ContarPalabras");
        }

        System.out.println("Hay un total de " + contador + " palabras");
    }

}

class ContarLineas extends Thread {


    public void run() {
        int contador = -1;
        try (BufferedReader br = new BufferedReader(new FileReader("Contitucion1812.txt"));) {
            contador = 0;
            String linea;
            while ((linea = br.readLine()) != null) {
                contador++; //sumamos 1 al contador por cada línea que lee
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer la línea en ContarLíneas");
        }
        System.out.println("Hay un total de " + contador + " líneas");
    }

}

class ContarTitulos extends Thread {
    public void run() {
        int contador = -1;
        try (BufferedReader br = new BufferedReader(new FileReader("Contitucion1812.txt"));) {
            contador = 0;
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" ");
                for (int i = 0; i < palabras.length; i++) { //recorremos el vector de palabras
                    if (palabras[i].equalsIgnoreCase("TITULO")) {
                        contador++; //suma 1 al contador cada vez que encuentra la palabra "titulo"
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer la línea en ContarTítulos");
        }
        System.out.println("Hay un total de " + contador + " títulos");
    }
}

class ContarArticulos extends Thread {

    public void run() {

        int contador = -1;
        try (BufferedReader br = new BufferedReader(new FileReader("Contitucion1812.txt"));) {
            contador = 0;
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" ");
                for (int i = 0; i < palabras.length; i++) {
                    if (palabras[i].equalsIgnoreCase("ARTICULO")) {
                        contador++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer la línea en ContarArtículos");
        }
        System.out.println("Hay un total de " + contador + " artículos");
    }
}

class ContarCapitulos extends Thread {

    public void run() {
        int contador = -1;
        try (BufferedReader br = new BufferedReader(new FileReader("Contitucion1812.txt"));) {
            contador = 0;
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" ");
                for (int i = 0; i < palabras.length; i++) {
                    if (palabras[i].equalsIgnoreCase("CAPITULO")) {
                        contador++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer la línea en ContarCapítulos");
        }
        System.out.println("Hay un total de " + contador + " capítulos");
    }
}

class ContarConstitucion extends Thread {

    public void run() {
        int contador = -1;
        try (BufferedReader br = new BufferedReader(new FileReader("Contitucion1812.txt"));) {
            contador = 0;
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" ");
                for (int i = 0; i < palabras.length; i++) {
                    if (palabras[i].equalsIgnoreCase("CONSTITUCION")) {
                        contador++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer la línea en ContarConstitucion");
        }
        System.out.println("La palabra \"constitucion\" se nombra " + contador + " veces");
    }
}

class ContarRey extends Thread {
    public void run() {
        int contador = -1;
        try (BufferedReader br = new BufferedReader(new FileReader("Contitucion1812.txt"));) {
            contador = 0;
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" ");
                for (int i = 0; i < palabras.length; i++) {
                    if (palabras[i].equalsIgnoreCase("REY")) {
                        contador++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el archivo");
        } catch (IOException e) {
            System.err.println("Error al leer la línea en ContarRey");
        }
        System.out.println("La palabra \"rey\" se nombra " + contador + " veces");
    }
}

public class Ejercicio4 {

    public static void main(String[] args) {

        ContarPalabras cp = new ContarPalabras();
        ContarLineas cl = new ContarLineas();
        ContarTitulos ct = new ContarTitulos();
        ContarArticulos ca = new ContarArticulos();
        ContarCapitulos cca = new ContarCapitulos();
        ContarConstitucion cco = new ContarConstitucion();
        ContarRey cr = new ContarRey();
        cp.start();
        try {
            cp.join();
            cl.start();
            cl.join();
            ct.start();
            ct.join();
            ca.start();
            ca.join();
            cca.start();
            cca.join();
            cco.start();
            cco.join();
            cr.start();
            cr.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
