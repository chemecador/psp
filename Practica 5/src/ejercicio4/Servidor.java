package ejercicio4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * TODO: gestionar empate, el ultimo jugador se queda esperando a que tiren
 */


public class Servidor {
    private ServerSocket ss;
    private DataOutputStream out1; //flujo de salida
    private DataOutputStream out2;
    private DataInputStream in1; //flujo de entrada
    private DataInputStream in2;
    private String t; //tablero
    private ArrayList<Socket> clientes;

    public Servidor() {
        try {
            //se inicializa el serversocket
            ss = new ServerSocket(5555);
            clientes = new ArrayList<>();

            Socket cliente1 = ss.accept();
            in1 = new DataInputStream(cliente1.getInputStream());
            out1 = new DataOutputStream(cliente1.getOutputStream());
            clientes.add(cliente1);

            Socket cliente2 = ss.accept();
            in2 = new DataInputStream(cliente2.getInputStream());
            out2 = new DataOutputStream(cliente2.getOutputStream());
            clientes.add(cliente2);

            enviarATodos("jugar");
            gestionarConexion();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void gestionarConexion() {
        try {
            //enviamos el id a cada cliente
            out1.writeUTF("1");
            out2.writeUTF("2");
            t = "0000000001";
            //enviamos el tablero
            enviarATodos(t);
            int turno = 1;
            while (true) {
                //leemos el tablero
                if (turno == 1) {
                    t = in1.readUTF();
                    turno++;
                } else if (turno == 2) {
                    t = in2.readUTF();
                    turno--;
                }
                System.out.println("tablero recibido: " + t);
                int r = comprobarFinal();
                if (r != 0) {
                    System.out.println("r = " + r);
                    resultado(r);
                    break;
                }
                enviarATodos(cambiarOrden(t));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String cambiarOrden(String t) {
        String nuevoT = t.substring(0, t.length() - 1);
        System.out.println("el orden es " + t.charAt(t.length() - 1));
        if (t.charAt(t.length() - 1) == '1') {
            nuevoT += "2";
            return nuevoT;
        } else if (t.charAt(t.length() - 1) == '2') {
            nuevoT += "1";
            return nuevoT;
        } else {
            System.err.println("El string que he recibido no contiene el turno");
            return null;
        }
    }

    //clase que envía el paquete a todos los clientes
    private void enviarATodos(String s) throws IOException {
        for (Socket soc : clientes) {
            //se crea un flujo de salida
            DataOutputStream out = new DataOutputStream(soc.getOutputStream());
            //se envía el paquete a cada cliente a través del flujo
            out.writeUTF(s);
        }
    }

    private int comprobarFinal() {
        //comprobar filas
        for (int i = 0; i < t.length(); i++) {
        }
        if (t.charAt(0) == t.charAt(1) && t.charAt(0) == t.charAt(2)) {
            if (t.charAt(0) != '0') {
                return Character.getNumericValue(t.charAt(0));
            }
        } else if (t.charAt(3) == t.charAt(4) && t.charAt(3) == t.charAt(5)) {
            if (t.charAt(3) != '0') {
                return Character.getNumericValue(t.charAt(3));
            }
        } else if (t.charAt(6) == t.charAt(7) && t.charAt(6) == t.charAt(8)) {
            if (t.charAt(6) != '0') {
                return Character.getNumericValue(t.charAt(6));
            }
        }
        //comprobar columnas
        if (t.charAt(0) == t.charAt(3) && t.charAt(0) == t.charAt(6)) {
            if (t.charAt(0) != '0') {
                return Character.getNumericValue(t.charAt(0));
            }
        } else if (t.charAt(1) == t.charAt(4) && t.charAt(1) == t.charAt(7)) {
            if (t.charAt(1) != '0') {
                return Character.getNumericValue(t.charAt(1));
            }
        } else if (t.charAt(2) == t.charAt(5) && t.charAt(2) == t.charAt(8)) {
            if (t.charAt(2) != '0') {
                return Character.getNumericValue(t.charAt(2));
            }

        }

        //comprobar diagonales
        if (t.charAt(0) == t.charAt(4) && t.charAt(0) == t.charAt(8)) {
            if (t.charAt(0) != '0') {
                return Character.getNumericValue(t.charAt(0));
            }
        } else if (t.charAt(2) == t.charAt(4) && t.charAt(2) == t.charAt(6)) {
            if (t.charAt(2) != '0') {
                return Character.getNumericValue(t.charAt(2));
            }
        }
        if (lleno()) {
            return 3;
        }
        return 0;
    }

    private void resultado(int r) {
        if (r == 1) {
            //JOptionPane.showMessageDialog(null,"","",JOptionPane.PLAIN_MESSAGE);
            System.out.println("Gana el jugador 1");
            try {
                out1.writeUTF("W");
                out2.writeUTF("L");
                out2.writeUTF(t);
            } catch (IOException e) {
                System.out.println("Error. No se ha podido notificar de la victoria 1");
                e.printStackTrace();
            }
        } else if (r == 2) {
            System.out.println("Gana el jugador 2");
            try {
                out1.writeUTF("L");
                out1.writeUTF(t);
                out2.writeUTF("W");
            } catch (IOException e) {
                System.out.println("Error. No se ha podido notificar de la victoria 2");
                e.printStackTrace();
            }
        } else if (r == 3) {
            System.out.println("Empate");
            try {
                out1.writeUTF("D");
                out2.writeUTF("D");
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.out.println("Error. No se ha podido notificar del empate");
                e.printStackTrace();
            }
        }
    }

    private boolean lleno() {
        if (t.indexOf('0') == -1){
            System.out.println("está lleno");
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        new Servidor();
    }
}
