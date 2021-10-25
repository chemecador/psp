package ejercicio5;

class Dado extends Thread {
    static int CANTIDAD_LANZAMIENTOS = 15000;
    int parImpar;

    public int getDado() {
        return parImpar;
    }

    public void run() {
        for (int i = 0; i < CANTIDAD_LANZAMIENTOS; i++) {
            if ((int) Math.floor(Math.random() * 6 + 1) % 2 == 0)
                parImpar++;
            else
                parImpar--;

        }
        System.out.println("RESULTADO " + parImpar);
    }
}


public class Ejercicio5 {
    public static int CANTIDAD_DADOS = 5;

    public static void main(String[] args) throws InterruptedException {
        Dado[] dados = new Dado[CANTIDAD_DADOS];
        for (int i = 0; i < CANTIDAD_DADOS; i++) {
            dados[i] = new Dado();
            dados[i].start();
        }
        Boolean seguir = true;

        while (seguir) {
            seguir = false;
            for (int i = 0; i < CANTIDAD_DADOS; i++) {
                if (dados[i].isAlive()) {
                    seguir = true;
                }
            }
        }
        int resultadoFinal = 0;
        for (int i = 0; i < CANTIDAD_DADOS; i++) {
            if (dados[i].getDado() > 0)
                resultadoFinal++;
            else
                resultadoFinal--;
        }
        if (resultadoFinal > 0)
            System.out.println("HA SALIDO PAR");
        else
            System.out.println("HA SALIDO IMPAR");

    }
}
