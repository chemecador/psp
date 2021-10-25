package ejercicio4;

import java.io.*;


class ContarPalabras extends Thread {


    public void run() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("Contitucion1812.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Hay un total de " + contador + " palabras");
    }

}

class ContarLineas extends Thread {


    public void run() {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("Contitucion1812.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int contador = 0;
        String linea;
        try {
            while ((linea = br.readLine()) != null) {
                contador++;
            }
        } catch (IOException e) {
            System.out.println("Error al leer la línea en ContarLineas");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Hay un total de " + contador + " líneas");
    }

}

class ContarTitulos extends Thread {
    public void run() {
        BufferedReader br = null;
        int contador = 0;
        try {
            br = new BufferedReader(new FileReader("Contitucion1812.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String linea;
        try {
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" "); //separamos cada línea por los espacios (vector de palabras)
                for (int i = 0; i < palabras.length; i++) {
                    if (palabras[i].equalsIgnoreCase("TITULO")) {
                        contador++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer la línea en ContarTitulos");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Hay un total de " + contador + " títulos");
    }
}

class ContarArticulos extends Thread {

    public void run() {
        BufferedReader br = null;
        int contador = 0;
        try {
            br = new BufferedReader(new FileReader("Contitucion1812.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String linea;
        try {
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" "); //separamos cada línea por los espacios (vector de palabras)
                for (int i = 0; i < palabras.length; i++) {
                    if (palabras[i].equalsIgnoreCase("ARTICULO")) {
                        contador++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer la línea en ContarArticulos");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Hay un total de " + contador + " artículos");
    }
}

class ContarCapitulos extends Thread {

    public void run() {
        BufferedReader br = null;
        int contador = 0;
        try {
            br = new BufferedReader(new FileReader("Contitucion1812.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String linea;
        try {
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" "); //separamos cada línea por los espacios (vector de palabras)
                for (int i = 0; i < palabras.length; i++) {
                    if (palabras[i].equalsIgnoreCase("CAPITULO")) {
                        contador++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer la línea en ContarArticulos");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Hay un total de " + contador + " capítulos");
    }
}

class ContarConstitucion extends Thread {

    public void run() {
        BufferedReader br = null;
        int contador = 0;
        try {
            br = new BufferedReader(new FileReader("Contitucion1812.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String linea;
        try {
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" "); //separamos cada línea por los espacios (vector de palabras)
                for (int i = 0; i < palabras.length; i++) {
                    if (palabras[i].equalsIgnoreCase("CONSTITUCION")) {
                        contador++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer la línea en ContarArticulos");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("La palabra \"constitucion\" se nombra " + contador + " veces");
    }
}

class ContarRey extends Thread {
    public void run() {
        BufferedReader br = null;
        int contador = 0;
        try {
            br = new BufferedReader(new FileReader("Contitucion1812.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String linea;
        try {
            while ((linea = br.readLine()) != null) {
                String[] palabras = linea.split(" "); //separamos cada línea por los espacios (vector de palabras)
                for (int i = 0; i < palabras.length; i++) {
                    if (palabras[i].equalsIgnoreCase("REY")) {
                        contador++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer la línea en ContarArticulos");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
