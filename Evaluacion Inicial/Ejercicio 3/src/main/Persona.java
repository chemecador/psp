package main;

import java.util.Scanner;

public class Persona {

	Scanner in = new Scanner(System.in);
	private String nombre;
	private String apellido1;
	private String apellido2;
	private Domicilio domicilio;
	private int telefono;
	private String dni;
	private Coche coche;

	public Persona() {
		System.out.println("Nombre: ");
		this.nombre = in.nextLine();
		System.out.println("Apellido 1: ");
		this.apellido1 = in.nextLine();
		System.out.println("Apellido 2: ");
		this.apellido2 = in.nextLine();
		this.domicilio = new Domicilio();
		System.out.println("Telefono: ");
		this.telefono = in.nextInt();
		in.nextLine();
		System.out.println("DNI: ");
		this.dni = in.nextLine();
		this.coche = new Coche();
		System.out.println("Persona rellenada correctamente");
	}
}
