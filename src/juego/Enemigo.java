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
	
	// función para hacer que los enemigos persigan al personaje
	public void moverHaciaPersonaje(int objetivoX, int objetivoY) {
		
		// calculo cuánto más lejos está el enemigo del personaje X y del Y
		int dx = objetivoX - this.x;
		int dy = objetivoY - this.y;
		
		// calculo la distancia entre el enemigo y el personaje
		double distancia = Math.sqrt(dx*dx + dy*dy);
		if(distancia != 0) {
			
			//voy a evitar la división por cero (en caso de que el enemigo ya está encima del personaje)
			//lo covierto en un vector, osea lo normalizo, este ya no tiene un largo (la magnitud), solo sabe hacia donde ir (una direccion)
			double dirX = dx/distancia;
			double dirY = dy/distancia;
			
			double velocidad = 2.0; // defino la velocidad
			//actulizo la posicon del enemigo en X y en Y con la dirección multiplicadad por la velocidad
			this.x += (int)(dirX * velocidad);
			this.y += (int)(dirY * velocidad);
			
		}
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
