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
                int i = 1;
                while (true) {
                    System.out.println("iteracion numero " + i);
                    if (!preguntarTabla()) {
                        System.out.println("CHAU");
                        break;
                    }
                    if (this.tu == TipoUsuario.user) {
                       userDo();
                    } else if (this.tu == TipoUsuario.admin) {
                        System.out.println("entro aqui por " + i + " vez");
                        adminDo();
                    } else {
                        System.err.println("Error inesperado al validar usuario/admin en el servidor.");
                    }
                    i++;
                }
            }
            out.writeUTF("¡Hasta pronto!");
            System.out.println("Conexión con el cliente cerrada");
        } catch (IOException ex) {
            System.out.println("IOException en Servidor\n" + ex.toString());
            ex.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQLException en Servidor\n" + e.toString());
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void adminDo() throws IOException, SQLException {
        //string que guarda las opciones disponibles
        String opciones =   "Opciones disponibles:" +
                            "\n 1. Insertar" +
                            "\n 2. Actualizar" +
                            "\n 3. Eliminar";
        //enviar las opciones disponibles al cliente
        out.writeUTF(opciones);
        //leer la elección (ins, act, eli) del cliente
        s = in.readUTF();
        if (s.equalsIgnoreCase("insertar")) {
            System.out.println("entro en insertar");
            insertar();
            System.out.println("salgo de insertar");
        } else if (s.equalsIgnoreCase("actualizar")) {
            out.writeUTF("actualizando...");
        } else if (s.equalsIgnoreCase("eliminar")) {
            out.writeUTF("eliminando...");
        } else {
            System.out.println("Opción no válida");
        }
    }

    private void userDo() throws SQLException, IOException {
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
    }

    private int insertar() throws IOException, SQLException {
        if (this.tabla.equalsIgnoreCase("jugador")) {
            out.writeUTF("Escribe el nombre del jugador");
            String nombreJug = in.readUTF();
            out.writeUTF("Escribe la nacionalidad del jugador");
            String paisJug = in.readUTF();
            out.writeUTF("Escribe la posición del jugador");
            String posJug = in.readUTF();
            out.writeUTF("Jugador insertado");
            return insertarJugador(nombreJug, paisJug, posJug);

        } else if (this.tabla.equalsIgnoreCase("entrenador")) {
            System.out.println("la tabla es entrenador");
            out.writeUTF("Escribe el nombre del entrenador");
            String nombreEnt = in.readUTF();
            out.writeUTF("Escribe la nacionalidad del entrenador");
            String paisEnt = in.readUTF();
            out.writeUTF("Entrenador insertado");
            return insertarEntrenador(nombreEnt, paisEnt);

        } else if (this.tabla.equalsIgnoreCase("estadio")) {
            out.writeUTF("Escribe el nombre del estadio");
            String nombreEst = in.readUTF();
            out.writeUTF("Escribe la ciudad del estadio");
            String ciudadEst = in.readUTF();
            out.writeUTF("Estadio insertado");
            System.out.println("el nombre del estadio es " + nombreEst);
            return insertarEstadio(nombreEst, ciudadEst);
        } else {
            System.out.println("Error. No existe la tabla " + this.tabla);
        }
        return -1;
    }

    private int insertarEstadio(String nombreEst, String ciudadEst) throws SQLException {
        if (conexion == null) {
            return -1;
        }
        if (conexion.isClosed()) {
            return -2;
        }
        String consulta = "INSERT INTO estadio(nombre,ciudad)" +
                "VALUES (?,?)";
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        sentencia.setString(1, nombreEst);
        sentencia.setString(2, ciudadEst);
        int numReg=sentencia.executeUpdate();
        if (sentencia!=null) {
            sentencia.close();
        }
        return numReg;
    }

    private int insertarEntrenador(String nombreEnt, String paisEnt) throws SQLException {
        if (conexion == null) {
            return -1;
        }
        if (conexion.isClosed()) {
            return -2;
        }
        String consulta = "INSERT INTO entrenador(nombre,nacionalidad,idEquipo)" +
                " VALUES (?,?,0)";
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        sentencia.setString(1, nombreEnt);
        sentencia.setString(2, paisEnt);

        int numReg=sentencia.executeUpdate();
        if (sentencia!=null) {
            sentencia.close();
        }
        return numReg;
    }

    private int insertarJugador(String nombreJug, String paisJug, String posJug) throws SQLException {
        if (conexion == null) {
            return -1;
        }
        if (conexion.isClosed()) {
            return -2;
        }
        String consulta = "INSERT INTO jugador(nombre,nacionalidad,idEquipo,posicion)" +
                "VALUES (?,?,0,?)";
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        sentencia.setString(1, nombreJug);
        sentencia.setString(2, paisJug);
        sentencia.setString(3, posJug);
        int numReg=sentencia.executeUpdate();
        if (sentencia!=null) {
            sentencia.close();
        }
        return numReg;
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
        String t =  "\n*************************" +
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
        System.out.println("El cliente está accediendo a la tabla " + this.tabla);
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

