package ejercicio1;

class Hilo extends Thread {
    private String s;

    public Hilo(String s) {
        this.s = s;
    }

    public void run() {
        for (int i = 0; i < 10; i++){
            System.out.print(s + " ");
        }
    }
}

class Ejercicio1 {
    public static void main(String[] args) {
        Hilo t1 = new Hilo("SI");
        Hilo t2 = new Hilo("NO");

        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();
    }
}
class Hilo2 extends Thread {
    private String s;

    public Hilo2(String s) {
        this.s = s;
    }

    public void run() {

        synchronized (getClass()) {
            for (int i = 0; i < 10; i++) {
                System.out.print(s + " ");
                System.out.flush();
                getClass().notifyAll();
                try {
                    getClass().wait();

                } catch (InterruptedException e) {
                    System.err.println("Error en el wait");
                }
            }
            getClass().notifyAll();
        }
    }
}
class Ejercicio2 {
    public static void main(String[] args) {
        Hilo2 t1 = new Hilo2("SI");
        Hilo2 t2 = new Hilo2("NO");

        t1.start();
        t2.start();
    }
}

