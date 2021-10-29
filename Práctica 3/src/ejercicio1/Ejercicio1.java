package ejercicio1;

import java.time.LocalDateTime;

class Notificacion{
    private static Notificacion notificacion = new Notificacion();
    String titulo;
    LocalDateTime hora;

    private Notificacion(){

    }
    private Notificacion(String titulo, LocalDateTime hora){
        this.titulo = titulo;
        this.hora = hora;
    }
    public static Notificacion getInstance(){
        if (notificacion == null){
            notificacion = new Notificacion();
        }
        return notificacion;
    }
}


public class Ejercicio1 {
    /*Se deberá de realizar una aplicación que realice notificaciones a tres clientes cuando exista una
notificación en las redes sociales. Estas notificaciones deberán de informar del título de la nueva
entrada en el blog. Cuando se reciba un correo electrónico se deberá indicar la fecha
(día-mesaño) y la hora (hora:minutos) en la que se ha recibido. */



}
