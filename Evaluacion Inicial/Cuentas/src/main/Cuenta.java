package main;

public class Cuenta {
	private String tipo;
	private double saldo;
	private int id;

	public Cuenta(int id, double saldo, String tipo) {
		super();
		this.tipo = tipo;
		this.saldo = saldo;
		this.id = id;
	}
	public void depositarDinero(double saldo) {
		this.saldo+=saldo;
	}
	public void retirarDinero(double saldo) {
		if (saldo > this.saldo) {
			System.out.println("Error, no tienes tanto dinero.");
		}
		else {
			this.saldo -= saldo;
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public double getSaldo() {
		return saldo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
}
