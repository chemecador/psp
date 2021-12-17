package ejercicio4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {

    private DataOutputStream out; //flujo de salida
    private DataInputStream in; //flujo de entrada
    private String t; //tablero
    private Socket socket; //socket con la conexión con el cliente
    private ArrayList<Socket> clientes;

    public ClientHandler(Socket cliente, ArrayList<Socket> clientes, int id) {
        try {
            //se inicializan los flujos de entrada y de salida
            out = new DataOutputStream(cliente.getOutputStream());
            in = new DataInputStream(cliente.getInputStream());
            this.clientes = clientes;
            out.write(id);
            t = "000000000U";
            //enviamos el tablero

            out.writeUTF(t);
            while (true) {
                //leemos el tablero
                t = in.readUTF();
                cambiarOrden(t);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String cambiarOrden(String t) {
        String nuevoT = t.substring(0, t.length() - 1);
        System.out.println("el orden es " + t.charAt(t.length() - 1));
        if (t.charAt(t.length() - 1) == 'U') {
            nuevoT += "D";
            return nuevoT;
        } else if (t.charAt(t.length() - 1) == 'D') {
            nuevoT += "U";
            return nuevoT;
        } else {
            System.err.println("El string que he recibido no contiene el turno");
            return null;
        }
    }

    //clase que envía el paquete a todos los clientes
    public void enviarATodos(String s) throws IOException {
        for (Socket conexion : clientes) {
            //se crea un flujo de salida
            out = new DataOutputStream(conexion.getOutputStream());
            //se envía el paquete a cada cliente a través del flujo
            out.writeUTF(s);
        }
    }

}
