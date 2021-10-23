package main;

import java.util.Scanner;

public class Domicilio {
	
	Scanner in = new Scanner(System.in);
	
	private String direccion;
	private char puerta;
	private int piso;
	private int CP;
	private String localidad;
	private String provincia;

	public Domicilio(){
		
		System.out.println("Direccion: ");
		this.direccion = in.nextLine();
		System.out.println("Puerta: ");
		this.puerta = in.nextLine().charAt(0);
		System.out.println("Piso: ");
		this.piso = in.nextInt();
		System.out.println("CP: ");
		this.CP = in.nextInt();
		in.nextLine();
		System.out.println("Localidad: ");
		this.localidad = in.nextLine();
		System.out.println("Provincia: ");
		this.provincia = in.nextLine();
	}
	
}
