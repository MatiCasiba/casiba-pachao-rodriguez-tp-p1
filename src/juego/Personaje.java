package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Personaje {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private int velocidad = 6; // velocidad base
	//private Color color;
	private Image imagen;
	
	public Personaje(int x, int y, int ancho, int alto, Color color) {
		this.x =x;
		this.y =y;
		this.ancho = ancho;
		this.alto = alto;
		//this.color = color;
		this.imagen = Herramientas.cargarImagen("image/mago.png");
	}
	
	// Dibujo el personaje
	public void dibujar(Entorno entorno) {
		//entorno.dibujarRectangulo(x, y, ancho, alto, 0, color);
		entorno.dibujarImagen(imagen, x, y, 0, 0.1);
	}
	
	public void aumentarVelocidad(double factor) {
        this.velocidad = (int)(velocidad * factor);
    }
	
	// Creo funciones para mover el rectangulo (osea el personaje)
	public void moverDerecha() {
        this.x += velocidad;
    }
    
    public void moverIzquierda() {
        this.x -= velocidad;
    }
    
    public void moverArriba() {
        this.y -= velocidad;
    }
    
    public void moverAbajo() {
        this.y += velocidad;
    }
	
	// pido los get para acceder a los atributos privados
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
	public int getVelocidad() {
	    return velocidad;
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
	public boolean colisionaConRoca(Roca roca) {
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
		
		
		return px1 < rx2 && px2 > rx1 && py1 < ry2 && py2 > ry1;
	}
	
	public boolean colisionaConEnemigo(Enemigo e) {
		// las coordenadas del personaje
		int px1 = this.x - this.ancho / 2;
		int px2 = this.x + this.ancho / 2;
		int py1 = this.y - this.alto / 2;
		int py2 = this.y + this.alto / 2;
		
		// las coordenadas de los enemigos
		int rx1 = e.getX() - e.getAncho() / 2;
		int rx2 = e.getX() + e.getAncho() / 2;
		int ry1 = e.getY() - e.getAlto() / 2;
		int ry2 = e.getY() + e.getAlto() / 2;
		
		return px1 < rx2 && px2 > rx1 && py1 < ry2 && py2 > ry1;
	}
}
