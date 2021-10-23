package main;

public class Main {

	public static void main(String[] args) {
		
		Circulo circulo = new Circulo(3.0);
		Rectangulo rectangulo = new Rectangulo(2.0, 4.0);
		Triangulo triangulo = new Triangulo (2.0, 5.0);
		Cuadrado cuadrado = new Cuadrado (2.0);
		
		circulo.dibujar();
		rectangulo.dibujar();
		triangulo.dibujar();
		cuadrado.dibujar();

		
	}

}
