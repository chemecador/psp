package main;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {

		int numPersonas = 5;
		ArrayList<Persona> personas = new ArrayList<Persona>();

		for (int i = 0; i < numPersonas; i++) {
			Persona nuevaPersona = new Persona();
			personas.add(nuevaPersona);
		}
	}

}
