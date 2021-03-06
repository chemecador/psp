package main;

import java.util.ArrayList;

public class Banco {
	ArrayList<Cuenta> cuentas;

	public Banco() {
		super();
		this.cuentas = new ArrayList<Cuenta>();
	}

	public void abrirCuenta(int id, double saldo, String tipo) {
		Cuenta nuevaCuenta = new Cuenta(id, saldo, tipo);
		cuentas.add(nuevaCuenta);
	}

	public void cerrarCuenta(Cuenta cuenta) {

		cuentas.remove(cuentas.indexOf(cuenta));
	}

	public void mostrarCuentas() {
		int i = 1;
		for (Cuenta cuenta : cuentas) {
			System.out.println("Cuenta n?mero " + i + ": " + cuenta.getId());
			i++;
		}
	}
}
