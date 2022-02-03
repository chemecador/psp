package ejercicio3;

public class Mensaje {
    private String s;
    private TipoUsuario tu;

    public Mensaje(String s, TipoUsuario tu) {
        this.s = s;
        this.tu = tu;
    }

    public Mensaje() {
    }

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
