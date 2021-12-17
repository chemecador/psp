package ejercicio4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {

    private DataOutputStream out; //flujo de salida
    private DataInputStream in; //flujo de entrada
    private String t; //tablero
    private Socket socket; //socket con la conexión con el cliente
    public ClientHandler(Socket cliente) {
        try {
            //se inicializan los flujos de entrada y de salida
            out = new DataOutputStream(cliente.getOutputStream());
            in = new DataInputStream(cliente.getInputStream());
            t = "000000000T";
            //enviamos el tablero
            out.writeUTF(t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
