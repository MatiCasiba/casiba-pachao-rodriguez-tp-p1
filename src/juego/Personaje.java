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
	
	// creo la función para la colisión del personaje con las rocas
	public boolean colisionaCon(Roca roca) {
		// las coordenadas del personaje
		int px1 = this.x - this.ancho / 2;
		int px2 = this.x + this.ancho / 2;
		int py1 = this.y - this.alto / 2;
		int py2 = this.y + this.alto / 2;
		
		// las coordenadas de la roca
		int rx1 = roca.getX() - roca.getAncho() / 2;
		int rx2 = roca.getX() + roca.getAncho() / 2;
		int ry1 = roca.getY() - roca.getAlto() / 2;
		int ry2 = roca.getY() + roca.getAlto() / 2;
		
		//OPCION1
		// condicion de colision
		//boolean seSuperponeHorizontalmente = px1 < rx2 && px2 > rx1;
		//boolean seSuperponeVerticalmente = py1 < ry2 && py2 > ry1;
	
		// voy a devolver true solo si hay superposición en ambos ejes
		//return seSuperponeHorizontalmente && seSuperponeVerticalmente;
		
		//OPCION 2
		return px1 < rx2 && px2 > rx1 && py1 < ry2 && py2 > ry1;
	}
}
