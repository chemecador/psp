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
                    int i = 0;
                    if (elegirTabla()) {
                        i++;
                        if (this.tu == TipoUsuario.user) {
                            //leer consulta
                            System.out.println(in.readUTF());
                        } else if (this.tu == TipoUsuario.admin) {
                            adminDo();
                        } else {
                            System.out.println("Error inesperado...");
                        }
                    } else {
                        System.err.println("No has elegido tabla");
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

    private void adminDo() throws IOException {
        String leerOpciones = in.readUTF();
        String opcionElegida = elegirOpcion(leerOpciones);
        out.writeUTF(opcionElegida);
        if (this.tabla.equalsIgnoreCase("jugador")) {
            //leer la pregunta del nombre
            System.out.println(in.readUTF());
            //escribir el nombre
            out.writeUTF(sc.nextLine());
            //leer la pregunta de la nacionalidad
            System.out.println(in.readUTF());
            //escribir la nacionalidad
            out.writeUTF(sc.nextLine());
            //leer la pregunta de la posición
            System.out.println(in.readUTF());
            //escribir la posición
            out.writeUTF(sc.nextLine());
            //leer mensaje de éxito
            System.out.println(in.readUTF());

        } else if (this.tabla.equalsIgnoreCase("entrenador")) {
            //leer la pregunta del nombre
            System.out.println(in.readUTF());
            //escribir el nombre
            out.writeUTF(sc.nextLine());
            //leer la pregunta de la nacionalidad
            System.out.println(in.readUTF());
            //escribir la nacionalidad
            out.writeUTF(sc.nextLine());
            //leer mensaje de éxito
            System.out.println(in.readUTF());

        } else if (this.tabla.equalsIgnoreCase("estadio")) {
            //leer la pregunta del nombre
            System.out.println(in.readUTF());
            //escribir el nombre
            out.writeUTF(sc.nextLine());
            //leer la pregunta de la ciudad
            System.out.println(in.readUTF());
            //escribir la ciudad
            out.writeUTF(sc.nextLine());
            //leer mensaje de éxito
            System.out.println(in.readUTF());

        } else {
            System.out.println("Error. La tabla " + opcionElegida + " no existe.");
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
            if (opcion.equals("1")) {
                return "insertar";
            }
            if (opcion.equals("2")) {
                return "actualizar";
            }
            if (opcion.equals("3")) {
                return "eliminar";
            }

        }
    }

    private boolean elegirTabla() throws IOException {
        //lee del servidor las tablas disponibles
        String opciones = in.readUTF();
        //muestra las tablas disponibles por pantalla
        System.out.println(opciones);
        //lee de teclado la tabla que elige el cliente
        String s = sc.nextLine();
        while (true) {
            //si la tabla es correcta
            if (s.equals("1")) {
                this.tabla = "entrenador";
                //envía al servidor la tabla que ha elegido
                out.writeUTF(s);
                //devuelve true ya que ha funcionado correctamente
                return true;
            }
            if (s.equals("2")) {
                this.tabla = "jugador";
                //envía al servidor la tabla que ha elegido
                out.writeUTF(s);
                //devuelve true ya que ha funcionado correctamente
                return true;
            }
            if (s.equals("3")) {
                this.tabla = "estadio";
                //envía al servidor la tabla que ha elegido
                out.writeUTF(s);
                //devuelve true ya que ha funcionado correctamente
                return true;
            }
            if (s.equalsIgnoreCase("entrenador")
                    || s.equalsIgnoreCase("jugador")
                    || s.equalsIgnoreCase("estadio")) {
                //cambia el atributo tabla de esta clase
                this.tabla = s;
                //envía al servidor la tabla que ha elegido
                out.writeUTF(s);
                //devuelve true ya que ha funcionado correctamente
                return true;
            } else if (s.equalsIgnoreCase("salir") || s.equalsIgnoreCase("4")) {
                //si el cliente quiere salir, devuelve falso
                return false;
            } else {
                //muestra el mensaje de que no existe esa tabla y permite al usuario volver a escribir
                System.out.println("No existe la tabla " + s + ". Los tipos son:\n" + opciones);
                s = sc.nextLine();
            }
        }

    }

    private void identificacionC() throws IOException {

        if (this.tu == TipoUsuario.user) {
            userComprobacion();
        } else if (this.tu == TipoUsuario.admin) {
            adminComprobacion();
        } else {
            System.err.println("Error inesperado. ¡¿No eres usuario ni administrador?!");
        }
    }

    private void adminComprobacion() throws IOException {
        out.writeUTF(this.tu.toString());
        System.out.println(in.readUTF());
        out.writeUTF(sc.nextLine());
        String comprobacion = in.readUTF();
        System.out.println(comprobacion);
        if (comprobacion.charAt(0) != 'B') {
            this.tu = TipoUsuario.user;
        }
    }

    private void userComprobacion() throws IOException {

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
