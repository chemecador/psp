package main;

public class Rectangulo extends Figura{
	
	private double b;
	private double h;
	

	public Rectangulo(double b, double h) {
		super();
		this.b = b;
		this.h = h;
	}


	@Override
	public void dibujar() {
		System.out.println("Dibujando...");
		
	}
}
