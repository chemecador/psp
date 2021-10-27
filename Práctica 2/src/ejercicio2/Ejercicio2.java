package ejercicio2;

class Hilo extends Thread {
    private String s; //string que guarda un verso de la canción

    public Hilo(String s) {
        this.s = s;
    }

    public void run() {
        System.out.println(s); //muestra el contenido del string s por pantalla
    }
}


public class Ejercicio2 {

    public static void main(String[] args) {

        Hilo t1 = new Hilo("Dale a tu cuerpo alegría Macarena");
        Hilo t2 = new Hilo("Que tu cuerpo es pa darle alegría y cosa buena");
        Hilo t3 = new Hilo("Dale a tu cuerpo alegría Macarena");
        Hilo t4 = new Hilo("Heeeeyyyyy Macarena");
        try {
            t1.start(); //comienza el Hilo 1
            t1.join();  //espera a que finalice

            t2.start();
            t2.join();

            t3.start();
            t3.join();

            t4.start();
            t4.join();

        } catch (InterruptedException e) {
            System.err.println("Error en el try");
        }
    }
}
