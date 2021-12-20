package ejercicio2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase Servidor
 */
public class Servidor {
    private ServerSocket ss; //serversocket que escucha peticiones de los clientes
    private DataOutputStream out; //flujo de salida
    private DataInputStream in; //flujo de entrada
    private Socket cliente; //socket para intercambiar información con el cliente
    private String año; //año que el cliente ha solicitado consultar
    /**
     * Constructor
     */
    public Servidor() {
        try {
            //iniciamos las conexiones
            ss = new ServerSocket(5555);
            cliente = ss.accept();
            out = new DataOutputStream(cliente.getOutputStream());
            in = new DataInputStream(cliente.getInputStream());
            //el servidor siempre está escuchando
            while (true) {
                //preguntamos el año y lo guardamos en la variable año
                out.writeUTF("¿Qué año quieres consultar?");
                this.año = in.readUTF();
                //si el año no era válido...
                if (calcular(año) == null) {
                    //mostramos por pantalla la petición y notificamos de que no hay registros de ese año
                    System.out.println(año);
                    out.writeUTF("No hay registros de ese año.");
                } else {
                    //si el usuario ha elegido salir...
                    if (año.equals("salir")) {
                        //le mandamos un mensaje de despedida y salimos del bucle
                        String incremento = "¡Hasta pronto!";
                        out.writeUTF(incremento);
                        break;
                    //el usuario ha hecho una petición de año correcta...   //
                    } else {
                        //mostramos el año por pantalla y le enviamos el incremento
                        System.out.println(año);
                        String incremento = "El incremento ha sido de " + calcular(año) + ".";
                        out.writeUTF(incremento);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("IOException en Servidor\n" + ex.toString());
        }
    }

    /**
     * String que recoge un año y devuelve su correspondiente aumento, si es que lo hay.
     * @param año Año que consulta el cliente
     * @return Aumento de ese año
     */
    private static String calcular(String año) {
        //si el usuario ha escrito salir...
        if (año.equals("salir")) {
            //...devolvemos el mismo mensaje
            return año;
        }
        //creamos un Bufferedreader para leer el archivo donde se encuentra la información
        try (BufferedReader br = new BufferedReader
                (new FileReader(new File
                        ("evolucion_anual_de_la_concentracion_de_dioxido_de_carbono_(co2)_en_la_atmosferaNOAA.csv")))) {
            //línea que vamos a leer
            String linea;
            //mientras no sea null (quedan líneas por leer)
            while ((linea = br.readLine()) != null) {
                //leemos el año correspondiente a esa línea
                String lineaAño = linea.substring(0, 4);
                //si el año se corresponde con el que ha solicitado consultar el usuario...
                if (lineaAño.equals(año)) {
                    //devolvemos lo que hay a partir de ahí, que es el aumento correspondiente
                    return linea.substring(5);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ha habido un error, devolvemos null
        return null;
    }

    /**
     * Método principal
     * @param args
     */
    public static void main(String[] args) {
        new Servidor();
    }
}
