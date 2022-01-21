package ejercicio1;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Main {
    public static void main(String[] args) throws IOException {

        String direccion_url = "https://raw.githubusercontent.com/bacinger/f1-circuits/master/f1-locations.json"; //Url

        URL pagina = new URL(direccion_url);

        //Se crea el objeto URLConnection
        URLConnection uc = pagina.openConnection();
        uc.connect();
        //Creamos el objeto con el que vamos a leer
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String inputLine;
        String contenido = "";
        Circuito circuito = new Circuito();
        while ((inputLine = in.readLine()) != null) {

            contenido += inputLine + "\n";
            Gson gson = new Gson();
            circuito = gson.fromJson(contenido,Circuito.class);
            circuito.toString();
        }
        in.close();
        //System.out.println(contenido);
    }
}
