package main;

public class Main {

	public static void main(String[] args) {
		Banco banco = new Banco();
		
		banco.abrirCuenta(2, 10,"AHORRO");
		banco.mostrarCuentas();

	}

}
