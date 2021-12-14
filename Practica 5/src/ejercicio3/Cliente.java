package ejercicio3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    private TipoUsuario tu;
    private Socket conn;
    private DataInputStream in;
    private DataOutputStream out;
    private final int PUERTO = 5555;
    private final String HOST = "localhost";
    private static Scanner sc = new Scanner(System.in);
    private String tabla;

    public Cliente() {
        try {
            //inicializamos el socket, dis y dos
            conn = new Socket(HOST, PUERTO);
            in = new DataInputStream(conn.getInputStream());
            out = new DataOutputStream(conn.getOutputStream());
            //leemos mensaje de bienvenida
            System.out.println(in.readUTF());
            //llamamos al método que gestiona el inicio de sesión
            iniciarSesion();

            identificacionC();

            while (true) {
                elegirTabla();
                System.out.println(in.readUTF());
                break;
            }
        } catch (IOException e) {
            System.out.println("Error al conectar el Cliente " + e.toString());
        }
    }

    private void elegirTabla() throws IOException {
        String tipos = in.readUTF();
        System.out.println(tipos);
        String s = sc.nextLine();
        while (true) {
            if (s.equalsIgnoreCase("entrenador")
                    || s.equalsIgnoreCase("jugador") || s.equalsIgnoreCase("estadio")) {
                this.tabla = s;
                out.writeUTF(s);
                return;
            }
        }

    }

    private void identificacionC() throws IOException {

        if (this.tu == TipoUsuario.user) {
            userDo();
        } else if (this.tu == TipoUsuario.admin) {
            adminDo();
        } else {
            System.err.println("Error inesperado. ¡¿No eres usuario ni administrador?!");
        }
    }

    private void adminDo() throws IOException {
        out.writeUTF(this.tu.toString());
        System.out.println(in.readUTF());
        out.writeUTF(sc.nextLine());
        String comprobacion = in.readUTF();
        System.out.println(comprobacion);
        if (comprobacion.charAt(0) != 'B') {
            this.tu = TipoUsuario.user;
        } else {

        }
    }

    private void userDo() throws IOException {

        out.writeUTF(this.tu.toString());
        System.out.println(in.readUTF());
    }

    private void iniciarSesion() {

        while (true) {
            System.out.println("\n1. Usuario" +
                    "\n2. Administrador");
            String s = sc.nextLine();
            if (s.equals("1")) {
                this.tu = TipoUsuario.user;
                return;
            }
            if (s.equals("2")) {
                this.tu = TipoUsuario.admin;
                return;
            }
            System.out.println(s + " no es correcto. Las opciones son: ");
        }
    }

    public static void main(String[] args) {
        new Cliente();
    }
}
