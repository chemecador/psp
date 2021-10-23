package main;

public class Cuadrado extends Figura {
	
	private double lado;
	
	public Cuadrado(double lado) {
		super();
		this.lado = lado;
	}


	@Override
	public void dibujar() {
		System.out.println("Dibujando...");
		
	}
}
