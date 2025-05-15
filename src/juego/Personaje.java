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
	
	// Creo funciones para mover el rectangulo (osea el personaje)
	public void moverDerecha() {
		this.x += 6;
	}
	public void moverIzquierda() {
		this.x -= 6;
	}
	public void moverArriba() {
		this.y -= 6;
	}
	public void moverAbajo() {
		this.y += 6;
	}
	
	// pido los get 
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
	
	// armo las colisiones
	public boolean colisionaPorDerecha(int limiteDerecho) {
		return this.x + this.ancho/2 >= limiteDerecho;
	}
	public boolean colisionaPorIzquierda(Entorno entorno) {
		return this.x - this.ancho/2 <= 0;
	}
	public boolean colisionaPorArriba(Entorno entorno) {
		return this.y - this.alto/2 <= 0;
	}
	public boolean colisionaPorAbajo(Entorno entorno) {
		return this.y + this.alto/2 >= entorno.alto();
	}
}
