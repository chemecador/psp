package ejercicio3;

/***
 * Clase mensaje
 */
public class Mensaje {

    //atributos
    private String año;
    private String incremento;

    //constructores
    public Mensaje(String año, String incremento) {
        this.año = año;
        this.incremento = incremento;
    }
    public Mensaje(){

    }

    //getters y setters
    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public String getIncremento() {
        return incremento;
    }

    public void setIncremento(String incremento) {
        this.incremento = incremento;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "año='" + año + '\'' +
                ", incremento='" + incremento + '\'' +
                '}';
    }
}
