package ejercicio1;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/***
 * Clase Principal
 */
public class Main {
    public static void main(String[] args) throws IOException {

        //guardamos la URL en un string
        String url = "https://raw.githubusercontent.com/bacinger/f1-circuits/master/f1-locations.json";
        //creamos una variable de tipo URL que contiene la dirección web del JSON
        URL pagina = new URL(url);
        //Se crea el objeto URLConnection y conectamos con la página web
        URLConnection uc = pagina.openConnection();
        uc.connect();
        //Creamos el objeto con el que vamos a leer
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        //creamos los dos String para leer el JSON
        String inputLine;
        String circuitosStr = "";
        //recorremos la web y guardamos los datos en un string
        while ((inputLine = in.readLine()) != null) {
            circuitosStr += inputLine;

        }
        //ya tenemos todos los circuitos guardados en el String circuitosStr, cerramos el flujo
        in.close();

        //creamos variable de tipo Gson una vez importada su librería
        Gson gson = new Gson();
        //para crear una instancia de TypeToken, lo tipamos con la lista de Circuito, invocamos a su método getType
        //y ya tenemos nuestra instancia de Type
        Type tipoCircuito = new TypeToken<ArrayList<Circuito>>() {}.getType();
        //guardamos todos los circuitos en un arraylist de Circuito, que hemos creado con la librería gson
        ArrayList<Circuito> circuitos = gson.fromJson(circuitosStr, tipoCircuito);
        //recorremos el array de circuitos...
        for (Circuito c : circuitos) {
            //...y los mostramos por pantalla con el método toString()
            System.out.println(c.toString());
        }
    }
}
