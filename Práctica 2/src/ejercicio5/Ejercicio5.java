package ejercicio5;

class Dado extends Thread {
    static int CANTIDAD_LANZAMIENTOS = 15000;
    int parImpar; //si es mayor que 0, es par; si es menor que 0, es impar

    public int getDado() {
        return parImpar;
    }

    public void run() {
        for (int i = 0; i < CANTIDAD_LANZAMIENTOS; i++) {
            if ((int) Math.floor(Math.random() * 6 + 1) % 2 == 0) //aleatorio entre 1 y 6
                parImpar++; //es par, se suma 1
            else
                parImpar--; //es impar, se resta 1
        }
        System.out.println("RESULTADO " + parImpar);
    }
}


public class Ejercicio5 {
    public static int CANTIDAD_DADOS = 5;

    public static void main(String[] args) {
        Dado[] dados = new Dado[CANTIDAD_DADOS];
        for (int i = 0; i < CANTIDAD_DADOS; i++) {
            dados[i] = new Dado();
            dados[i].start(); //iniciar los dados
        }
        Boolean seguir = true; //booleano que controla si se debe salir del bucle

        while (seguir) {
            seguir = false; //por defecto, no hay que seguir
            for (int i = 0; i < CANTIDAD_DADOS; i++) {
                if (dados[i].isAlive()) {
                    seguir = true; //queda algún hilo vivo, hay que seguir
                }
            }
        }
        int resultadoFinal = 0;
        for (int i = 0; i < CANTIDAD_DADOS; i++) {
            if (dados[i].getDado() > 0) //el hilo i ha salido par
                resultadoFinal++; //sumamos 1 al contador de pares
            else //el hilo i ha salido impar
                resultadoFinal--; //sumamos 1 al contador de impares
        }
        if (resultadoFinal > 0) //han salido más pares que impares
            System.out.println("HA SALIDO PAR");
        else if (resultadoFinal < 0) //han salido más impares que pares
            System.out.println("HA SALIDO IMPAR");
        else //es imposible que llegue aquí con una tirada impar
            System.out.println("EMPATE, VUELVE A LANZAR LOS DADOS");

    }
}
