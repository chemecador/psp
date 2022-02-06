package ejercicio4;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.*;

public class ClientHandler extends Thread {

    private final String pass = "admin"; //contraseña de administrador
    private DataOutputStream out; //flujo de salida
    private DataInputStream in; //flujo de entrada
    private TipoUsuario tu; //tipo de usuario (usuario o administrador)
    private String tabla; //tabla con la que se va a trabajar
    private String s; //string para leer y escribir mensajes entre cliente y servidor
    private Connection conexion; //conexión con la base de datos
    private Socket socket; //socket con la conexión con el cliente
    private boolean salir; //centinela para controlar el fin del bucle
    private boolean correcta; //si la clave es correcta, true ; si no lo es, false
    private Gson gson; //librería Gson para trabajar con datos json
    private Mensaje m; //mensaje que contiene los datos a enviar entre cliente y servidor
    private String json; //string que contiene el json que se enviará entre cliente y servidro

    //constructor
    public ClientHandler(Socket cliente) {
        //se muestra por pantalla que ha llegado un nuevo cliente
        System.out.println("Conexión establecida con un nuevo cliente.");
        this.socket = cliente;
        try {
            //se inicializan los flujos de entrada y de salida, así como el centinela
            out = new DataOutputStream(cliente.getOutputStream());
            in = new DataInputStream(cliente.getInputStream());
            salir = false;
            gson = new Gson(); //inicializamos la librería Gson
            m = new Mensaje(); //inicializamos el Mensaje
            //se conecta con la base de datos
            conectar();
            //gestiona la identificación (usuario, administrador o salir del programa)
            identificacionS();
            //mientras el cliente no haya elegido salir
            while (!salir) {
                //método que gestiona la elección de tabla con la que se quiere trabajar
                preguntarTabla();
                //si el cliente es un usuario y no ha elegido salir...
                if (this.tu == TipoUsuario.user && !salir) {
                    //se gestiona las consultas que desee hacer
                    userDo();
                    //si el cliente es un administrador y no ha elegido salir...
                } else if (this.tu == TipoUsuario.admin && !salir) {
                    adminDo();
                    //si el cliente no es ni usuario ni administrador...
                } else if (!salir) {
                    System.err.println("Error inesperado al validar usuario/admin en el servidor.");
                }
                //si ha elegido salir en algún momento, sale del bucle
            }
            //en el Mensaje M, establecemos dentro del atributo String s el mensaje que queremos enviar
            //lo convertimos a Json y lo enviamos
            m.setS("¡Hasta pronto!");
            json = gson.toJson(m);
            out.writeUTF(json);
            System.out.println("Conexión con el cliente cerrada");
        } catch (SocketException se) {
            System.out.println("Conexión con el cliente cerrada");
        } catch (IOException ex) {
            System.out.println("IOException en Servidor\n" + ex.toString());
            ex.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQLException en Servidor\n" + e.toString());
            e.printStackTrace();
        } finally {
            try {
                //cierra los flujos de entrada y salida
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Método que gestiona lo que puede hacer el administrador
     *
     * @throws IOException
     * @throws SQLException
     */
    private void adminDo() throws IOException, SQLException {
        //string que guarda las opciones disponibles
        String opciones = "Opciones disponibles:" +
                "\n 1. Insertar" +
                "\n 2. Actualizar" +
                "\n 3. Eliminar" +
                "\n 4. Salir";
        //enviar las opciones disponibles al cliente
        //en el Mensaje M, establecemos dentro del atributo String s el mensaje que queremos enviar
        //lo convertimos a Json y lo enviamos
        m.setS(opciones);
        json = gson.toJson(m);
        out.writeUTF(json);
        //leer la elección (ins, act, eli) del cliente
        s = in.readUTF();
        m = gson.fromJson(s,Mensaje.class);
        s = m.getS();
        //se llama a los métodos correspondientes en función de la elección
        if (s.equalsIgnoreCase("insertar")) {
            insertar();
        } else if (s.equalsIgnoreCase("actualizar")) {
            actualizar();
        } else if (s.equalsIgnoreCase("eliminar")) {
            eliminar();
        } else if (s.equalsIgnoreCase("salir")) {
            salir = true;
        } else {
            System.out.println("Opción no válida");
        }
    }

    /**
     * Método que gestiona la actualización en la base de datos
     *
     * @return Número de registros actualizados
     * @throws IOException
     * @throws SQLException
     */
    private int actualizar() throws IOException, SQLException {
        //se distingue en función de la tabla que quiere actualizar
        if (this.tabla.equalsIgnoreCase("jugador")) {
            //en el Mensaje M, establecemos dentro del atributo String s el mensaje que queremos enviar
            //lo convertimos a Json y lo enviamos
            m.setS("Escribe el nombre del jugador que quieres modificar");
            json = gson.toJson(m);
            out.writeUTF(json);

            //leemos en el String s el mensaje que nos envía el cliente
            //lo convertimos a Mensaje con el método fromJson de la librería Gson
            //en el String s guardamos el contenido del atributo s del Mensaje m
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            String nombreOld = s;
            //idem
            m.setS("Escribe el nuevo nombre del jugador");
            json = gson.toJson(m);
            out.writeUTF(json);
            //idem
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            String nombreNew = s;

            m.setS("Escribe la nueva nacionalidad del jugador");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            String paisJug = s;

            m.setS("Escribe la nueva posición del jugador");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            String posJug = s;

            m.setS("Jugador actualizado");
            json = gson.toJson(m);
            out.writeUTF(json);
            //una vez recogidos los datos, se llama al método que trabajará con la base de datos
            return actualizarJugador(nombreOld, nombreNew, paisJug, posJug);

        } else if (this.tabla.equalsIgnoreCase("entrenador")) {
            //idem
            m.setS("Escribe el nombre del entrenador que quieres modificar");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();

            String nombreOld = s;
            m.setS("Escribe el nuevo nombre del entrenador");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();

            String nombreNew = s;
            m.setS("Escribe la nueva nacionalidad del entrenador");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            String paisEnt = s;

            m.setS("Entrenador actualizado");
            json = gson.toJson(m);
            out.writeUTF(json);
            return actualizarEntrenador(nombreOld, nombreNew, paisEnt);

        } else if (this.tabla.equalsIgnoreCase("estadio")) {
            //idem
            m.setS("Escribe el nombre del estadio que quieres modificar");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            String nombreOld = s;

            m.setS("Escribe el nuevo nombre del estadio");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();

            String nombreNew = s;
            m.setS("Escribe la nueva ciudad del estadio");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();

            String ciudadEst = s;
            m.setS("Estadio actualizado");
            json = gson.toJson(m);
            out.writeUTF(json);
            return actualizarEstadio(nombreOld, nombreNew, ciudadEst);
        } else {
            System.out.println("Error. No existe la tabla " + this.tabla);
        }
        //si ha habido algún error, devuelve -1
        return -1;
    }

    /**
     * Método que actualiza un estadio en la base de datos
     *
     * @param nombreOld Nombre del estadio viejo
     * @param nombreNew Nombre del estadio nuevo
     * @param ciudadEst Ciudad del estadio
     * @return Número de registros actualizados
     * @throws SQLException
     */
    private int actualizarEstadio(String nombreOld, String nombreNew, String ciudadEst) throws SQLException {
        //conectar con la base de datos
        conectar();
        if (conexion == null) {
            return -1;
        }
        if (conexion.isClosed()) {
            return -2;
        }
        //si no ha habido errores, se crea una consulta
        String consulta = "UPDATE estadio " +
                " SET nombre = ?, ciudad = ? WHERE nombre = ?";
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        sentencia.setString(1, nombreNew);
        sentencia.setString(2, ciudadEst);
        sentencia.setString(3, nombreOld);
        //se sustituyen los datos en la consulta y se ejecuta
        int numReg = sentencia.executeUpdate();
        if (sentencia != null) {
            sentencia.close();
        }
        desconectar();
        //se desconecta de la base de datos y se devuelve el número de registros afectados
        return numReg;
    }


    /**
     * Método que actualiza un entrenador en la base de datos. Su funcionamiento es idéntico al de un estadio.
     *
     * @param nombreOld Nombre del entrenador viejo
     * @param nombreNew Nombre del entrenador nuevo
     * @param paisEnt   Ciudad del entrenador
     * @return Número de registros actualizados
     * @throws SQLException
     */
    private int actualizarEntrenador(String nombreOld, String nombreNew, String paisEnt) throws SQLException {
        conectar();
        if (conexion == null) {
            return -1;
        }
        if (conexion.isClosed()) {
            return -2;
        }
        String consulta = "UPDATE entrenador " +
                " SET nombre = ?, nacionalidad = ? WHERE nombre = ?";
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        sentencia.setString(1, nombreNew);
        sentencia.setString(2, paisEnt);
        sentencia.setString(3, nombreOld);
        int numReg = sentencia.executeUpdate();
        if (sentencia != null) {
            sentencia.close();
        }
        desconectar();
        return numReg;
    }

    /**
     * Método que actualiza un jugador en la base de datos. Su funcionamiento es idéntico al de un estadio.
     *
     * @param nombreOld Nombre del jugador viejo
     * @param nombreNew Nombre del jugador nuevo
     * @param paisJug   Nacionalidad del entrenador
     * @param posJug    Posición del jugador
     * @return Número de registros actualizados
     * @throws SQLException
     */
    private int actualizarJugador(String nombreOld, String nombreNew, String paisJug, String posJug) throws SQLException {
        conectar();
        if (conexion == null) {
            return -1;
        }
        if (conexion.isClosed()) {
            return -2;
        }
        String consulta = "UPDATE jugador " +
                " SET nombre = ?, nacionalidad = ?, posicion = ? WHERE nombre = ?";
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        sentencia.setString(1, nombreNew);
        sentencia.setString(2, paisJug);
        sentencia.setString(3, posJug);
        sentencia.setString(4, nombreOld);
        int numReg = sentencia.executeUpdate();
        if (sentencia != null) {
            sentencia.close();
        }
        desconectar();
        return numReg;
    }

    /**
     * Método que elimina un dato de la base de datos.
     *
     * @return Número de registros eliminados.
     * @throws IOException
     * @throws SQLException
     */
    private int eliminar() throws IOException, SQLException {
        //se distingue en función de la tabla que se desea eliminar
        if (this.tabla.equalsIgnoreCase("jugador")) {
            m.setS("Escribe el nombre del jugador que deseas eliminar");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
        } else if (this.tabla.equalsIgnoreCase("entrenador")) {
            m.setS("Escribe el nombre del entrenadore que deseas eliminar");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
        } else if (this.tabla.equalsIgnoreCase("estadio")) {
            m.setS("Escribe el nombre del estadio que deseas eliminar");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
        } else {
            System.out.println("Error. No existe la tabla " + this.tabla);
        }
        //una vez recogido el dato que se desea eliminar, se notifica y se invoca al método encargado de hacerlo
        m.setS(s + " ya no se encuentra en la tabla " + this.tabla);
        json = gson.toJson(m);
        out.writeUTF(json);
        return eliminar(s);
    }

    /**
     * Método que se encarga de eliminar un dato introducido como parámetro de la base de datos.
     *
     * @param nombre Dato a eliminar
     * @return Número de registros eliminados
     * @throws SQLException
     */
    private int eliminar(String nombre) throws SQLException {
        //conexión con la base de datos
        conectar();
        if (conexion == null) {
            return -1;
        }
        if (conexion.isClosed()) {
            return -2;
        }
        //si no ha habido errores, se crea la consulta
        String consulta = "DELETE FROM " + this.tabla + " WHERE nombre=?";
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        sentencia.setString(1, nombre);
        //se sustituye el nombre en la consulta y se ejecuta
        int numRegistros = sentencia.executeUpdate();
        if (sentencia != null) {
            conexion.close();
        }
        desconectar();
        //se desconecta de la base de datos y se devuelve el número de registros afectados
        return numRegistros;
    }

    /**
     * Método que gestiona las consultas de un usuario
     *
     * @throws SQLException
     * @throws IOException
     */
    private void userDo() throws SQLException, IOException {
        System.out.println("He llegado a userDo");
        if (this.tabla.equalsIgnoreCase("entrenador")
                || this.tabla.equalsIgnoreCase("1")) {
            //si la tabla es entrenador, escribe al cliente la consulta que le ha devuelto el método correspondiente
            m.setS(leerEntrenadores(consultar()));
            json = gson.toJson(m);
            out.writeUTF(json);
        } else if (this.tabla.equalsIgnoreCase("jugador")
                || this.tabla.equalsIgnoreCase("2")) {
            //idem si es un jugador
            m.setS(leerJugadores(consultar()));
            json = gson.toJson(m);
            out.writeUTF(json);
        } else if (this.tabla.equalsIgnoreCase("estadio")
                || this.tabla.equalsIgnoreCase("3")) {
            //idem si es un estadio
            m.setS(leerEntrenadores(consultar()));
            json = gson.toJson(m);
            out.writeUTF(json);
        } else if (this.tabla.equalsIgnoreCase("salir")
                || this.tabla.equalsIgnoreCase("4")) {
            //si el usuario ha decidido salir, pone el centinela a true y no vuelve a iterar el bucle
            salir = true;
        } else {
            System.err.println("Error inesperado al elegir tabla en el servidor.");
        }
    }

    /**
     * Método encargado de insertar un dato en la base de datos
     *
     * @return Devuelve el número de datos insertados
     * @throws IOException
     * @throws SQLException
     */
    private int insertar() throws IOException, SQLException {
        if (this.tabla.equalsIgnoreCase("jugador")) {
            //idem
            m.setS("Escribe el nombre del jugador");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            String nombreJug = s;

            m.setS("Escribe la nacionalidad del jugador");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            String paisJug = s;

            m.setS("Escribe la posición del jugador");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            String posJug = s;

            m.setS("Jugador insertado");
            json = gson.toJson(m);
            out.writeUTF(json);
            //se recogen los datos insertados por el cliente y se invoca al método correspondiente
            return insertarJugador(nombreJug, paisJug, posJug);

        } else if (this.tabla.equalsIgnoreCase("entrenador")) {
            //idem
            m.setS("Escribe el nombre del entrenador");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            String nombreEnt = s;
            //idem
            m.setS("Escribe la nacionalidad del entrenador");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            String paisEnt = s;

            m.setS("Entrenador insertado");
            json = gson.toJson(m);
            out.writeUTF(json);
            //se recogen los datos insertados por el cliente y se invoca al método correspondiente
            return insertarEntrenador(nombreEnt, paisEnt);

        } else if (this.tabla.equalsIgnoreCase("estadio")) {
            //idem
            m.setS("Escribe el nombre del estadio");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();

            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            String nombreEst = s;

            m.setS("Escribe la ciudad del estadio");
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            String ciudadEst = s;

            m.setS("Estadio insertado");
            json = gson.toJson(m);
            out.writeUTF(json);
            //se recogen los datos insertados por el cliente y se invoca al método correspondiente
            return insertarEstadio(nombreEst, ciudadEst);
        } else {
            System.out.println("Error. No existe la tabla " + this.tabla);
        }
        //ha habido algún error, se devuelve -1
        return -1;
    }

    /**
     * Método que inserta un estadio con los datos introducidos como parámetro
     *
     * @param nombreEst Nombre del estadio a insertar
     * @param ciudadEst Nombre de la ciudad a insertar
     * @return
     * @throws SQLException
     */
    private int insertarEstadio(String nombreEst, String ciudadEst) throws SQLException {
        //conecta con la base de datos
        conectar();
        if (conexion == null) {
            return -1;
        }
        if (conexion.isClosed()) {
            return -2;
        }
        //string que guarda la consulta de inserción
        String consulta = "INSERT INTO estadio(nombre,ciudad)" +
                "VALUES (?,?)";
        PreparedStatement sentencia = conexion.prepareStatement(consulta);
        sentencia.setString(1, nombreEst);
        sentencia.setString(2, ciudadEst);
        int numReg = sentencia.executeUpdate();
        //se guarda el número de registros afectados en un entero y se ejecuta la consulta
        if (sentencia != null) {
            sentencia.close();
        }
        desconectar();
        //se devuelve el número de registros
        return numReg;
    }

    /**
     * Método que inserta un entrenador con los datos introducidos como parámetro.
     * Su funcionamiento es idéntico al de un estadio.
     *
     * @param nombreEnt Nombre del entrenador a insertar
     * @param paisEnt   Nombre de la nacionalidad a insertar
     * @return Devuelve el número de registros afectados
     * @throws SQLException
     */
    private int insertarEntrenador(String nombreEnt, String paisEnt) throws SQLException {
        conectar();
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

        int numReg = sentencia.executeUpdate();
        System.out.println(numReg + " datos insertados");
        if (sentencia != null) {
            sentencia.close();
        }
        return numReg;
    }

    /**
     * Método que inserta un jugador con los datos introducidos como parámetro.
     * Su funcionamiento es idéntico al de un estadio.
     *
     * @param nombreJug Nombre del jugador a insertar
     * @param paisJug   Nombre de la nacionalidad del jugador a insertar
     * @param posJug    Posición del jugador a insertar
     * @return Devuelve el número de registros afectados
     * @throws SQLException
     */
    private int insertarJugador(String nombreJug, String paisJug, String posJug) throws SQLException {
        conectar();
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
        int numReg = sentencia.executeUpdate();
        if (sentencia != null) {
            sentencia.close();
        }
        desconectar();
        return numReg;
    }

    /**
     * Método que devuelve un string con los estadios disponibles en la base de datos
     *
     * @param consultar Consulta que ha devuelto la base de datos
     * @return String con los estadios disponibles en la base de datos
     * @throws SQLException
     */
    private String leerEstadios(ResultSet consultar) throws SQLException {
        //conecta con la base de datos
        conectar();
        //iterador
        int i = 1;
        //string que contiene todos los estadios
        String estadios = "";
        //mientras queden estadios en la consulta...
        estadios += "Estos son los estadios que se encuentran en nuestra base de datos:\n";
        while (consultar.next()) {
            estadios += "Estadio número " + i + ":";
            estadios += "\n  Nombre: " + consultar.getString(2);
            estadios += "\n  Ciudad: " + consultar.getString(3);
            estadios += "\n";
            i++;
        }
        desconectar();
        //desconecta de la base de datos y devuelve el string correspondiente
        return estadios;
    }

    /**
     * Método que devuelve un string con los entrenadores disponibles en la base de datos.
     * Su funcionamiento es idéntico al de estadios.
     *
     * @param consultar Consulta que ha devuelto la base de datos
     * @return String con los entrenadores disponibles en la base de datos
     * @throws SQLException
     */
    private String leerEntrenadores(ResultSet consultar) throws SQLException {
        conectar();
        int i = 1;
        String entrenadores = "";
        entrenadores += "Estos son los entrenadores que se encuentran en nuestra base de datos:\n";
        while (consultar.next()) {
            entrenadores += "Entrenador número " + i + ":";
            entrenadores += "\n  Nombre: " + consultar.getString(2);
            entrenadores += "\n  Nacionalidad: " + consultar.getString(3);
            entrenadores += "\n";
            i++;
        }
        desconectar();
        return entrenadores;
    }

    /**
     * Método que devuelve un string con los jugadores disponibles en la base de datos.
     * Su funcionamiento es idéntico al de estadios.
     *
     * @param consultar Consulta que ha devuelto la base de datos
     * @return String con los jugadores disponibles en la base de datos
     * @throws SQLException
     */
    private String leerJugadores(ResultSet consultar) throws SQLException {
        conectar();
        int i = 1;
        String jugadores = "";
        jugadores += "Estos son los jugadores que se encuentran en nuestra base de datos:\n";
        while (consultar.next()) {
            jugadores += "Jugador número " + i + ":";
            jugadores += "\n  Nombre: " + consultar.getString(2);
            jugadores += "\n  Nacionalidad: " + consultar.getString(3);
            jugadores += "\n  Posición: " + consultar.getString(5);
            jugadores += "\n";
            i++;
        }
        desconectar();
        return jugadores;
    }

    /**
     * Método que realiza la consulta en la base de datos.
     *
     * @return
     */
    private ResultSet consultar() {
        //conecta con la base de datos
        conectar();
        //realiza la consulta de la tabla actual
        String consulta = "SELECT * FROM " + this.tabla;
        PreparedStatement sentencia = null;
        try {
            //realiza la consulta y la ejecuta
            sentencia = conexion.prepareStatement(consulta);
            //devuelve el resultado de la consulta
            return sentencia.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error al consultar + " + e.toString());
        }
        desconectar();
        //ha habido un error; desconecta y devuelve null.
        return null;
    }

    /**
     * Método que conecta con la base de datos.
     */
    private void conectar() {
        this.conexion = null;
        try {
            //conexión con localhost a través de mi puerto
            this.conexion = DriverManager.getConnection("jdbc:mysql://localhost:3307/liga"
                    , "root", "");
        } catch (SQLException e) {
            System.out.println("No se ha podido conectar con la base de datos. \n" + e.toString());
        }
    }

    /**
     * Método que desconecta de la base de datos
     */
    private void desconectar() {
        try {
            conexion.close();
        } catch (SQLException e) {
            System.out.println("No he podido desconectar de la base de datos");
            e.printStackTrace();
        }
    }

    /**
     * Método que pregunta la tabla con la que se desea trabajar y guarda
     * la elección del cliente.
     *
     * @throws IOException
     */
    private void preguntarTabla() throws IOException {
        s = "\n*************************" +
                "\n¿Qué tabla quieres consultar?" +
                "\n1. Entrenador" +
                "\n2. Jugador" +
                "\n3. Estadio" +
                "\n4. Salir" +
                "\n*************************";
        //envía al cliente el string que contiene las tablas disponibles
        m.setS(s);
        json = gson.toJson(m);
        out.writeUTF(json);

        //lee la tabla elegida por el cliente
        s = in.readUTF();
        m = gson.fromJson(s,Mensaje.class);
        s = m.getS();
        //si la tabla es salir...
        if (s.equals("salir")) {
            //...pone el centinela a true para que salga del bucle
            this.salir = true;
            //sale del método y no sigue ejecutando el código que hay debajo
            return;
        }
        //convierte la tabla a minúsculas
        this.tabla = s.toLowerCase();
        //si ha elegido la tabla de manera numérica, la transforma a texto
        if (s.equals("1")) {
            this.tabla = "entrenador";
        }
        if (s.equals("2")) {
            this.tabla = "jugador";
        }
        if (s.equals("3")) {
            this.tabla = "estadio";
        }
        //notifica por pantalla en el servidor la tabla con la que está trabajando el cliente
        System.out.println("El cliente está accediendo a la tabla " + this.tabla);
    }

    /**
     * Método que gestiona la comprobación de si el cliente es usuario o administrador
     *
     * @return True (comprobación correcta) False (el cliente ha elegido salir)
     * @throws IOException
     */
    private void identificacionS() throws IOException {
        m.setS("¡Buenas! ¿Eres un usuario o un administrador?");
        json = gson.toJson(m);
        //escribe al cliente la pregunta
        out.writeUTF(json);

        //lee la respuesta (si es usuario o administrador)
        s = in.readUTF();
        m = gson.fromJson(s,Mensaje.class);
        s = m.getS();
        //si es administrador...
        if (s.equals("admin")) {
            //comprueba si es correcta
            correcta = false;
            String user = "";
            String pass = "";
            while (!correcta) {
                //pregunta user y pass
                m.setS("Introduce el email:");
                json = gson.toJson(m);
                out.writeUTF(json);

                s = in.readUTF();
                m = gson.fromJson(s,Mensaje.class);
                user = m.getS();

                m.setS("Introduce la contraseña:");
                json = gson.toJson(m);
                out.writeUTF(json);

                s = in.readUTF();
                m = gson.fromJson(s,Mensaje.class);
                pass = m.getS();

                correcta = comprobarClaveAdmin(user, pass);
                //notifica si es correcta o no
                if (correcta) {
                    m.setS("Bienvenido, " + user);
                    json = gson.toJson(m);
                    out.writeUTF(json);
                } else {
                    m.setS("Email o contraseña incorrectos.\n");
                    json = gson.toJson(m);
                    out.writeUTF(json);
                }
            }
            //una vez es correcta, se señala en el atributo de la clase
            this.tu = TipoUsuario.admin;

        } else if (s.equals("user")) {
            //idem
            correcta = false;
            String user = "";
            String pass = "";
            while (!correcta) {
                //pregunta user y pass
                m.setS("Introduce el email:");
                json = gson.toJson(m);
                out.writeUTF(json);

                s = in.readUTF();
                m = gson.fromJson(s,Mensaje.class);
                user = m.getS();

                m.setS("Introduce la contraseña:");
                json = gson.toJson(m);
                out.writeUTF(json);

                s = in.readUTF();
                m = gson.fromJson(s,Mensaje.class);
                pass = m.getS();

                correcta = comprobarClaveUser(user, pass);
                if (correcta) {
                    m.setS("Bienvenido, " + user);
                    json = gson.toJson(m);
                    out.writeUTF(json);
                } else {
                    m.setS("Email o contraseña incorrectos.\n");
                    json = gson.toJson(m);
                    out.writeUTF(json);
                }
            }
            this.tu = TipoUsuario.user;
        } else if (s.equals("salir")) {
            //devuelve falso y pone salir a true
            this.salir = true;
        } else {
            //nunca debería entrar aquí, pero ha habido un error
            System.err.println("Error inesperado. ");
            this.salir = true;
        }

    }

    /**
     * Método que comprueba si el email y la contraseña del admin son correctos
     *
     * @param email Email del administrador
     * @param pass  Contraseña del administrador
     * @return True (es correcto), False (no es correcto)
     */
    private boolean comprobarClaveAdmin(String email, String pass) {
        //conecta con la base de datos
        conectar();
        //realiza la consulta de la tabla actual
        String consulta = "SELECT * FROM usuario WHERE email = \'" + email
                + "\' AND password = \'" + pass + "\' AND es_administrador = 1";
        PreparedStatement sentencia = null;
        try {
            //realiza la consulta y la ejecuta
            sentencia = conexion.prepareStatement(consulta);
            ResultSet res = sentencia.executeQuery();
            return res.next();
        } catch (SQLException e) {
            System.out.println("Error al consultar + " + e.toString());
        }
        desconectar();
        //ha habido un error; desconecta y devuelve null.
        return false;
    }

    /**
     * Método que comprueba si el email y la contraseña del usuario son correctos
     *
     * @param email Email del usuario
     * @param pass  Contraseña del usuario
     * @return True (es correcto), False (no es correcto)
     */
    private boolean comprobarClaveUser(String email, String pass) {
        //conecta con la base de datos
        conectar();
        //realiza la consulta de la tabla actual
        String consulta = "SELECT * FROM usuario WHERE email = \'" + email + "\' AND password = \'" + pass + "\'";
        PreparedStatement sentencia = null;
        try {
            //realiza la consulta y la ejecuta
            sentencia = conexion.prepareStatement(consulta);
            ResultSet res = sentencia.executeQuery();
            return res.next();
        } catch (SQLException e) {
            System.out.println("Error al consultar + " + e.toString());
        }
        desconectar();
        //ha habido un error; desconecta y devuelve null.
        return false;
    }
}

