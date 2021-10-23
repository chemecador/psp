package main;

import java.util.Scanner;

public class Coche {
	
	Scanner in = new Scanner (System.in);
	
	private String marca;
	private String modelo;
	private String matricula;
	private int cilindrada;
	
	public Coche() {
		System.out.println("Marca: ");
		this.marca = in.nextLine();
		System.out.println("Modelo: ");
		this.modelo = in.nextLine();
		System.out.println("Matricula: ");
		this.matricula = in.nextLine();
		System.out.println("Cilindrada: ");
		this.cilindrada = in.nextInt();
	}
	
}
