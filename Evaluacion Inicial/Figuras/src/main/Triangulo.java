package main;

public class Triangulo extends Figura {
	private double b;
	private double h;

	public Triangulo(double b, double h) {
		super();
		this.b = b;
		this.h = h;
	}


	@Override
	public void dibujar() {
		System.out.println("Dibujando...");
		
	}
}
