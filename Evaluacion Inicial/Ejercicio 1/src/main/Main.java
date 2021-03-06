package main;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// b = billete, m = moneda, e = euro, c = cents

		int b500 = 0, b200 = 0, b100 = 0, b50 = 0, b20 = 0, b10 = 0, b5 = 0, m2e = 0, m1e = 0, m50c = 0, m20c = 0,
				m10c = 0, m5c = 0, m2c = 0, m1c = 0;

		Scanner in = new Scanner(System.in);

		System.out.println("Introduce cantidad en euros");
		double cantidad = in.nextDouble();
		if (cantidad < 0) {
			System.out.println("La cantidad no puede ser 0");
		}

		while (cantidad >= 500.0) {
			cantidad -= 500;
			b500++;
		}
		if (b500 > 0) {
			System.out.println(b500 + " billetes de 500");
		}
		while (cantidad >= 200.0) {
			cantidad -= 200;
			b200++;
		}
		if (b200 > 0) {
			System.out.println(b200 + " billetes de 200");
		}
		while (cantidad >= 100.0) {
			cantidad -= 100;
			b100++;
		}
		if (b100 > 0) {
			System.out.println(b100 + " billetes de 100");
		}
		while (cantidad >= 50.0) {
			cantidad -= 50;
			b50++;
		}
		if (b50 > 0) {
			System.out.println(b50 + " billetes de 50");
		}
		while (cantidad >= 20.0) {
			cantidad -= 20;
			b20++;
		}
		if (b20 > 0) {
			System.out.println(b20 + " billetes de 20");
		}
		while (cantidad >= 10.0) {
			cantidad -= 10;
			b10++;
		}

		if (b10 > 0) {
			System.out.println(b10 + " billetes de 10");
		}
		while (cantidad >= 5.0) {
			cantidad -= 5;
			b5++;
		}
		if (b5 > 0) {
			System.out.println(b5 + " billetes de 5");
		}
		while (cantidad >= 2.0) {
			cantidad -= 2;
			m2e++;
		}
		if (m2e > 0) {
			System.out.println(m2e + " monedas de 5");
		}
		while (cantidad >= 1.0) {
			cantidad -= 1;
			m1e++;
		}
		if (m1e > 0) {
			System.out.println(m1e + " monedas de 1");
		}
		while (cantidad >= 0.50) {
			cantidad -= 0.50;
			m50c++;
		}
		if (m50c > 0) {
			System.out.println(m50c + " monedas de 50 cents");
		}
		while (cantidad >= 0.20) {
			cantidad -= 0.20;
			m20c++;
		}
		if (m20c > 0) {
			System.out.println(m20c + " monedas de 20 cents");
		}
		while (cantidad >= 0.10) {
			cantidad -= 0.10;
			m10c++;
		}
		if (m10c > 0) {
			System.out.println(m10c + " monedas de 10 cents");
		}
		while (cantidad >= 0.05) {
			cantidad -= 0.05;
			m5c++;
		}
		if (m5c > 0) {
			System.out.println(m50c + " monedas de 5 cents");
		}
		while (cantidad >= 0.02) {
			cantidad -= 0.02;
			m2c++;
		}
		if (m2c > 0) {
			System.out.println(m50c + " monedas de 2 cents");
		}
		while (cantidad >= 0.01) {
			cantidad -= 0.01;
			m1c++;
		}
		if (m1c > 0) {
			System.out.println(m50c + " monedas de 1 cents");
		}
		in.close();
	}
}
