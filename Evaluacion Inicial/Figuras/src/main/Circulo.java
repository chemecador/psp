package main;

public class Circulo extends Figura{

	private double radio;
	
	public Circulo(double radio) {
		super();
		this.radio = radio;
	}

	@Override
	public void dibujar() {
		System.out.println("Dibujando...");
		
	}

}
