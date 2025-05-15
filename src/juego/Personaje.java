package juego;

import java.awt.Color;

import entorno.Entorno;

public class Personaje {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private Color color;
	
	public Personaje(int x, int y, int ancho, int alto, Color color) {
		this.x =x;
		this.y =y;
		this.ancho = ancho;
		this.alto = alto;
		this.color = color;
	}
	
	// Dibujo el personaje
	public void dibujar(Entorno entorno) {
		entorno.dibujarRectangulo(x, y, ancho, alto, 0, color);
	}
	
}
