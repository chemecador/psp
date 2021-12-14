package ejercicio3;

import javax.xml.transform.Result;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class Servidor {

    /*En base a la base de datos LaLiga.sql, realizar una aplicación realizar dos aplicaciones (Cliente y
servidor) que utilizando sockets, que permitan administrar y consultar la BD. Para ello existirán
dos tipos de perfiles:
• Usuarios: podrán consultar información.
• Administradores: podrán insertar, actualizar y eliminar la información.
Para ello se trabajarán con las siguientes tablas:
• Entrenador
• Jugador
• Estadio
*/

    private final String pass = "admin";
    //private ServerSocket ss;
    private DataOutputStream out;
    private DataInputStream in;
    private TipoUsuario tu;
    private String tabla;
    private String s;
    private Connection conexion;

    public Servidor() {
        try (ServerSocket ss = new ServerSocket(5555)) {
            Socket client = ss.accept();
            out = new DataOutputStream(client.getOutputStream());
            in = new DataInputStream(client.getInputStream());
            identificacionS();
            conectarBBDD();
            while (true) {
                preguntarTabla();
                System.out.println("Tabla seleccionada: " + this.tabla);
                if (this.tu == TipoUsuario.user) {
                    if (this.tabla.equalsIgnoreCase("jugador")) {
                        System.out.println(leerJugadores(consultar()));
                        //out.writeUTF(leerJugadores(consultar()));
                    }

                } else if (this.tu == TipoUsuario.admin) {

                } else {
                    System.err.println("Error inesperado al validar usuario/admin en el servidor.");
                }
                break;
            }
        } catch (IOException ex) {
            System.out.println("IOException en Servidor\n" + ex.toString());
        } catch (SQLException e) {
            System.out.println("SQLException en Servidor\n" + e.toString());
        }
    }

    private String leerJugadores(ResultSet consultar) throws SQLException {
        int i = 1;
        String jugadores = "";
        while (consultar.next()) {

            jugadores += "Jugador número " + i + ":\n";
            jugadores += "\n  Nombre: " + consultar.getString(2);
            jugadores += "\n  Nacionalidad: " + consultar.getString(3);
            jugadores += "\n  Posición: " + consultar.getString(5);
            jugadores += "\n";
            i++;
        }
        return jugadores;
    }

    private ResultSet consultar() {

        String consulta = "SELECT * FROM " + this.tabla;
        PreparedStatement sentencia = null;
        System.out.println("consultemos");
        try {
            sentencia = conexion.prepareStatement(consulta);
            return sentencia.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error en la consulta + " + e.toString());
        }

        return null;
    }

    private void conectarBBDD() {
        conexion = null;
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3307/liga"
                    , "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void preguntarTabla() throws IOException {
        String t = "\n\n***************\n¿Qué tabla quieres consultar?" +
                "\nEntrenador" +
                "\nJugador" +
                "\nEstadio" +
                "\n***************";
        out.writeUTF(t);
        this.tabla = in.readUTF();
    }

    private void identificacionS() throws IOException {
        out.writeUTF("¡Buenas! ¿Eres un usuario o un administrador?");
        s = in.readUTF();
        if (s.equals("admin")) {
            out.writeUTF("Introduce la clave de administrador: ");
            s = in.readUTF();
            if (s.equals(pass)) {
                out.writeUTF("Bienvenido, administrador.");
                this.tu = TipoUsuario.admin;
            } else {
                out.writeUTF("Contraseña incorrecta. Serás identificado como usuario.");
                this.tu = TipoUsuario.user;
            }
        } else if (s.equals("user")) {
            out.writeUTF("Bienvenido, usuario.");
            this.tu = TipoUsuario.user;
        } else {
            System.err.println("Error inesperado. ");
        }
    }

    public static void main(String[] args) {
        new Servidor();
    }

}
