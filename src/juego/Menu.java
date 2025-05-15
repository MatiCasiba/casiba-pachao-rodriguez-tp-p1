package juego;

import java.awt.Color;

import entorno.Entorno;

public class Menu {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private Color colorFondo;
	
	public Menu(int pantallaAncho, int pantallaAlto) {
		this.ancho = 200;
		this.alto = pantallaAlto;
		this.x = pantallaAncho - this.ancho/2;
		this.y = pantallaAlto /2;
		this.colorFondo = Color.red; // lo coloco en rojo para distinguir con la pantalla de juego
	}
	
	public void dibujar(Entorno entorno) {
		entorno.dibujarRectangulo(x, y, ancho, alto, 0, colorFondo);
	}
	
	public int getAncho() {
		return ancho;
	}
}
