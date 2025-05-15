package juego;

import java.awt.Color;

import entorno.Entorno;

public class Enemigo {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private Color color;
	
	public Enemigo(int x, int y, int ancho, int alto, Color color) {
		this. x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.color = color;
	}
	
	public void dibujar (Entorno entorno) {
		entorno.dibujarRectangulo(x, y, ancho, alto, 0, color);
	}
	
	// me traigo getters por si los necesito
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getAncho() {
		return ancho;
	}
	public int getAlto() {
		return alto;
	}
}
