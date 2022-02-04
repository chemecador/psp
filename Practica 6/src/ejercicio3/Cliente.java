package ejercicio3;

import com.google.gson.Gson;
import ejercicio2.Mensaje;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Clase Cliente.
 */
public class Cliente {
    private final int PUERTO = 5555; //puerto del servidor
    private final String HOST = "localhost"; //dirección del servidor
    private Socket conn; //socket para conectarse al servidor
    private DataInputStream in; //flujo de entrada de datos del servidor
    private DataOutputStream out; //flujo de salida de datos del servidor
    private String s; //año que queremos consultar
    private Mensaje m; //variable del tipo Mensaje
    private Gson gson; //variable de tipo Gson, librería de Google para trabajar con json
    private String json; //variable de tipo String que almacenará los datos de un json

    public Cliente() {
        //se define el socket
        try {
            //inicializamos la conexión
            conn = new Socket(HOST, PUERTO);
            in = new DataInputStream(conn.getInputStream());
            out = new DataOutputStream(conn.getOutputStream());
            gson = new Gson(); //inicializamos la librería Gson
            m = new Mensaje(); //inicializamos el Mensaje
            Scanner sc = new Scanner(System.in); //creamos un scanner para leer por teclado
            while (true) {
                // leemos en pantalla el mensaje recibido
                s = in.readUTF();
                //guardamos en la variable Mensaje m el contenido del json (String s) gracias al método fromJson de la librería gson
                m = gson.fromJson(s, Mensaje.class);
                //lo mostramos por pantalla
                System.out.println(m.getAño());

                //pedimos por teclado el año que queremos consultar y lo guardamos en el atributo año del Mensaje m
                m.setAño(sc.nextLine());
                //convertimos el Mensaje m a un json con el método toJson
                json = gson.toJson(m);
                //se lo enviamos al servidor en formato json
                out.writeUTF(json);
                //leemos la respuesta del servidor
                s = in.readUTF();
                //guardamos en la variable Mensaje m el contenido del json (String s) gracias al método fromJson de la librería gson
                m = gson.fromJson(s, Mensaje.class);
                //lo mostramos por pantalla
                System.out.println(m.getAño());
                //si el servidor se ha despedido de nosotros...
                if (m.getAño().equals("¡Hasta pronto!")) {
                    //...salimos del bucle para cerrar la aconexión
                    break;
                }
            }
            System.out.println("Conexión con el servidor cerrada");
            //cerramos la conexión
            conn.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Método principal
     *
     * @param args
     */
    public static void main(String[] args) {
        new Cliente();
    }
}