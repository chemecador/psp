package rezar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Clase que gestiona la interacción Cliente-Servidor.
 */
public class ClientHandler extends Thread {

    private DataOutputStream out; //flujo de salida
    private DataInputStream in; //flujo de entrada
    private String rezos; //string que contiene los rezos disponibles
    private String s; //string que contiene los mensajes que se envían y reciben

    public ClientHandler(Socket cliente) {
        try {
            //iniciar conexión con el cliente
            out = new DataOutputStream(cliente.getOutputStream());
            in = new DataInputStream(cliente.getInputStream());
            System.out.println("Conexión establecida con un nuevo cliente.");
            rezos = "¿Qué quieres rezar?\n1. Padre nuestro\n2. Ave María\n3. Santa Cruz";

        } catch (SocketException se) {
            System.out.println("Conexión con el cliente cerrada.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que gestiona la respuesta del servidor.
     * @param s Rezo que quiere leer el cliente
     * @throws IOException
     */
    private void devolverRezo(String s) throws IOException {
        if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("padre nuestro")) {
            out.writeUTF("Padre nuestro que estás en el cielo, santificado sea tu Nombre; venga a nosotros tu Reino;");
            out.writeUTF("hágase tu voluntad en la tierra como en el cielo. Danos hoy nuestro pan de cada día;");
            out.writeUTF("perdona nuestras ofensas, como también nosotros perdonamos a los que nos ofenden;");
            out.writeUTF("no nos dejes caer en la tentación, y líbranos del mal. Amén.");
        } else if (s.equalsIgnoreCase("2") || s.equalsIgnoreCase("ave maria")
                || s.equalsIgnoreCase("ave maría")) {
            out.writeUTF("Dios te salve, María, llena eres de gracia; el Señor es contigo");
            out.writeUTF("Bendita Tú eres entre todas las mujeres, y bendito es el fruto de tu vientre, Jesús.");
            out.writeUTF("Santa María, Madre de Dios, ruega por nosotros, pecadores,");
            out.writeUTF("ahora y en la hora de nuestra muerte. Amén");

        } else if (s.equalsIgnoreCase("3") || s.equalsIgnoreCase("santa cruz")) {
            out.writeUTF("Por la señal + de la Santa Cruz, ");
            out.writeUTF("de nuestros + enemigos líbranos Señor,");
            out.writeUTF("+ Dios nuestro. En el nombre del Padre, y del + Hijo, y del Espíritu Santo.");
            out.writeUTF("Amén.");
        } else {
            out.writeUTF("Error. Ese rezo no está disponible");
        }
    }

    public void run() {
        try {
            while (true) {
                out.writeUTF(rezos);
                s = in.readUTF();
                devolverRezo(s);
            }
        } catch (SocketException se) {
            System.out.println("Conexión con el cliente cerrada.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
