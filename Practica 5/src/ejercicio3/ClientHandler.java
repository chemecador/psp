package ejercicio3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private final String pass = "admin";
    private DataOutputStream out;
    private DataInputStream in;
    private TipoUsuario tu;
    private String tabla;
    private String s;
    private Connection conexion;
    private boolean salir;
    private Socket socket;

    public ClientHandler(Socket cliente) {
        System.out.println("Conexión establecida con un nuevo cliente.");
        this.socket = cliente;
        try {
            out = new DataOutputStream(cliente.getOutputStream());
            in = new DataInputStream(cliente.getInputStream());
            if (identificacionS()) {
                conectarBBDD();
                while (true) {
                    if (!preguntarTabla()) {
                        break;
                    }
                    if (this.tu == TipoUsuario.user) {
                        if (this.tabla.equalsIgnoreCase("entrenador")
                                || this.tabla.equalsIgnoreCase("1")) {
                            out.writeUTF(leerEntrenadores(consultar()));
                        } else if (this.tabla.equalsIgnoreCase("jugador")
                                || this.tabla.equalsIgnoreCase("2")) {
                            out.writeUTF(leerJugadores(consultar()));
                        } else if (this.tabla.equalsIgnoreCase("estadio")
                                || this.tabla.equalsIgnoreCase("3")) {
                            out.writeUTF(leerEstadios(consultar()));
                        } else if (this.tabla.equalsIgnoreCase("salir")
                                || this.tabla.equalsIgnoreCase("4")) {
                            salir = true;
                        } else {
                            System.err.println("Error inesperado al elegir tabla en el servidor.");
                        }

                    } else if (this.tu == TipoUsuario.admin) {
                        String opcionesAdmin = "Opciones disponibles:" +
                                "\n 1. Insertar" +
                                "\n 2. Actualizar" +
                                "\n 3. Eliminar";
                        out.writeUTF(opcionesAdmin);
                        System.out.println("Has elegido " + in.readUTF());
                    } else {
                        System.err.println("Error inesperado al validar usuario/admin en el servidor.");
                    }
                }
            }
            out.writeUTF("¡Hasta pronto!");
            System.out.println("Conexión con el cliente cerrada");
        } catch (
                IOException ex) {
            System.out.println("IOException en Servidor\n" + ex.toString());
        } catch (
                SQLException e) {
            System.out.println("SQLException en Servidor\n" + e.toString());
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String leerEstadios(ResultSet consultar) throws SQLException {
        int i = 1;
        String estadios = "";
        while (consultar.next()) {
            estadios += "Estos son los estadios que se encuentran en nuestra base de datos:\n";
            estadios += "Entrenador número " + i + ":";
            estadios += "\n  Nombre: " + consultar.getString(2);
            estadios += "\n  Ciudad: " + consultar.getString(3);
            estadios += "\n";
            i++;
        }
        return estadios;
    }

    private String leerEntrenadores(ResultSet consultar) throws SQLException {
        int i = 1;
        String entrenadores = "";
        while (consultar.next()) {
            entrenadores += "Estos son los entrenadores que se encuentran en nuestra base de datos:\n";
            entrenadores += "Entrenador número " + i + ":";
            entrenadores += "\n  Nombre: " + consultar.getString(2);
            entrenadores += "\n  Nacionalidad: " + consultar.getString(3);
            entrenadores += "\n";
            i++;
        }
        return entrenadores;
    }

    private String leerJugadores(ResultSet consultar) throws SQLException {
        int i = 1;
        String jugadores = "";
        while (consultar.next()) {

            jugadores += "Estos son los jugadores que se encuentran en nuestra base de datos:\n";
            jugadores += "Jugador número " + i + ":";
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
        try {
            sentencia = conexion.prepareStatement(consulta);
            return sentencia.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error al consultar + " + e.toString());
        }

        return null;
    }

    private void conectarBBDD() {
        this.conexion = null;
        try {
            this.conexion = DriverManager.getConnection("jdbc:mysql://localhost:3307/liga"
                    , "root", "");
        } catch (SQLException e) {
            System.out.println("No se ha podido conectar con la base de datos. \n" + e.toString());
        }
    }

    private boolean preguntarTabla() throws IOException {
        String t = "\n*************************" +
                "\n¿Qué tabla quieres consultar?" +
                "\n1. Entrenador" +
                "\n2. Jugador" +
                "\n3. Estadio" +
                "\n4. Salir" +
                "\n*************************";
        out.writeUTF(t);
        String tablaElegida = in.readUTF();
        if (tablaElegida.equals("salir")) {
            return false;
        }
        this.tabla = tablaElegida;
        if (tablaElegida.equals("1")) {
            this.tabla = "Entrenador";
        }
        if (tablaElegida.equals("2")) {
            this.tabla = "Jugador";
        }
        if (tablaElegida.equals("3")) {
            this.tabla = "Estadio";
        }
        System.out.println("El cliente está consultando la tabla " + this.tabla);
        return true;
    }

    private boolean identificacionS() throws IOException {
        out.writeUTF("¡Buenas! ¿Eres un usuario o un administrador?");
        s = in.readUTF();
        if (s.equals("admin")) {
            out.writeUTF("Introduce la clave de administrador: ");
            s = in.readUTF();
            if (s.equals(pass)) {
                out.writeUTF("Bienvenido, administrador.");
                this.tu = TipoUsuario.admin;
                return true;
            } else {
                out.writeUTF("Contraseña incorrecta. Serás identificado como usuario.");
                this.tu = TipoUsuario.user;
                return true;
            }
        } else if (s.equals("user")) {
            out.writeUTF("Bienvenido, usuario.");
            this.tu = TipoUsuario.user;
            return true;
        } else if (s.equals("salir")) {
            return false;
        } else {
            System.err.println("Error inesperado. ");
            return false;
        }
    }
}

