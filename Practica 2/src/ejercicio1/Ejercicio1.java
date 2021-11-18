package ejercicio1;

class Hilo extends Thread {
    private String s; //string que guarda "SI" o "NO"

    //constructor
    public Hilo(String s) {
        this.s = s;
    }

    public void run() {
        //bucle que escribe 10 veces el mensaje
        for (int i = 0; i < 10; i++) {
            System.out.print(s + " ");
        }
    }
}

class Ejercicio1 {
    public static void main(String[] args) {
        Hilo t1 = new Hilo("SI");
        Hilo t2 = new Hilo("NO");

        t1.start(); //comienza el Hilo 1 (muestra "SI" 10 veces por pantalla)
        try {
            t1.join(); //espera a que finalice el Hilo 1 antes de continuar
        } catch (InterruptedException e) {
            System.err.println("Error en el Try del Hilo 1");
            e.printStackTrace();
        }
        t2.start(); //comienza el Hilo 2 (muestra "NO" 10 veces por pantalla)
    }
}

class Hilo2 extends Thread {
    private String s; //string que guarda "SI" o "NO"

    public Hilo2(String s) {
        this.s = s;
    }

    public void run() {

        synchronized (getClass()) { //se realiza una tarea a la vez
            for (int i = 0; i < 10; i++) {
                System.out.print(s + " ");
                System.out.flush(); //vacía el buffer
                getClass().notifyAll(); //despierta a los demás procesos
                try {
                    getClass().wait(); //se duerme hasta que le despierte otro proceso
                } catch (InterruptedException e) {
                    System.err.println("Error en el wait");
                }
            }
            getClass().notifyAll(); //despierta a los demás procesos
        }
    }
}

class Ejercicio2 {
    public static void main(String[] args) {
        Hilo2 t1 = new Hilo2("SI");
        Hilo2 t2 = new Hilo2("NO");

        t1.start(); //comienza el Hilo 1
        t2.start(); //comienza el Hilo 2
    }
}

