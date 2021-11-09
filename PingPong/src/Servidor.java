import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Servidor {
    private ServerSocket socketServidor;
    private int puerto;


    /**
     * Constructor de la clase Servidor. Esta clase inicia un servidor trivial que espera una conexión
     * de una aplicacion cliente por socket. Una vez recibida la conexion de un cliente el servidor
     * escribirá Ping, que será mostrado porn el cliente, y este le responderá PONG.
     *
     *
     * @param puerto  int que representa el puerto por el que se realizará la conexión a través de socket.
     */
    public Servidor(int puerto) {
        this.puerto = puerto;
        //Se inicia el servidor
        arrancarServidor();
    }




    /**
     * Método privado que arranca de manera automatica el servidor. Esperará que se realice la
     * conexión de una aplicación cliente
     */
    private void arrancarServidor(){

        //control de errores
        try {
            //Se crea el ServerSocket
            socketServidor = new ServerSocket(this.puerto);

            /* Se crea el socket de conexión del cliente para trabajar con él.
               En un caso más real se utilizaría para crear los canales de
               comunicacion de entrada y salida con la aplicación cliente
            */
            Socket cienteConectado = socketServidor.accept();

            //Declaramos un PrintStream para escribir datos
            PrintStream salida = new PrintStream(cienteConectado.getOutputStream());

            //Declaramos un DataInputStream para leer los datos
            DataInputStream entrada = new DataInputStream(cienteConectado.getInputStream());


            for (int i = 0; i<50; i++){
                escribir(salida);
                leer(entrada);
            }

            //Se cierra el canal de entrada
            entrada.close();
            //Se cierra el canal de salida
            salida.close();
            //Se cierra el socket
            cienteConectado.close();

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void leer(DataInputStream entrada) throws IOException {
        // Creamos un String recibido para cuando recibamos la orden del cliente
        String recibido="";

        recibido = entrada.readLine();
        System.out.println("Soy el servidor, he recibido un "+recibido);

    }
    public void escribir(PrintStream salida){

        salida.println("Pong");
    }


    public static void main(String[] args) throws IOException {
        Servidor s = new Servidor(4444);
    }


}
