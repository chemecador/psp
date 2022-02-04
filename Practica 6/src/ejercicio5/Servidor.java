package ejercicio5;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Clase Servidor
 */
public class Servidor {
    private ServerSocket ss; //ServerSocket que escucha las llegadas de clientes
    private DataOutputStream out1; //flujo de salida de datos con el jugador 1 (a partir de ahora, J1)
    private DataOutputStream out2;//flujo de salida con el jugador 2 (a partir de ahora, J2)
    private DataInputStream in1; //flujo de entrada de datos con J1
    private DataInputStream in2; //flujo de entrada con J1
    private String t; //tablero
    private ArrayList<Socket> jugadores; //lista de jugadores
    private Tablero tablero; //objeto de tipo Tablero que servirá para enviarle a cada cliente el tablero
    private String json; //String que contiene el json que será enviado entre clientes
    private Gson gson; //librería Gson para trabajar con datos json

    /**
     * Constructor de la clase
     */
    public Servidor() {
        try {
            //se inicializa el serversocket y el arraylist de jugadores
            ss = new ServerSocket(5555);
            jugadores = new ArrayList<>();
            tablero = new Tablero();
            gson = new Gson();
            //primer socket que registra la llegada del primer jugador
            Socket jugador1 = ss.accept();

            //se inicializan los flujos de entrada y salida
            in1 = new DataInputStream(jugador1.getInputStream());
            out1 = new DataOutputStream(jugador1.getOutputStream());

            //se añade el jugador al arraylist
            jugadores.add(jugador1);


            //lo mismo
            Socket jugador2 = ss.accept();
            in2 = new DataInputStream(jugador2.getInputStream());
            out2 = new DataOutputStream(jugador2.getOutputStream());
            jugadores.add(jugador2);

            enviarATodos("jugar");
            gestionar();
            //bucle infinito, el servidor está siempre en ejecución
            while (true) {
                //mandamos el mensaje "jugar" a todos los jugadores. Comienza la partida
                enviarATodos("jugar");
                //llamamos al método que se encarga de gestionar la partida
                gestionar();
            }
        } catch (SocketException se) {
            System.out.println("Conexión con el cliente cerrada.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Método que se encarga de gestionar la partida
     */
    private void gestionar() {
        try {
            //enviamos a cada cliente su id
            tablero.setT("1");
            json = gson.toJson(tablero);
            out1.writeUTF(json);
            tablero.setT("2");
            json = gson.toJson(tablero);
            out2.writeUTF(json);
            //se elige quién comienza de manera aleatoria
            int turno = (int) Math.floor(Math.random() * 2 + 1);
            enviarATodos(String.valueOf(turno));
            //se crea el tablero, todas las casillas a 0, con el turno al final
            t = "000000000" + turno;
            //enviamos el tablero
            enviarATodos(t);
            //creamos la variable r que controlará si ha habido ganador
            int r = comprobarFinal();
            //mientras no lo haya...
            while (r == 0) {
                //si le toca al j1...
                if (turno == 1) {
                    //...leemos el tablero tras su jugada
                    t = in1.readUTF();
                    tablero = gson.fromJson(t,Tablero.class);
                    t = tablero.getT();
                    //cambio de turno
                    turno++;
                } else if (turno == 2) {
                    t = in2.readUTF();
                    tablero = gson.fromJson(t,Tablero.class);
                    t = tablero.getT();
                    turno--;
                } else {
                    System.err.println("Ha ocurrido un error inesperado. Por favor, contacta con el desarrollador.");
                    System.exit(0);
                }
                //enviamos a los jugadores el nuevo tablero
                enviarATodos(cambiarOrden(t));
                //comprobamos si ha habido ganador
                r = comprobarFinal();
            }
            //si lo ha habido, llamamos al método que se encarga de gestionarlo
            resultado(r);
        } catch (SocketException se) {
            System.out.println("Conexión con el cliente cerrada.");
        } catch (IOException e) {
            System.out.println("Conexión cerrada con el cliente.");
        }
    }

    /**
     * Método que cambia el turno de un tablero
     *
     * @param t Tablero antes de cambiarlo
     * @return String con el nuevo tablero
     */
    private String cambiarOrden(String t) {
        //creamos un nuevo string que contiene los mismos caracteres excepto el último
        String nuevoT = t.substring(0, t.length() - 1);
        //si el turno era del J1...
        if (t.charAt(t.length() - 1) == '1') {
            //...le concatenamos al nuevo string que cambia el turno...
            nuevoT += "2";
            //...y devolvemos el string
            return nuevoT;
        } else if (t.charAt(t.length() - 1) == '2') {
            //idem
            nuevoT += "1";
            return nuevoT;
        } else {
            //nunca debería llegar a aquí, ha habido un error, devolvemos null
            System.err.println("El string que he recibido no contiene el turno");
            return null;
        }
    }


    /**
     * Clase que envía el tablero a todos los jugadores
     *
     * @param s Tablero a enviar
     * @throws IOException
     */
    private void enviarATodos(String s) throws IOException {
        //recorremos el arraylist de jugadores
        for (Socket soc : jugadores) {
            //se crea un flujo de salida
            DataOutputStream out = new DataOutputStream(soc.getOutputStream());
            tablero.setT(s);
            json = gson.toJson(tablero);
            //se envía el paquete a cada cliente a través del flujo
            System.out.println("el json que envio es" + json);
            out.writeUTF(json);
        }
    }

    /**
     * Método que comprueba si ha habido ganador
     *
     * @return 0 (no ha habido), 1 (ha ganado el jugador 1), 2 (ha ganado el jugador 2), 3 (empate)
     */
    private int comprobarFinal() {
        //comprobar filas
        if (t.charAt(0) == t.charAt(1) && t.charAt(0) == t.charAt(2)) {
            if (t.charAt(0) != '0') {
                //hay 3 iguales y no son 0, devolvemos su valor numérico
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
        //no hemos encontrado ganador todavía, comprobamos si quedan casillas libres
        if (lleno()) {
            //el tablero está lleno y no ha habido ganador : ha habido empate
            return 3;
        }
        //no hay ganador y quedan casillas libres, devolvemos 0 y se sigue jugando
        return 0;
    }

    /**
     * Método que es invocado cuando ha habido ganador. Gestiona qué tiene que ocurrir después.
     *
     * @param r Código que contiene el ganador : 0 (no ha habido), 1 (ha ganado el jugador 1), 2 (ha ganado el jugador 2), 3 (empate)
     */
    private void resultado(int r) {
        //ha ganado el jugador 1
        if (r == 1) {
            System.out.println("Gana el jugador 1");
            try {
                tablero.setT("W");
                json = gson.toJson(tablero);
                out1.writeUTF(json);
                tablero.setT("L");
                json = gson.toJson(tablero);
                out2.writeUTF(json);
                tablero.setT(t);
                json = gson.toJson(tablero);
                out2.writeUTF(json);
            } catch (SocketException se) {
                System.out.println("Conexión con el cliente cerrada.");
            } catch (IOException e) {
                System.err.println("Error. No se ha podido notificar de la victoria 1");
                e.printStackTrace();
            }
        } else if (r == 2) {
            System.out.println("Gana el jugador 2");
            try {
                tablero.setT("L");
                json = gson.toJson(tablero);
                out1.writeUTF(json);
                tablero.setT("W");
                json = gson.toJson(tablero);
                out2.writeUTF(json);
                tablero.setT(t);
                json = gson.toJson(tablero);
                out1.writeUTF(json);
            } catch (SocketException se) {
                System.out.println("Conexión con el cliente cerrada.");
            } catch (IOException e) {
                System.err.println("Error. No se ha podido notificar de la victoria 2");
                e.printStackTrace();
            }
        } else if (r == 3) {
            System.out.println("Empate");
            try {
                tablero.setT("D");
                json = gson.toJson(tablero);
                out1.writeUTF(json);
                out2.writeUTF(json);
            } catch (SocketException se) {
                System.out.println("Conexión con el cliente cerrada.");
            } catch (IOException e) {
                System.err.println("Error. No se ha podido notificar del empate");
                e.printStackTrace();
            }
        }
        try {
            //cerramos los flujos de datos
            out1.close();
            out2.close();
            in1.close();
            in2.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar los flujos de datos");
            e.printStackTrace();
        }
    }

    /**
     * Método que comprueba si el tablero está lleno
     *
     * @return True (lleno), False (no lleno)
     */
    private boolean lleno() {
        if (t.indexOf('0') == -1) {
            return true;
        }
        return false;
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
