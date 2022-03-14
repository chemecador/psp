package ejercicio4;

import com.google.gson.Gson;
import ejercicio1.Cifrado;

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
    private Cifrado c; //variable de tipo Cifrado para cifrar los mensajes

    //constructor
    public Cliente() {
        try {
            //inicializamos el socket, dis y dos
            conn = new Socket(HOST, PUERTO);
            in = new DataInputStream(conn.getInputStream());
            out = new DataOutputStream(conn.getOutputStream());
            gson = new Gson(); //inicializamos la librería Gson
            m = new Mensaje(); //inicializamos el Mensaje
            c = new Cifrado(); //inicializamos la librería Cifrado

            //llamamos al método que gestiona el inicio de sesión
            registro();
            //bucle infinito del que los diferentes métodos se encargan de gestionar su salida
            while (true) {
                //si la elección de tabla es correcta (no ha elegido salir)...
                if (elegirTabla()) {
                    //se distingue según sea usuario o administrador
                    if (this.tu == TipoUsuario.user) {
                        //leer consulta descifrada con la librería Cifrado, el usuario solo puede consultar datos de la BBDD
                        s = c.descifrar(in.readUTF());
                        m = gson.fromJson(s, Mensaje.class);
                        s = m.getS();
                        System.out.println(s);
                    } else if (this.tu == TipoUsuario.admin) {
                        //llama al método que gestiona lo que puede hacer el administrador
                        adminDo();
                    } else {
                        System.err.println("Error inesperado. ¿No eres ni usuario ni administrador?");
                    }
                } else {
                    //... si no es correcta, sale
                    break;
                }
            }

            //manda al servidor su petición de salir
            m.setS("salir");
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));

            //lee el mensaje de despedida, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //cierra las diferentes conexiones
            conn.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.err.println("Error al conectar el Cliente " + e.toString());
        }
    }

    private void registro() throws IOException {
        //lee del servidor la pregunta de registrarse o iniciar sesión, descifrado con librería Cifrado
        s = c.descifrar(in.readUTF());
        m = gson.fromJson(s, Mensaje.class);
        String opciones = m.getS();

        //escribe al servidor si quiere registrarse o iniciar sesión, cifrado con la librería Cifrado
        s = opcionRegistro(opciones);
        m.setS(s);
        json = gson.toJson(m);
        out.writeUTF(c.cifrar(json));
        if (s.equals("1")) {
            this.tu = TipoUsuario.user;
            gestionarRegistro();
        } else if (s.equals("2")) {
            iniciarSesion();
        } else {
            System.err.println("No ha elegido ni registrarse ni iniciar sesión");
            System.exit(0);
        }

    }

    private void gestionarRegistro() throws IOException {
        //lee del servidor la pregunta de usuario, descifrado con librería Cifrado
        s = c.descifrar(in.readUTF());
        m = gson.fromJson(s, Mensaje.class);
        System.out.println(m.getS());

        m.setS(sc.nextLine());
        json = gson.toJson(m);
        out.writeUTF(c.cifrar(json));

        //lee del servidor la pregunta de contraseña, descifrado con librería Cifrado
        s = c.descifrar(in.readUTF());
        m = gson.fromJson(s, Mensaje.class);
        System.out.println(m.getS());

        m.setS(sc.nextLine());
        json = gson.toJson(m);
        out.writeUTF(c.cifrar(json));

        s = c.descifrar(in.readUTF());
        m = gson.fromJson(s, Mensaje.class);
        System.out.println(m.getS());
    }

    private String opcionRegistro(String opciones) {
        //bucle infinito que no finaliza hasta que el usuario elige la opción correcta
        while (true) {
            //se muestran por pantalla las opciones disponibles
            System.out.println(opciones);
            //se recoge de teclado la opción que elige el administrador
            String opcion = sc.nextLine();
            //si el usuario ha escrito una opción válida...
            if (opcion.equalsIgnoreCase("1") ||
                    opcion.equalsIgnoreCase("registrarse")) {
                //devuelve la opción elegida
                return "1";
            }
            if (opcion.equalsIgnoreCase("2") ||
                    opcion.equalsIgnoreCase("iniciar sesion")) {
                //devuelve la opción elegida
                return "2";
            }
            //si no ha elegido una opción correcta, se vuelve a repetir el proceso
        }
    }

    /**
     * Método que gestiona lo que puede hacer el administrador
     *
     * @throws IOException IOException
     */
    private void adminDo() throws IOException {
        //guardamos las opciones disponibles, descifrado con librería Cifrado, en un string
        s = c.descifrar(in.readUTF());
        m = gson.fromJson(s, Mensaje.class);
        s = m.getS();
        //guardamos la opción elegida por el cliente en otro string
        String opcionElegida = elegirOpcion(s);
        //enviamos al servidor la opción elegida, cifrada con la librería Cifrado
        m.setS(opcionElegida);
        json = gson.toJson(m);
        out.writeUTF(c.cifrar(json));
        //diferenciamos las 3 posibles opciones que tiene el administrador
        if (opcionElegida.equalsIgnoreCase("insertar")) {
            //si ha elegido insertar, nos preparamos para recibir los datos
            insertar();
        } else if (opcionElegida.equalsIgnoreCase("actualizar")) {
            //si ha elegido actualizar, nos preparamos para recibir los datos
            actualizar();
        } else if (opcionElegida.equalsIgnoreCase("eliminar")) {
            //si ha elegido eliminar, nos preparamos para recibir los datos
            eliminar();
        } else {
            System.err.println("Error inesperado. Esa opción no existe");
        }

    }

    /**
     * Método que gestiona qué hacer cuando el cliente decide actualizar.
     * Le envía al servidor los datos que quiere actualizar y se prepara para interactuar con él.
     *
     * @throws IOException IOException
     */
    private void actualizar() throws IOException {
        //se diferencia en función de la tabla que quiere actualizar
        if (this.tabla.equalsIgnoreCase("jugador")) {
            //leer la pregunta del nombre que quieres actualizar, descifrado con librería Cifrado
            s = in.readUTF();
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre viejo, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer la pregunta del nuevo nombre, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre nuevo, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer la pregunta de la nacionalidad, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la nacionalidad, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer la pregunta de la posición, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la posición
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer mensaje de éxito, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);

        } else if (this.tabla.equalsIgnoreCase("entrenador")) {
            //leer la pregunta del nombre viejo, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre viejo, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer la pregunta del nombre nuevo, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre nuevo, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer la pregunta de la nacionalidad, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la nacionalidad, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer mensaje de éxito, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);

        } else if (this.tabla.equalsIgnoreCase("estadio")) {
            //leer la pregunta del nombre viejo, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre viejo, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer la pregunta del nombre nuevo, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre nuevo, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer la pregunta de la ciudad, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la ciudad, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer mensaje de éxito, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);

        } else {
            System.err.println("Error. La tabla " + this.tabla + " no existe.");
        }
    }

    /**
     * Método que gestiona qué hacer cuando el cliente decide eliminar.
     * Le envía al servidor los datos que quiere eliminar y se prepara para interactuar con él.
     *
     * @throws IOException IOException
     */
    private void eliminar() throws IOException {
        //leer la pregunta del nombre que quiere eliminar, descifrado con librería Cifrado
        s = c.descifrar(in.readUTF());
        m = gson.fromJson(s, Mensaje.class);
        s = m.getS();
        System.out.println(s);
        //escribir el nombre que quiere eliminar, cifrada con la librería Cifrado
        m.setS(sc.nextLine());
        json = gson.toJson(m);
        out.writeUTF(c.cifrar(json));
        //leer el mensaje de éxito, descifrado con librería Cifrado
        s = c.descifrar(in.readUTF());
        m = gson.fromJson(s, Mensaje.class);
        s = m.getS();
        System.out.println(s);
    }

    /**
     * Método que gestiona qué hacer cuando el cliente decide insertar.
     * Le envía al servidor los datos que quiere insertar y se prepara para interactuar con él.
     *
     * @throws IOException IOException
     */
    private void insertar() throws IOException {
        //se diferencia en función de la tabla que quiere actualizar
        if (this.tabla.equalsIgnoreCase("jugador")) {
            //leer la pregunta del nombre, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer la pregunta de la nacionalidad, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la nacionalidad, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer la pregunta de la posición, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la posición, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer mensaje de éxito, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);

        } else if (this.tabla.equalsIgnoreCase("entrenador")) {
            //leer la pregunta del nombre, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer la pregunta de la nacionalidad, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la nacionalidad, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer mensaje de éxito, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);

        } else if (this.tabla.equalsIgnoreCase("estadio")) {
            //leer la pregunta del nombre, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir el nombre, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer la pregunta de la ciudad, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            //escribir la ciudad, cifrada con la librería Cifrado
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            //leer mensaje de éxito, descifrado con librería Cifrado
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);

        } else {
            System.err.println("Error. La tabla " + this.tabla + " no existe.");
        }
    }

    /**
     * Método que permite elegir al administrador qué quiere hacer (insertar, actualizar o eliminar)
     *
     * @param opciones Opciones disponibles
     * @return String con la opción elegida
     */
    private String elegirOpcion(String opciones){
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
     *
     * @return True (ha ido bien) False (el usuario ha elegido salir)
     * @throws IOException IOException
     */
    private boolean elegirTabla() throws IOException {
        //lee del servidor las tablas disponibles, descifrado con librería Cifrado
        s = c.descifrar(in.readUTF());
        m = gson.fromJson(s, Mensaje.class);
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
                //envía al servidor la tabla que ha elegido, cifrada con la librería Cifrado
                m.setS(s);
                json = gson.toJson(m);
                out.writeUTF(c.cifrar(json));
                //devuelve true ya que ha funcionado correctamente
                return true;
            }
            if (s.equals("2")) {
                this.tabla = "jugador";
                //envía al servidor la tabla que ha elegido, cifrada con la librería Cifrado
                m.setS(s);
                json = gson.toJson(m);
                out.writeUTF(c.cifrar(json));
                //devuelve true ya que ha funcionado correctamente
                return true;
            }
            if (s.equals("3")) {
                this.tabla = "estadio";
                //envía al servidor la tabla que ha elegido, cifrada con la librería Cifrado
                m.setS(s);
                json = gson.toJson(m);
                out.writeUTF(c.cifrar(json));
                //devuelve true ya que ha funcionado correctamente
                return true;
            }
            if (s.equalsIgnoreCase("entrenador")
                    || s.equalsIgnoreCase("jugador")
                    || s.equalsIgnoreCase("estadio")) {
                //cambia el atributo tabla de esta clase
                this.tabla = s;
                //envía al servidor la tabla que ha elegido, cifrada con la librería Cifrado
                m.setS(s);
                json = gson.toJson(m);
                out.writeUTF(c.cifrar(json));
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
     * Método que comprueba que la contraseña de administrador es correcta
     *
     * @throws IOException IOException
     */
    private void adminComprobacion() throws IOException {
        do {
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);

            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
            m.setS(sc.nextLine());
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
        } while (s.charAt(0) != 'B');
    }

    /**
     * Notifica al servidor que se ha elegido la opción de usuario
     *
     * @throws IOException IOException
     */
    private void userComprobacion() throws IOException {
        do {
            //leo la pregunta de email
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);

            //enviar email
            s = sc.nextLine();
            m.setS(s);
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));

            //lee el mensaje de pedir contraseña
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);

            //enviar contraseña
            s = sc.nextLine();
            m.setS(s);
            json = gson.toJson(m);
            out.writeUTF(c.cifrar(json));

            //leer si es correcto o no
            s = c.descifrar(in.readUTF());
            m = gson.fromJson(s, Mensaje.class);
            s = m.getS();
            System.out.println(s);
        } while (s.charAt(0) != 'B');
    }

    /***
     *
     * Método que muestra los diferentes tipos de usuario que existen y recoge la elección del usuario
     *
     * @throws IOException IOException
     */
    private void iniciarSesion() throws IOException {

        //bucle infinito hasta que se produzca una elección correcta
        s = c.descifrar(in.readUTF());
        m = gson.fromJson(s, Mensaje.class);
        //lee la pregunta (si es usuario o administrador), descifrado con la librería Cifrado
        do {
            System.out.println(m.getS());
            s = sc.nextLine();
        } while (!s.equals("1") && !s.equals("2"));
        m.setS(s);
        json = gson.toJson(m);
        //escribe al cliente la pregunta, cifrado con la librería Cifrado
        out.writeUTF(c.cifrar(json));
        //si es una opción válida de usuario...
        if (s.equals("1")) {
            //se recoge la elección y se devuelve true
            this.tu = TipoUsuario.user;
            userComprobacion();
        }
        //si es una opción válida de admin...
        if (s.equals("2")) {
            //se recoge la elección
            this.tu = TipoUsuario.admin;
            adminComprobacion();
        }
    }

    /**
     * Método principal
     *
     * @param args Args
     */
    public static void main(String[] args) {
        new Cliente();
    }
}
