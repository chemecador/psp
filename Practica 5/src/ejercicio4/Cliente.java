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
    private int[][] t;
    private String s;
    private boolean turno;
    private char id;


    public Cliente() {
        new Thread(this).start();
    }

    private void iniciar() {
        t = new int[TOTAL_FILAS][TOTAL_COLUMNAS];
        for (int i = 0; i < TOTAL_FILAS; i++) {
            for (int j = 0; j < TOTAL_COLUMNAS; j++) {
                this.t[i][j] = 0;
            }
        }
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
        System.out.println("Mmm... a ver si te toca:");
        if (s.charAt(s.length() - 1) == id) {
            System.out.println("Te toca");
            this.turno = true;
        }
    }

    private void mostrarTablero() {
        for (int i = 0; i < TOTAL_FILAS; i++) {
            for (int j = 0; j < TOTAL_COLUMNAS; j++) {
                System.out.print(this.t[i][j]);
                if (j == TOTAL_COLUMNAS - 1) {
                    System.out.println();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Cliente();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JButton btn = (JButton) e.getSource();
        if (turno)
            jugar(btn);
        turno = false;
    }

    private boolean jugar(JButton btn) {
        if (this.id == 'U') {
            btn.setText("X");
        } else if (this.id == 'D') {
            btn.setText("O");
        } else {
            System.err.println("ID no especificado");
        }

        btn.setText("X");
        int index = botones.indexOf(btn);
        if (!sustituir(index)) {
            return false;
        }
        try {
            out.writeUTF(s);
        } catch (IOException ex) {
            System.out.println("Error. No he podido enviar el tablero");
            ex.printStackTrace();
        }
        return true;
    }

    private boolean sustituir(int index) {
        if (Integer.parseInt(String.valueOf(s.charAt(index))) != 0) {
            System.err.println("La casilla está ocupada");
            return false;
        }
        if (s.substring(s.length() - 1).equals("U")) {
            s = s.substring(0, index) + "1" + s.substring(index + 1);
            s = s.substring(0, s.length() - 1);
            s += "D";
            return actualizarT(index);
        } else if (s.substring(s.length() - 1).equals("D")) {
            s = s.substring(0, index) + "2" + s.substring(index + 1);
            s = s.substring(0, s.length() - 1);
            s += "U";
            return actualizarT(index);
        } else {
            System.err.println("Error en el tablero. El turno no está definido");
            return false;
        }
    }

    private boolean actualizarT(int index) {
        mostrarTablero();
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
        System.out.println("\nel tablero posterior es ");
        mostrarTablero();
        return true;
    }

    @Override
    public void run() {
        System.out.println("buenas");
        iniciar();
        System.out.println("he iniciado");
        for (JButton boton : botones) {
            boton.addActionListener(this);
        }
        try {
            conn = new Socket(HOST, PUERTO);
            in = new DataInputStream(conn.getInputStream());
            out = new DataOutputStream(conn.getOutputStream());
            System.out.println("voy a leer");
            int i = in.read();
            System.out.println(" i es " + i);
            if (i == 1) {
                this.id = 'U';
            } else if (i == 2) {
                this.id = 'D';
            } else {
                System.err.println("Error. Ya hay dos jugadores.");
                System.exit(0);
            }
            while (true) {
                s = in.readUTF();
                leerTablero(s);

            }
            //mostrarTablero();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
