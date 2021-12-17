package ejercicio4;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente extends Vista implements ActionListener {
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


    public Cliente() {
        iniciarTablero();
        for (JButton boton : botones) {
            boton.addActionListener(this);
        }

        try {
            conn = new Socket(HOST, PUERTO);
            in = new DataInputStream(conn.getInputStream());
            out = new DataOutputStream(conn.getOutputStream());
            s = in.readUTF();
            leerTablero(s);
            mostrarTablero();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniciarTablero() {
        t = new int[TOTAL_FILAS][TOTAL_COLUMNAS];
        for (int i = 0; i < TOTAL_FILAS; i++) {
            for (int j = 0; j < TOTAL_COLUMNAS; j++) {
                this.t[i][j] = 0;
            }
        }
    }

    private void leerTablero(String s) {
        t[0][0] = Integer.parseInt(String.valueOf(s.charAt(0)));
        t[1][0] = Integer.parseInt(String.valueOf(s.charAt(1)));
        t[2][0] = Integer.parseInt(String.valueOf(s.charAt(2)));
        t[0][1] = Integer.parseInt(String.valueOf(s.charAt(3)));
        t[1][1] = Integer.parseInt(String.valueOf(s.charAt(4)));
        t[2][1] = Integer.parseInt(String.valueOf(s.charAt(5)));
        t[0][2] = Integer.parseInt(String.valueOf(s.charAt(6)));
        t[1][2] = Integer.parseInt(String.valueOf(s.charAt(7)));
        t[2][2] = Integer.parseInt(String.valueOf(s.charAt(8)));
        if (s.charAt(9) == 'T') {
            this.turno = true;
        } else if (s.charAt(9) == 'N') {
            this.turno = false;
        } else {
            System.err.println("Error. Turno no especificado");
        }
    }

    private void mostrarTablero() {
        for (int i = 0; i < TOTAL_FILAS; i++) {
            for (int j = 0; j < TOTAL_COLUMNAS; j++) {
                if (j == 0) {
                    System.out.println();
                }
                System.out.print(this.t[i][j]);
            }
        }
    }

    public static void main(String[] args) {
        new Cliente();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JButton btn = (JButton) e.getSource();
        if (turno) {
            btn.setText("X");
            int index = botones.indexOf(btn);
            /*String nuevoTablero = s.substring(0,index)+"1"+s.substring(index+1);
            s = nuevoTablero;*/
            s = s.substring(0,index)+"1"+s.substring(index+1);
            System.out.println("\n"+s);
        }
        else {
            btn.setText("O");
            int index = botones.indexOf(btn);
            s.replace(String.valueOf(s.charAt(index)),"2");
            String nuevoTablero = s.substring(0,index)+"1"+s.substring(index+1);
            s = nuevoTablero;
            System.out.println(s);
        }
    }

}
