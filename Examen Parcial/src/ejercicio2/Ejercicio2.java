package ejercicio2;

class Coche extends Thread {

    static int DISTANCIA_TOTAL = 99;

    double distanciaPorIteracion;
    double distanciaRecorrida;

    public Coche(double distancia) {
        this.distanciaPorIteracion = distancia;
        this.distanciaRecorrida = 0;
    }

    public double getDistanciaPorIteracion() {
        return distanciaPorIteracion;
    }

    public void setDistanciaPorIteracion(double distanciaPorIteracion) {
        this.distanciaPorIteracion = distanciaPorIteracion;
    }

    public double getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    public void setDistanciaRecorrida(double distanciaRecorrida) {
        this.distanciaRecorrida = distanciaRecorrida;
    }

    public void run() {

        while (distanciaRecorrida < DISTANCIA_TOTAL) {
            try {
                Thread.currentThread().sleep(1); //le damos tiempo al procesador para contar
            } catch (InterruptedException e) {
                System.err.println("Error en el sleep");
                e.printStackTrace();
            }
            distanciaRecorrida += distanciaPorIteracion;
        }
        distanciaRecorrida = DISTANCIA_TOTAL; //el coche se para al llegar a la línea de meta
    }
}

public class Ejercicio2 {

    public static void main(String[] args) {

        Coche redBulo = new Coche(1.1);
        Coche mariMerche = new Coche(1);
        Coche fuelarre = new Coche(0.75);

        redBulo.start();
        mariMerche.start();
        fuelarre.start();

        String primero = "";
        String segundo = "";
        String tercero = "";


        while (true) {
            if (!redBulo.isAlive()) {
                //mariMerche.stop();
                //fuelarre.stop();
                primero = "Red Bulo";
                if (mariMerche.getDistanciaRecorrida() > fuelarre.getDistanciaRecorrida()) {
                    segundo = "MariMerche";
                    tercero = "Fuelarre";
                } else if (mariMerche.getDistanciaRecorrida() < fuelarre.getDistanciaRecorrida()) {
                    segundo = "Fuelarre";
                    tercero = "MariMerche";
                } else {
                    System.out.println("El ganador es Red Bulo, Mari Merche y Fuelarre han llegado a la vez");
                }
                break;
            } else if (!mariMerche.isAlive()) {
                //redBulo.stop();
                //mariMerche.stop();
                primero = "MariMerche";
                if (redBulo.getDistanciaRecorrida() > fuelarre.getDistanciaRecorrida()) {
                    segundo = "Red Bulo";
                    tercero = "Fuelarre";
                } else if (redBulo.getDistanciaRecorrida() < fuelarre.getDistanciaRecorrida()) {
                    segundo = "Fuelarre";
                    tercero = "Red Bulo";
                } else {
                    System.out.println("El ganador es MariMerche, Red Bulo y Fuelarre han llegado a la vez");
                }
                break;
            } else if (!fuelarre.isAlive()) {
                //redBulo.stop();
                //mariMerche.stop();
                primero = "Fuelarre";
                if (mariMerche.getDistanciaRecorrida() > redBulo.getDistanciaRecorrida()) {
                    segundo = "MariMerche";
                    tercero = "Red Bulo";
                } else if (mariMerche.getDistanciaRecorrida() < redBulo.getDistanciaRecorrida()) {
                    segundo = "Red Bulo";
                    tercero = "MariMerche";
                } else {
                    System.out.println("El ganador es Fuelarre, Mari Merche y Red Bulo han llegado a la vez");
                }
                break;
            }
        }
        if (!segundo.equals(tercero)) {
            System.out.println("El ganador es " + primero + ", el segundo " + segundo + " y el tercero " + tercero + ".");
        }
        System.out.println("Red Bulo ha recorrido " + redBulo.getDistanciaRecorrida());
        System.out.println("MariMerche ha recorrido " + mariMerche.getDistanciaRecorrida());
        System.out.println("Fuelarre ha recorrido " + fuelarre.getDistanciaRecorrida());
    }
}
