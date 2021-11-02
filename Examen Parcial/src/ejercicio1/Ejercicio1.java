package ejercicio1;


class Hilo extends Thread {

    private int numHilo;

    public Hilo(int numHilo) {
        this.numHilo = numHilo;
    }


    public void run() {
        //la i cuenta las 10 veces que tiene que escribir cada hilo su frase
        //la j cuenta los 10 números que muestra cada hilo
        for (int i = 0; i < 10; i++) {
            if (this.numHilo == 1) {
                for (int j = 1; j < 11; j++) {
                    System.out.println("Hilo " + this.numHilo + " vale " + j);
                }
                break;
            } else if (this.numHilo == 2) {
                for (int j = 11; j < 21; j++) {
                    System.out.println("Hilo " + this.numHilo + " vale " + j);
                }
                break;
            } else if (this.numHilo == 3) {
                for (int j = 21; j < 31; j++) {
                    System.out.println("Hilo " + this.numHilo + " vale " + j);
                }
                break;
            } else { // nunca debería entrar aquí, pero si se invoca con otros números, contará también
                int j = this.numHilo * 10;
                int acabar = j+10;
                while (j < acabar) {
                    System.out.println("Hilo " + this.numHilo + " vale " + (j+1));
                    j++;
                }
                break;

            }
        }

    }
}

public class Ejercicio1 {
    public static void main(String[] args) {
        Hilo hilo1 = new Hilo(1);
        Hilo hilo2 = new Hilo(2);
        Hilo hilo3 = new Hilo(5);

        hilo1.start();
        try {
            hilo1.join();
            hilo2.start();
            hilo2.join();
            hilo3.start();
            hilo3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
