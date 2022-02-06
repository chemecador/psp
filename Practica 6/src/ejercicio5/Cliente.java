package ejercicio5;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Clase cliente. Hereda de la clase Vista, e implementa ActionListener para los botones y Runnable para lanzar un hilo.
 */
public class Cliente extends Vista implements ActionListener, Runnable {
    private final int TOTAL_FILAS = 3; //total de filas del tablero
    private final int TOTAL_COLUMNAS = 3;//total de columnas del tablero
    private final int PUERTO = 5555; //puerto del servidor al que conectarse
    private final String HOST = "localhost"; //ip del servidor (local)
    private Socket conn; //socket con la conexión
    private DataInputStream in; //flujo de entrada de datos con el servidor
    private DataOutputStream out; //flujo de salida de datos con el servidor
    private int[][] t; //tablero
    private String s; //string para enviar y recibir información
    private boolean turno; //true si es mi turno, false si no
    private char id; //id de cliente (jugador 1 o jugador 2)
    private int contadorTrampas; //cantidad de veces que el usuario ha intentado una acción no permitida
    private Tablero tablero; //objeto de tipo Tablero que servirá para enviarle a cada cliente el tablero
    private String json; //String que contiene el json que será enviado entre clientes
    private Gson gson; //librería Gson para trabajar con datos json

    /**
     * Constructor de la clase
     */
    private Cliente() {
        try {
            //puesta a punto del cliente
            conn = new Socket(HOST, PUERTO);
            in = new DataInputStream(conn.getInputStream());
            out = new DataOutputStream(conn.getOutputStream());
            tablero = new Tablero();
            gson = new Gson();
        } catch (IOException e) {
            System.out.println("Error al conectar el Cliente con el Servidor");
            e.printStackTrace();
        }
        //método que hace realiza las tareas iniciales
        iniciar();
        //lanzamos el hilo
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                //leo el String que manda el servidor
                s = in.readUTF();
                //lo convierto a Tablero con el método fromJson
                tablero = gson.fromJson(s, Tablero.class);
                //guardo en el String s el contenido del String t del Tablero tablero
                s = tablero.getT();
                //si he recibido W (win)...
                if (s.equals("W")) {
                    //... es que he ganado, lo notifico
                    JOptionPane.showMessageDialog(null,
                            "Bien jugado, campeón.",
                            "¡Has ganado!",
                            JOptionPane.PLAIN_MESSAGE);
                    //cerramos la ventana
                    dispose();
                    //salimos del bucle
                    break;
                }
                //si he recibido L (lose)...
                if (s.equals("L")) {
                    //es que he perdido, termino de leer la ficha que ha puesto el rival
                    //leo el String que manda el servidor
                    s = in.readUTF();
                    //lo convierto a Tablero con el método fromJson
                    tablero = gson.fromJson(s, Tablero.class);
                    //guardo en el String s el contenido del String t del Tablero tablero
                    s = tablero.getT();
                    leerTablero(s);
                    interpretar();
                    //y lo notifico
                    JOptionPane.showMessageDialog(null,
                            "Tienes que mejorar.",
                            "¡Has perdido!",
                            JOptionPane.PLAIN_MESSAGE);

                    dispose();
                    break;
                }
                //si he recibido D (draw)...
                if (s.equals("D")) {
                    //es que he empatado, lo notifico
                    JOptionPane.showMessageDialog(null,
                            "Los dos habéis jugado igual de mal",
                            "¡Empate!",
                            JOptionPane.PLAIN_MESSAGE);
                    dispose();
                    break;
                }
                //guardamos el tablero que ha llegado del string en el tablero local de enteros t[][]
                leerTablero(s);
                //leemos del tablero de enteros y actualizamos la interfaz gráfica
                interpretar();
            }
            //cerramos las conexiones y el programa
            in.close();
            out.close();
            conn.close();
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Error al leer el tablero");
            e.printStackTrace();
        }
    }

    /**
     * Método que lee el tablero local y pone X donde ha colocado el jugador 1, y O donde ha colocado el jugador 2
     */
    private void interpretar() {
        //si en la posición [0][0] hay un 1 (ha puesto ficha el jugador 1)...
        if (t[0][0] == 1) {
            //...pintamos la X en la ventana
            botones.get(0).setText("X");
            //...si hay un 2...
        } else if (t[0][0] == 2) {
            //...pintamos la O en la ventana
            botones.get(0).setText("O");
        }
        //se repite el proceso para las demás combinaciones
        if (t[0][1] == 1) {
            botones.get(1).setText("X");
        } else if (t[0][1] == 2) {
            botones.get(1).setText("O");
        }
        if (t[0][2] == 1) {
            botones.get(2).setText("X");
        } else if (t[0][2] == 2) {
            botones.get(2).setText("O");
        }
        if (t[1][0] == 1) {
            botones.get(3).setText("X");
        } else if (t[1][0] == 2) {
            botones.get(3).setText("O");
        }
        if (t[1][1] == 1) {
            botones.get(4).setText("X");
        } else if (t[1][1] == 2) {
            botones.get(4).setText("O");
        }
        if (t[1][2] == 1) {
            botones.get(5).setText("X");
        } else if (t[1][2] == 2) {
            botones.get(5).setText("O");
        }
        if (t[2][0] == 1) {
            botones.get(6).setText("X");
        } else if (t[2][0] == 2) {
            botones.get(6).setText("O");
        }
        if (t[2][1] == 1) {
            botones.get(7).setText("X");
        } else if (t[2][1] == 2) {
            botones.get(7).setText("O");
        }
        if (t[2][2] == 1) {
            botones.get(8).setText("X");
        } else if (t[2][2] == 2) {
            botones.get(8).setText("O");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //guardamos en una variable JButton el botón que ha sido pulsado
        //guardamos el índice del botón
        int index = botones.indexOf(e.getSource());
        //si es mi turno...
        if (turno) {
            //...intento jugar. Si he podido colocar la ficha correctamente...
            if (jugar(index)) {
                //... ya he jugado, por lo que ya no es mi turno.
                turno = false;
            } else {
                //...si no he podido colocar la ficha correctamente,
                // es que he intentado poner la ficha en una casilla ocupada. Lo notifico PERO NO PIERDO el turno
                JOptionPane.showMessageDialog(null,
                        "No intentes hacer trampas",
                        "¿Pero no ves que está ocupada?",
                        JOptionPane.PLAIN_MESSAGE);
            }
        } else {
            //...si no es mi turno...
            if (contadorTrampas < 2) {
                //si he intentado poner la ficha en una casilla ocupada menos de 2 veces...
                if (this.id == '1') {
                    //notifico que no es mi turno
                    JOptionPane.showMessageDialog(null,
                            "Es el turno del jugador 2",
                            "Espera tu turno",
                            JOptionPane.PLAIN_MESSAGE);
                } else if (this.id == '2') {
                    JOptionPane.showMessageDialog(null,
                            "Es el turno del jugador 1",
                            "Espera tu turno",
                            JOptionPane.PLAIN_MESSAGE);
                }
            } else {
                //si el usuario intenta poner la ficha más de 2 veces, le digo que espere
                JOptionPane.showMessageDialog(null,
                        "Deja al otro jugador pensar dónde quiere colocar su ficha.",
                        "Tranquilo...",
                        JOptionPane.PLAIN_MESSAGE);
            }
            //sumamos 1 al contador de "descuidos"
            contadorTrampas++;
        }
    }

    /**
     * Método que se encarga de jugar, colocar la ficha en el índice correspondiente
     *
     * @param index índice del botón en el arraylist de botones
     * @return True (ficha colocada correctamente), False (la ficha no se puede colocar en esa posición)
     */
    private boolean jugar(int index) {

        if (!sustituir(index)) {
            return false;
        }
        if (this.id == '1') {
            botones.get(index).setText("X");
        } else if (this.id == '2') {
            botones.get(index).setText("O");
        } else {
            System.err.println("ID no especificado");
        }

        try {
            tablero.setT(s);
            json = gson.toJson(tablero);
            out.writeUTF(json);
        } catch (IOException ex) {
            System.out.println("Error. No he podido enviar el tablero");
            ex.printStackTrace();
        }
        return true;
    }

    /**
     * Lee el tablero del string y lo guarda en un tablero local de enteros
     *
     * @param s string que contiene el tablero
     */
    private void leerTablero(String s) {
        //guardamos en cada celda de la matriz local de enteros, el valor que nos ha llegado a través del string
        t[0][0] = Integer.parseInt(String.valueOf(s.charAt(0)));
        t[0][1] = Integer.parseInt(String.valueOf(s.charAt(1)));
        t[0][2] = Integer.parseInt(String.valueOf(s.charAt(2)));
        t[1][0] = Integer.parseInt(String.valueOf(s.charAt(3)));
        t[1][1] = Integer.parseInt(String.valueOf(s.charAt(4)));
        t[1][2] = Integer.parseInt(String.valueOf(s.charAt(5)));
        t[2][0] = Integer.parseInt(String.valueOf(s.charAt(6)));
        t[2][1] = Integer.parseInt(String.valueOf(s.charAt(7)));
        t[2][2] = Integer.parseInt(String.valueOf(s.charAt(8)));

        //comprobamos el turno que viene definido en el último dígito del string
        if (s.charAt(s.length() - 1) == id) {
            this.turno = true;
        }
    }

    /**
     * Actualizamos el tablero local en función de la casilla que se ha marcado
     *
     * @param index Posición de la casilla en la que se ha colocado la ficha
     * @return True (actualización correcta), False (ha habido un error)
     */
    private boolean actualizarT(int index) {
        //distinguimos en función del valor del índice
        switch (index) {
            case 0:
                //si la casilla no contiene un 0 (es decir, está ocupada)...
                if (t[0][0] != 0) {
                    //...no se puede colocar ficha, devolvemos falso
                    return false;
                }
                //si está libre, ponemos 1 si es mi turno...
                if (this.turno) {
                    t[0][0] = 1;
                } else {
                    //...o un 2 si es el turno del rival
                    t[0][0] = 2;
                }
                break;
            //idem para el resto de casos
            case 1:
                if (t[0][1] != 0) {
                    return false;
                }
                if (this.turno) {
                    t[0][1] = 1;
                } else {
                    t[0][1] = 2;
                }
                break;
            case 2:
                if (t[0][2] != 0) {
                    return false;
                }
                if (this.turno) {
                    t[0][2] = 1;
                } else {
                    t[0][2] = 2;
                }
                break;
            case 3:
                if (t[1][0] != 0) {
                    return false;
                }
                if (this.turno) {
                    t[1][0] = 1;
                } else {
                    t[1][0] = 2;
                }
                break;
            case 4:
                if (t[1][1] != 0) {
                    return false;
                }
                if (this.turno) {
                    t[1][1] = 1;
                } else {
                    t[1][1] = 2;
                }
                break;
            case 5:
                if (t[1][2] != 0) {
                    return false;
                }
                if (this.turno) {
                    t[1][2] = 1;
                } else {
                    t[1][2] = 2;
                }
                break;
            case 6:
                if (t[2][0] != 0) {
                    return false;
                }
                if (this.turno) {
                    t[2][0] = 1;
                } else {
                    t[2][0] = 2;
                }
                break;
            case 7:
                if (t[2][1] != 0) {
                    return false;
                }
                if (this.turno) {
                    t[2][1] = 1;
                } else {
                    t[2][1] = 2;
                }
                break;
            case 8:
                if (t[2][2] != 0) {
                    return false;
                }
                if (this.turno) {
                    t[2][2] = 1;
                } else {
                    t[2][2] = 2;
                }
                break;
        }
        return true;
    }

    /**
     * Clase iniciar. Realiza las tareas iniciales.
     */
    private void iniciar() {
        //inicializar contador de trampas a 0
        contadorTrampas = 0;
        try {
            System.out.println("Esperando jugadores...");
            //leemos la orden de jugar
            s = in.readUTF();
            tablero = gson.fromJson(s, Tablero.class);
            s = tablero.getT();
            //recibimos el id
            s = in.readUTF();
            tablero = gson.fromJson(s, Tablero.class);
            s = tablero.getT();
            //lo convertimos a char
            this.id = s.charAt(0);
            //leemos el turno
            s = in.readUTF();
            tablero = gson.fromJson(s, Tablero.class);
            s = tablero.getT();
        } catch (IOException e) {
            System.out.println("Error al comenzar el juego");
            e.printStackTrace();
        }
        //comprobamos que no haya ningún jugador que no sea jugador 1 o jugador 2
        if (id != '1' && id != '2') {
            //de momento, al 3 en raya solo pueden jugar 2 personas.
            System.err.println("Ya hay dos jugadores conectados.");
            System.exit(0);
        }
        //si es el jugador 1...
        if (this.id == '1') {
            //...se indica en el título de la ventana
            setTitle("Tres en raya ( jugador 1 - X )");
        } else if (this.id == '2') {
            //...idem si es el jugador 2
            setTitle("Tres en raya ( jugador 2 - O )");
        }
        //si el turno es igual al id (turno = 1 && jugador = 1, turno = 2 && jugador = 2)
        if (s.equals(String.valueOf(this.id))) {
            //notificamos al jugador que le toca empezar
            JOptionPane.showMessageDialog(null,
                    " Empiezas tú.",
                    "¡A jugar!",
                    JOptionPane.PLAIN_MESSAGE);
        } else {
            //...si no, notificamos al jugador que empieza el rival
            JOptionPane.showMessageDialog(null,
                    " Empieza el rival.",
                    "¡A jugar!",
                    JOptionPane.PLAIN_MESSAGE);
        }
        //inicializamos el tablero local de enteros
        t = new int[TOTAL_FILAS][TOTAL_COLUMNAS];
        for (int i = 0; i < TOTAL_FILAS; i++) {
            for (int j = 0; j < TOTAL_COLUMNAS; j++) {
                this.t[i][j] = 0;
            }
        }
        //añadimos los actionListeners
        for (JButton boton : botones) {
            boton.addActionListener(this);
        }
    }

    /**
     * Sustituye en el string que se envía al servidor el contenido de la posición en la que se intenta jugar.
     *
     * @param index Posición donde se pretende colocar la ficha
     * @return True (se ha podido colocar correctamente), False (la casilla está ocupada o ha sucedido un error inesperado)
     */
    private boolean sustituir(int index) {
        //si en la posición del índice no hay un 0 (no está libre)...
        if (s.charAt(index) != '0') {
            //...notificamos que la casilla está ocupada y devolvemos falso
            System.err.println("La casilla está ocupada");
            return false;
        }
        //...no está ocupada. Si era el turno del jugador 1...
        if (s.substring(s.length() - 1).equals("1")) {
            //...ponemos un 1, y devolvemos el string
            s = s.substring(0, index) + "1" + s.substring(index + 1);
            return actualizarT(index);
        } else if (s.substring(s.length() - 1).equals("2")) {
            //...idem si era el turno del jugador 2
            s = s.substring(0, index) + "2" + s.substring(index + 1);
            return actualizarT(index);
        } else {
            //Error inesperado que nunca debería suceder
            System.err.println("Error en el tablero, el turno no está definido. Por favor, contacta con el desarrollador");
            return false;
        }
    }

    /**
     * Clase principal
     *
     * @param args
     */
    public static void main(String[] args) {
        new Cliente();
    }
}
