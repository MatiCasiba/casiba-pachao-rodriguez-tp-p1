package juego;

import java.awt.Color;

import entorno.Entorno;

public class Boton {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private Color color;
	private boolean sePresionoBoton;
	
	public Boton(int x, int y, int ancho, int alto) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		color = Color.green;
		sePresionoBoton = false;
	}
	
	public void dibujarse(Entorno e) {
		if(sePresionoBoton) {
			e.dibujarRectangulo(x, y, ancho, alto, 0, Color.black);
			e.dibujarCirculo(e.mouseX(), e.mouseY(), 200, Color.cyan); //Esto iria en otra clase?
		} else {
			e.dibujarRectangulo(x, y, ancho, alto, 0, Color.white);
		}
	}
	

	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public int getAncho() {
		return ancho;
	}


	public void setAncho(int ancho) {
		this.ancho = ancho;
	}


	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public boolean isSePresionoBoton() {
		return sePresionoBoton;
	}

	public void setSePresionoBoton(boolean sePresionoBoton) {
		this.sePresionoBoton = sePresionoBoton;
	}

	public boolean mouseEstaSobreElBoton(int mouseX, int mouseY) {
		int ladoIzquierdo = this.x - this.ancho/2;
		int ladoDerecho = this.x + this.ancho/2;
		int ladoSuperior = this.y - this.alto/2;
		int ladoInferior = this.y + this.alto/2;
		
		if (mouseX >= ladoIzquierdo && mouseX <= ladoDerecho && mouseY >= ladoSuperior && mouseY <= ladoInferior) {
			return true;
		} else {
			return false;
		}
		
	}
	
}
