package ejercicio2;

public class Mensaje {

    String año;
    String incremento;

    public Mensaje(String año, String incremento) {
        this.año = año;
        this.incremento = incremento;
    }

    public Mensaje() {
    }

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
