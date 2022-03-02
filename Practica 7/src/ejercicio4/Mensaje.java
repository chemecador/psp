package ejercicio4;

/***
 * Clase mensaje
 */
public class Mensaje {
    //atributos
    private String s;
    private TipoUsuario tu;

    //constructores
    public Mensaje(String s, TipoUsuario tu) {
        this.s = s;
        this.tu = tu;
    }

    public Mensaje() {
    }

    //getters y setters
    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public TipoUsuario getTu() {
        return tu;
    }

    public void setTu(TipoUsuario tu) {
        this.tu = tu;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "mensaje='" + s + '\'' +
                ", Tipo de usuario=" + tu +
                '}';
    }
}
