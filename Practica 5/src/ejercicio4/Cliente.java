package ejercicio4;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLOutput;

public class Cliente extends Vista implements ActionListener, Runnable {
    private final int TOTAL_FILAS = 3;
    private final int TOTAL_COLUMNAS = 3;
    private final int PUERTO = 5555;
    private final String HOST = "localhost";
    private Socket conn;
    private DataInputStream in;
    private DataOutputStream out;
    private static int[][] t;
    private String s;
    private boolean turno;
    private char id;
    private int contadorTrampas;


    public Cliente() {
        try {
            conn = new Socket(HOST, PUERTO);
            in = new DataInputStream(conn.getInputStream());
            out = new DataOutputStream(conn.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error al conectar el Cliente con el Servidor");
            e.printStackTrace();
        }
        iniciar();
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                s = in.readUTF();
                if (s.equals("W")){
                    JOptionPane.showMessageDialog(null,
                            "Bien jugado, campeón.",
                        "¡Has ganado!",
                        JOptionPane.PLAIN_MESSAGE);
                    break;
            }
                if (s.equals("L")){
                    s = in.readUTF();
                    leerTablero(s);
                    interpretar();
                    JOptionPane.showMessageDialog(null,
                            "Tienes que mejorar.",
                            "¡Has perdido!",
                            JOptionPane.PLAIN_MESSAGE);
                    break;
                }
                if (s.equals("D")){
                    s = in.readUTF();
                    System.out.println("me llega una " + s);
                    leerTablero(s);
                    interpretar();
                    System.out.println("lanzo el mensaje");
                    JOptionPane.showMessageDialog(null,
                            "Los dos habéis jugado igual de mal",
                            "¡Empate!",
                            JOptionPane.PLAIN_MESSAGE);
                    break;
                }
                leerTablero(s);
                interpretar();
                System.out.println("lo último que me llega es " +s );
            }
        } catch (IOException e) {
            System.err.println("Error al leer el tablero");
            e.printStackTrace();
        }
    }

    private void interpretar() {
        if (t[0][0] == 1) {
            botones.get(0).setText("X");
        } else if (t[0][0] == 2) {
            botones.get(0).setText("O");
        }
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

        JButton btn = (JButton) e.getSource();
        if (turno) {
            if (jugar(btn)) {
                turno = false;
            } else {
                JOptionPane.showMessageDialog(null,
                        "No intentes hacer trampas",
                        "¿Pero no ves que está ocupada?",
                        JOptionPane.PLAIN_MESSAGE);
            }
        } else {
            if (contadorTrampas < 2) {
                if (this.id == '1') {
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
                JOptionPane.showMessageDialog(null,
                        "Deja al otro jugador pensar dónde quiere colocar su ficha.",
                        "QUE TE ESPERES, IMPACIENTE",
                        JOptionPane.PLAIN_MESSAGE);
            }
            contadorTrampas++;
        }
    }

    private boolean jugar(JButton btn) {
        int index = botones.indexOf(btn);
        if (!sustituir(index)) {
            return false;
        }
        if (this.id == '1') {
            btn.setText("X");
        } else if (this.id == '2') {
            btn.setText("O");
        } else {
            System.err.println("ID no especificado");
        }

        try {
            out.writeUTF(s);
        } catch (IOException ex) {
            System.out.println("Error. No he podido enviar el tablero");
            ex.printStackTrace();
        }
        return true;
    }

    private void leerTablero(String s) {
        t[0][0] = Integer.parseInt(String.valueOf(s.charAt(0)));
        t[0][1] = Integer.parseInt(String.valueOf(s.charAt(1)));
        t[0][2] = Integer.parseInt(String.valueOf(s.charAt(2)));
        t[1][0] = Integer.parseInt(String.valueOf(s.charAt(3)));
        t[1][1] = Integer.parseInt(String.valueOf(s.charAt(4)));
        t[1][2] = Integer.parseInt(String.valueOf(s.charAt(5)));
        t[2][0] = Integer.parseInt(String.valueOf(s.charAt(6)));
        t[2][1] = Integer.parseInt(String.valueOf(s.charAt(7)));
        t[2][2] = Integer.parseInt(String.valueOf(s.charAt(8)));

        if (s.charAt(s.length() - 1) == id) {
            this.turno = true;
        }
    }


    private boolean actualizarT(int index) {
        switch (index) {
            case 0:
                if (t[0][0] != 0) {
                    return false;
                }
                if (this.turno) {
                    t[0][0] = 1;
                } else {
                    t[0][0] = 2;
                }
                break;
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

    private void iniciar() {
        contadorTrampas = 0;
        try {
            System.out.println("Esperando jugadores...");
            //leemos la orden de jugar
            s = in.readUTF();
            //recibimos el id
            s = in.readUTF();
            //lo convertimos a char
            this.id = s.charAt(0);
        } catch (IOException e) {
            System.out.println("Error al leer el ID");
            e.printStackTrace();
        }
        if (id != '1' && id != '2') {
            System.err.println("Ya hay dos jugadores conectados.");
            System.exit(0);
        }
        System.out.println("¡A jugar!");
        t = new int[TOTAL_FILAS][TOTAL_COLUMNAS];
        for (int i = 0; i < TOTAL_FILAS; i++) {
            for (int j = 0; j < TOTAL_COLUMNAS; j++) {
                this.t[i][j] = 0;
            }
        }
        for (JButton boton : botones) {
            boton.addActionListener(this);
        }
    }

    private boolean sustituir(int index) {
        if (Integer.parseInt(String.valueOf(s.charAt(index))) != 0) {
            System.err.println("La casilla está ocupada");
            return false;
        }
        if (s.substring(s.length() - 1).equals("1")) {
            s = s.substring(0, index) + "1" + s.substring(index + 1);
            return actualizarT(index);
        } else if (s.substring(s.length() - 1).equals("2")) {
            s = s.substring(0, index) + "2" + s.substring(index + 1);
            return actualizarT(index);
        } else {
            System.err.println("Error en el tablero. El turno no está definido");
            return false;
        }
    }

    private void mostrarTablero() {
        for (int i = 0; i < TOTAL_FILAS; i++) {
            for (int j = 0; j < TOTAL_COLUMNAS; j++) {
                System.out.print(t[i][j]);
                if (j == TOTAL_COLUMNAS - 1) {
                    System.out.println();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Cliente();
    }
}
