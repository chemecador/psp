package main;

import java.awt.geom.*;

import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class UsoThreads {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JFrame marco = new MarcoRebote();

		marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		marco.setVisible(true);

	}

}

class PelotaHilos implements Runnable {

	public PelotaHilos(Pelota unaPelota, Component unComponente) {
		this.pelota = unaPelota;
		this.componente = unComponente;
	}

	private Pelota pelota;
	private Component componente;

	@Override
	public void run() {

		System.out.println("Estado del hilo al comenzar: " + Thread.currentThread().isInterrupted());
		// for (int i = 1; i <= 3000; i++) {

		while (!Thread.interrupted()) {
			pelota.mueve_pelota(componente.getBounds());

			componente.paint(componente.getGraphics());

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// System.out.println("Hilo bloqueado.");
				// e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("Estado del hilo al terminar: " + Thread.currentThread().isInterrupted());

	}

}

//Movimiento de la pelota-----------------------------------------------------------------------------------------

class Pelota {

	// Mueve la pelota invirtiendo posici?n si choca con l?mites

	public void mueve_pelota(Rectangle2D limites) {

		x += dx;

		y += dy;

		if (x < limites.getMinX()) {

			x = limites.getMinX();

			dx = -dx;
		}

		if (x + TAMX >= limites.getMaxX()) {

			x = limites.getMaxX() - TAMX;

			dx = -dx;
		}

		if (y < limites.getMinY()) {

			y = limites.getMinY();

			dy = -dy;
		}

		if (y + TAMY >= limites.getMaxY()) {

			y = limites.getMaxY() - TAMY;

			dy = -dy;

		}

	}

	// Forma de la pelota en su posici?n inicial

	public Ellipse2D getShape() {

		return new Ellipse2D.Double(x, y, TAMX, TAMY);

	}

	private static final int TAMX = 15;

	private static final int TAMY = 15;

	private double x = 0;

	private double y = 0;

	private double dx = 1;

	private double dy = 1;

}

// L?mina que dibuja las pelotas----------------------------------------------------------------------

class LaminaPelota extends JPanel {

	// A?adimos pelota a la l?mina

	public void add(Pelota b) {

		pelotas.add(b);
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		for (Pelota b : pelotas) {

			g2.fill(b.getShape());
		}

	}

	private ArrayList<Pelota> pelotas = new ArrayList<Pelota>();
}

//Marco con l?mina y botones------------------------------------------------------------------------------

class MarcoRebote extends JFrame {

	public MarcoRebote() {

		setBounds(600, 300, 600, 350);

		setTitle("Rebotes");

		lamina = new LaminaPelota();

		add(lamina, BorderLayout.CENTER);

		JPanel laminaBotones = new JPanel();

		arrancar = new JButton("Arrancar1");
		arrancar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				comienza_el_juego(evento);
			}
		});
		arrancar2 = new JButton("Arrancar2");
		arrancar2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				comienza_el_juego(evento);
			}
		});
		arrancar3 = new JButton("Arrancar3");
		arrancar3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				comienza_el_juego(evento);
			}
		});
		detener = new JButton("Detener1");
		detener.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				detener(evento);
			}
		});
		detener2 = new JButton("Detener2");
		detener2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				detener(evento);
			}
		});
		detener3 = new JButton("Detener3");
		detener3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				detener(evento);
			}
		});
		laminaBotones.add(arrancar);
		laminaBotones.add(arrancar2);
		laminaBotones.add(arrancar3);
		laminaBotones.add(detener);
		laminaBotones.add(detener2);
		laminaBotones.add(detener3);
		

		add(laminaBotones, BorderLayout.SOUTH);
	}

	// A?ade pelota y la bota 1000 veces

	public void comienza_el_juego(ActionEvent e) {

		Pelota pelota = new Pelota();

		lamina.add(pelota);

		Runnable r = new PelotaHilos(pelota, lamina);
		if (e.getSource().equals(arrancar)) {
			t1 = new Thread(r);
			t1.start();
		}
		else if (e.getSource().equals(arrancar2)) {
			t2 = new Thread(r);
			t2.start();
		}
		else if (e.getSource().equals(arrancar3)) {
			t3 = new Thread(r);
			t3.start();
		}

	}

	public void detener(ActionEvent e) {
		if (e.getSource().equals(detener)) {
			t1.interrupt();
		}
		else if (e.getSource().equals(detener2)) {
			t2.interrupt();
		}
		else if (e.getSource().equals(detener3)) {
			t3.interrupt();
		}
		
	}

	Thread t1,t2,t3;
	JButton arrancar,arrancar2,arrancar3,
			detener,detener2,detener3;
	private LaminaPelota lamina;

}
