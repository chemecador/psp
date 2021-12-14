package ejercicio2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    //private ServerSocket ss;
    private DataOutputStream out;
    private DataInputStream in;

    public Servidor() {
        try (ServerSocket ss = new ServerSocket(5555)) {
            Socket client = ss.accept();
            out = new DataOutputStream(client.getOutputStream());
            in = new DataInputStream(client.getInputStream());
            while (true) {
                out.writeUTF("¿Qué año quieres consultar?");
                String año = in.readUTF();
                System.out.println(año);
                if (calcular(año) == null) {
                    out.writeUTF("No hay registros de ese año.");
                } else {
                    if (año.equals("salir")) {
                        String incremento = "¡Hasta pronto!";
                        out.writeUTF(incremento);
                        break;
                    } else {
                        String incremento = "El incremento ha sido de " + calcular(año) + ".";
                        out.writeUTF(incremento);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("IOException en Servidor\n" + ex.toString());
        }
    }

    private static String calcular(String año) {
        if (año.equals("salir")) {
            return año;
        }
        try (BufferedReader br = new BufferedReader
                (new FileReader(new File
                        ("evolucion_anual_de_la_concentracion_de_dioxido_de_carbono_(co2)_en_la_atmosferaNOAA.csv")))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                String lineaAño = linea.substring(0, 4);
                if (lineaAño.equals(año)) {
                    return linea.substring(5);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        new Servidor();
    }
}
