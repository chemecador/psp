package main;

public abstract class Figura {
	private int x;
	private int y;
	
	public void mover(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public abstract void dibujar();

}