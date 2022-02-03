package ejercicio3;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Clase cliente
 */
public class Cliente {

    private TipoUsuario tu; //usuario o admin
    private Socket conn; //socket con la conexión
    private DataInputStream in; //flujo de entrada
    private DataOutputStream out; //flujo de salida
    private final int PUERTO = 5555; //puerto que se utilizará
    private final String HOST = "localhost"; //dirección IP (local)
    private String tabla; //tabla en la que se consultará
    private static Scanner sc = new Scanner(System.in); //Scanner para leer de teclado
    private String s; //string que contiene los mensajes que se envían y se reciben
    private Gson gson; //librería Gson para trabajar con datos json
    private Mensaje m; //mensaje que contiene los datos a enviar entre cliente y servidor
    private String json; //string que contiene el json que se enviará entre cliente y servidro
    //constructor
    public Cliente() {
        try {
            //inicializamos el socket, dis y dos
            conn = new Socket(HOST, PUERTO);
            in = new DataInputStream(conn.getInputStream());
            out = new DataOutputStream(conn.getOutputStream());
            gson = new Gson(); //inicializamos la librería Gson
            m = new Mensaje(); //inicializamos el Mensaje
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            //leemos mensaje de bienvenida
            System.out.println(s);
            //llamamos al método que gestiona el inicio de sesión

            /*  entra en el if() si el cliente dice que es usuario o admin,
                no entra si el usuario presiona salir*/

            if (iniciarSesion()) {
                //bucle infinito del que los diferentes métodos se encargan de gestionar su salida
                while (true) {
                    //si la elección de tabla es correcta (no ha elegido salir)...
                    if (elegirTabla()) {
                        //se distingue según sea usuario o administrador
                        if (this.tu == TipoUsuario.user) {
                            //leer consulta, el usuario solo puede consultar datos de la BBDD
                            s = in.readUTF();
                            m = gson.fromJson(s,Mensaje.class);
                            s = m.getS();
                            System.out.println(s);
                        } else if (this.tu == TipoUsuario.admin) {
                            //llama al método que gestiona lo que puede hacer el administrador
                            adminDo();
                        } else {
                            System.out.println("Error inesperado. ¿No eres ni usuario ni administrador?");
                        }
                    } else {
                    //... si no es correcta, sale
                        break;
                    }
                }
            }
            //manda al servidor su petición de salir
            m.setS("salir");
            json = gson.toJson(m);
            out.writeUTF(json);
            //lee el mensaje de despedida
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //cierra las diferentes conexiones
            conn.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Error al conectar el Cliente " + e.toString());
        }
    }

    /**
     * Método que gestiona lo que puede hacer el administrador
     *
     * @throws IOException
     */
    private void adminDo() throws IOException {
        //guardamos las opciones disponibles en un string
        s = in.readUTF();
        m = gson.fromJson(s,Mensaje.class);
        s = m.getS();
        //guardamos la opción elegida por el cliente en otro string
        String opcionElegida = elegirOpcion(s);
        //enviamos al servidor la opción elegida
        m.setS(opcionElegida);
        json = gson.toJson(m);
        out.writeUTF(json);
        //diferenciamos las 3 posibles opciones que tiene el administrador
        if (opcionElegida.equalsIgnoreCase("insertar")) {
            //si ha elegido insertar, nos preparamos para recibir los datos
            insertar();
        } else if (opcionElegida.equalsIgnoreCase("actualizar")){
            //si ha elegido actualizar, nos preparamos para recibir los datos
            actualizar();
        } else if (opcionElegida.equalsIgnoreCase("eliminar")) {
            //si ha elegido eliminar, nos preparamos para recibir los datos
            eliminar();
        } else {
            System.out.println("Error inesperado. Esa opción no existe");
        }


    }

    /**
     * Método que gestiona qué hacer cuando el cliente decide actualizar.
     * Le envía al servidor los datos que quiere actualizar y se prepara para interactuar con él.
     *
     * @throws IOException
     */
    private void actualizar() throws IOException {
        //se diferencia en función de la tabla que quiere actualizar
        if (this.tabla.equalsIgnoreCase("jugador")) {
            //leer la pregunta del nombre que quieres actualizar
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre viejo
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer la pregunta del nuevo nombre
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre nuevo
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer la pregunta de la nacionalidad
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la nacionalidad
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer la pregunta de la posición
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la posición
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer mensaje de éxito
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);

        } else if (this.tabla.equalsIgnoreCase("entrenador")) {
            //leer la pregunta del nombre viejo
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre viejo
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer la pregunta del nombre nuevo
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre nuevo
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer la pregunta de la nacionalidad
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la nacionalidad
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer mensaje de éxito
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);

        } else if (this.tabla.equalsIgnoreCase("estadio")) {
            //leer la pregunta del nombre viejo
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre viejo
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer la pregunta del nombre nuevo
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre nuevo
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer la pregunta de la ciudad
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la ciudad
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer mensaje de éxito
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);

        } else {
            System.out.println("Error. La tabla " + this.tabla + " no existe.");
        }
    }
    /**
     * Método que gestiona qué hacer cuando el cliente decide eliminar.
     * Le envía al servidor los datos que quiere eliminar y se prepara para interactuar con él.
     *
     * @throws IOException
     */
    private void eliminar() throws IOException {
        //leer la pregunta del nombre que quiere eliminar
        s = in.readUTF();
        m = gson.fromJson(s,Mensaje.class);
        s = m.getS();
        System.out.println(s);
        //escribir el nombre que quiere eliminar
        m.setS(sc.nextLine());
        json = gson.toJson(m);
        out.writeUTF(json);
        //leer el mensaje de éxito
        s = in.readUTF();
        m = gson.fromJson(s,Mensaje.class);
        s = m.getS();
        System.out.println(s);
    }
    /**
     * Método que gestiona qué hacer cuando el cliente decide insertar.
     * Le envía al servidor los datos que quiere insertar y se prepara para interactuar con él.
     *
     * @throws IOException
     */
    private void insertar() throws IOException {
        //se diferencia en función de la tabla que quiere actualizar
        if (this.tabla.equalsIgnoreCase("jugador")) {
            //leer la pregunta del nombre
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer la pregunta de la nacionalidad
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la nacionalidad
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer la pregunta de la posición
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la posición
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer mensaje de éxito
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);

        } else if (this.tabla.equalsIgnoreCase("entrenador")) {
            //leer la pregunta del nombre
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer la pregunta de la nacionalidad
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la nacionalidad
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer mensaje de éxito
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);

        } else if (this.tabla.equalsIgnoreCase("estadio")) {
            //leer la pregunta del nombre
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer la pregunta de la ciudad
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la ciudad
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            //leer mensaje de éxito
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);

        } else {
            System.out.println("Error. La tabla " + this.tabla + " no existe.");
        }
    }

    /**
     * Método que permite elegir al administrador qué quiere hacer (insertar, actualizar o eliminar)
     * @param opciones Opciones disponibles
     * @return String con la opción elegida
     * @throws IOException
     */
    private String elegirOpcion(String opciones) throws IOException {
        //bucle infinito que no finaliza hasta que el usuario elige la opción correcta
        while (true) {
            //se muestran por pantalla las opciones disponibles
            System.out.println(opciones);
            //se recoge de teclado la opción que elige el administrador
            String opcion = sc.nextLine();
            //si el usuario ha escrito una opción válida...
            if (opcion.equalsIgnoreCase("insertar") ||
                    opcion.equalsIgnoreCase("actualizar") ||
                    opcion.equalsIgnoreCase("eliminar")) {
                //devuelve la opción elegida
                return opcion;
            }
            //si ha elegido un número, lo transforma en texto y lo devuelve
            if (opcion.equals("1")) {
                return "insertar";
            }
            if (opcion.equals("2")) {
                return "actualizar";
            }
            if (opcion.equals("3")) {
                return "eliminar";
            }
            //si no ha elegido una opción correcta, se vuelve a repetir el proceso
        }
    }

    /**
     * Método que permite elegir la tabla (entrenador, jugador, estadio) con la que se quiere interactuar
     * @return True (ha ido bien) False (el usuario ha elegido salir)
     * @throws IOException
     */
    private boolean elegirTabla() throws IOException {
        //lee del servidor las tablas disponibles
        String opciones = m.getS();
        //muestra las tablas disponibles por pantalla
        System.out.println(opciones);
        //lee de teclado la tabla que elige el cliente
        s = sc.nextLine();
        while (true) {
            //si la tabla es correcta
            if (s.equals("1")) {
                this.tabla = "entrenador";
                //envía al servidor la tabla que ha elegido
                m.setS(s);
                json = gson.toJson(m);
                out.writeUTF(json);
                //devuelve true ya que ha funcionado correctamente
                return true;
            }
            if (s.equals("2")) {
                this.tabla = "jugador";
                //envía al servidor la tabla que ha elegido
                m.setS(s);
                json = gson.toJson(m);
                out.writeUTF(json);
                //devuelve true ya que ha funcionado correctamente
                return true;
            }
            if (s.equals("3")) {
                this.tabla = "estadio";
                //envía al servidor la tabla que ha elegido
                m.setS(s);
                json = gson.toJson(m);
                out.writeUTF(json);
                //devuelve true ya que ha funcionado correctamente
                return true;
            }
            if (s.equalsIgnoreCase("entrenador")
                    || s.equalsIgnoreCase("jugador")
                    || s.equalsIgnoreCase("estadio")) {
                //cambia el atributo tabla de esta clase
                this.tabla = s;
                //envía al servidor la tabla que ha elegido
                m.setS(s);
                json = gson.toJson(m);
                out.writeUTF(json);
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
            //se repite el bucle de manera infinita hasta que se elija una opción correcta
        }
    }

    /**
     * Método que comprueba si la identificación es correcta
     *
     * @throws IOException
     */
    private void identificacionC() throws IOException {
        //distingue si es usuario o administrador
        if (this.tu == TipoUsuario.user) {
            //si es usuario, llama al método
            userComprobacion();
        } else if (this.tu == TipoUsuario.admin) {
            //
            adminComprobacion();
        } else {
            System.err.println("Error inesperado. ¡¿No eres usuario ni administrador?!");
        }
    }

    /**
     * Método que comprueba que la contraseña de administrador es correcta
     * @throws IOException
     */
    private void adminComprobacion() throws IOException {
        //envía al servidor su petición de logearse como administrador
        m.setS(this.tu.toString());
        m.setTu(this.tu);
        json = gson.toJson(m);
        out.writeUTF(json);
        do{
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);

            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            System.out.println(s);
        } while (s.charAt(0) != 'B');
    }

    /**
     * Notifica al servidor que se ha elegido la opción de usuario
     * @throws IOException
     */
    private void userComprobacion() throws IOException {
        //envía al servidor un mensaje con su Tipo de Usuario (usuario)
        m.setS(this.tu.toString());
        json = gson.toJson(m);
        out.writeUTF(json);
        //lee el mensaje de pedir contraseña
        do {
            //leer el mensaje
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //leer de teclado y enviar
            s = sc.nextLine();
            m.setS(s);
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
            s = sc.nextLine();
            m.setS(s);
            json = gson.toJson(m);
            out.writeUTF(json);
            s = in.readUTF();
            m = gson.fromJson(s,Mensaje.class);
            s = m.getS();
            System.out.println(s);
        } while (s.charAt(0) != 'B');
    }

    /**
     * Método que muestra los diferentes tipos de usuario que existen y recoge la elección del usuario
     * @return True (elección correcta) False (el cliente quiere salir)
     */
    private boolean iniciarSesion() throws IOException {

        //bucle infinito hasta que se produzca una elección correcta
        while (true) {
            //se muestran por pantalla las opciones disponibles
            System.out.println("\n1. Usuario" +
                    "\n2. Administrador" +
                    "\n3. Salir");
            //se recoge en un string la elección por teclado del cliente
            s = sc.nextLine();
            //si es una opción válida de usuario...
            if (s.equals("1") || s.equalsIgnoreCase("usuario")
                    || s.equalsIgnoreCase("user")) {
                //se recoge la elección y se devuelve true
                this.tu = TipoUsuario.user;
                userComprobacion();
                return true;
            }
            //si es una opción válida de admin...
            if (s.equals("2") || s.equalsIgnoreCase("admin")
                    || s.equalsIgnoreCase("administrador")) {
                //se recoge la elección y se devuelve true
                this.tu = TipoUsuario.admin;
                adminComprobacion();
                return true;
            }
            //si el cliente decide salir...
            if (s.equals("3") || s.equalsIgnoreCase("salir")) {
                //devuelve falso
                return false;
            }
            //se muestran las opciones y vuelve a empezar
            System.out.println(s + " no es correcto. Las opciones son: ");
        }
    }

    /**
     * Método principal
     * @param args
     */
    public static void main(String[] args) {
        new Cliente();
    }
}
