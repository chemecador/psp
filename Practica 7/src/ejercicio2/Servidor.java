package ejercicio2;

import com.google.gson.Gson;
import ejercicio1.Cifrado;

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
    private String s; //año que el cliente ha solicitado consultar
    private Mensaje m; //variable de tipo Mensaje
    private Gson gson;//creamos una variable gson de la librería Gson
    private String json; //variable de tipo string que contendrá un json
    private Cifrado c; //variable de tipo Cifrado para cifrar los mensajes
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
            m = new Mensaje(); //inicializamos el mensaje
            c = new Cifrado(); //inicializamos la librería Cifrado
            gson = new Gson(); //inicializamos la librería Gson
            while (true) {
                //enviamos la pregunta del año dentro del atributo año del Mensaje m
                m.setAño("¿Qué año quieres consultar?");
                //creamos un string json donde convertimos el Mensaje m en un json con la librería gson
                String json = gson.toJson(m);
                //enviamos el json, cifrado con la librería Cifrado
                out.writeUTF(c.cifrar(json));
                //leemos la respuesta del cliente y lo desciframos con la librería Cifrado
                s = c.descifrar(in.readUTF());
                //guardamos en la variable Mensaje m el contenido del json (String s) gracias al método fromJson de la librería gson
                m = gson.fromJson(s, Mensaje.class);
                //si el año no era válido...
                if (calcular(m.getAño()) == null) {
                    //mostramos por pantalla la petición y notificamos de que no hay registros de ese año
                    System.out.println(m.getAño());
                    //escribimos en el atributo año que no hay registros de ese año
                    m.setAño("No hay registros de ese año.");
                    //guardamos en la variable json el Mensaje m convertido a json con el método toJson
                    json = gson.toJson(m);
                    //lo enviamos al cliente, cifrado con la librería Cifrado
                    out.writeUTF(c.cifrar(json));
                } else {
                    //si el usuario ha elegido salir...
                    if (m.getAño().equals("salir")) {
                        //le mandamos un mensaje de despedida y salimos del bucle
                        m.setAño("¡Hasta pronto!");
                        json = gson.toJson(m);
                        //lo enviamos al cliente, cifrado con la librería Cifrado
                        out.writeUTF(c.cifrar(json));
                        break;
                        //el usuario ha hecho una petición de año correcta...   //
                    } else {
                        //mostramos el año por pantalla y le enviamos el incremento
                        System.out.println(m.getAño());
                        m.setAño("El incremento ha sido de " + calcular(m.getAño()) + ".");
                        json = gson.toJson(m);
                        //lo enviamos al cliente, cifrado con la librería Cifrado
                        out.writeUTF(c.cifrar(json));
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("IOException en Servidor\n" + ex.toString());
        }
    }

    /**
     * String que recoge un año y devuelve su correspondiente aumento, si es que lo hay.
     *
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
                        ("incremento_de_la_temperatura_global2.csv")))) {
            //línea que vamos a leer
            String linea;
            //mientras no sea null (quedan líneas por leer)
            while ((linea = br.readLine()) != null) {
                //leemos el año correspondiente a esa línea
                String lineaAño = linea.substring(0, 4);
                //si el año se corresponde con el que ha solicitado consultar el usuario...
                if (lineaAño.equals(año)) {
                    //devolvemos el aumento
                    return linea.substring(7, linea.length() - 1);
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
     *
     * @param args
     */
    public static void main(String[] args) {
        new Servidor();
    }
}
