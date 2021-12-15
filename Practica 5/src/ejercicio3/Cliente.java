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
    private boolean salir;

    public Cliente() {
        try {
            //inicializamos el socket, dis y dos
            conn = new Socket(HOST, PUERTO);
            in = new DataInputStream(conn.getInputStream());
            out = new DataOutputStream(conn.getOutputStream());
            salir = false;
            //leemos mensaje de bienvenida
            System.out.println(in.readUTF());
            //llamamos al método que gestiona el inicio de sesión
            if (iniciarSesion()) {
                identificacionC();
                while (true) {
                    if (elegirTabla()) {
                        if (this.tu == TipoUsuario.user) {
                            //leer consulta
                            System.out.println(in.readUTF());
                        } else if (this.tu == TipoUsuario.admin) {
                            //leer opciones
                            String opciones = in.readUTF();
                            out.writeUTF(elegirOpcion(opciones));
                        } else {
                            System.out.println("Error inesperado...");
                        }
                    } else {
                        break;
                    }
                }
            }
            out.writeUTF("salir");
            System.out.println(in.readUTF());
            conn.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Error al conectar el Cliente " + e.toString());
        }
    }

    private String elegirOpcion(String opciones) throws IOException {
        while (true) {
            System.out.println(opciones);
            String opcion = sc.nextLine();
            if (opcion.equalsIgnoreCase("insertar") ||
                    opcion.equalsIgnoreCase("actualizar") ||
                    opcion.equalsIgnoreCase("eliminar")) {
                return opcion;
            }
            if (opcion.equals("1")){
                return "insertar";
            }
            if (opcion.equals("2")){
                return "actualizar";
            }
            if (opcion.equals("3")){
                return "eliminar";
            }

        }
    }

    private boolean elegirTabla() throws IOException {
        String tipos = in.readUTF();
        System.out.println(tipos);
        String s = sc.nextLine();
        while (true) {
            if (s.equalsIgnoreCase("entrenador")
                    || s.equalsIgnoreCase("jugador")
                    || s.equalsIgnoreCase("estadio")
                    || s.equalsIgnoreCase("1")
                    || s.equalsIgnoreCase("2")
                    || s.equalsIgnoreCase("3")) {
                this.tabla = s;
                out.writeUTF(s);
                return true;
            } else if (s.equalsIgnoreCase("salir") || s.equalsIgnoreCase("4")) {
                return false;
            } else {
                System.out.println("No existe la tabla " + s + ". Los tipos son:\n" + tipos);
                s = sc.nextLine();
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

    private boolean iniciarSesion() {

        while (true) {
            System.out.println("\n1. Usuario" +
                    "\n2. Administrador" +
                    "\n3. Salir");
            String s = sc.nextLine();
            if (s.equals("1") || s.equalsIgnoreCase("usuario")
                    || s.equalsIgnoreCase("user")) {
                this.tu = TipoUsuario.user;
                return true;
            }
            if (s.equals("2") || s.equalsIgnoreCase("admin")
                    || s.equalsIgnoreCase("administrador")) {
                this.tu = TipoUsuario.admin;
                return true;
            }
            if (s.equals("3") || s.equalsIgnoreCase("salir")) {
                return false;
            }
            System.out.println(s + " no es correcto. Las opciones son: ");
        }
    }

    public static void main(String[] args) {
        new Cliente();
    }
}
