package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);
		ArrayList<Integer> numeros = new ArrayList<Integer>();
		for (int i = 0; i < 6; i++) {
			System.out.println("Introduce el numero " + (i + 1));
			numeros.add(in.nextInt());
		}
		Collections.sort(numeros);
		System.out.println("Los numeros son:" + numeros);
		in.close();
	}

}
