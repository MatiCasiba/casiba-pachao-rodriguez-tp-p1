package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Menu {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	//private Color colorFondo;
	private Image fondoMenu;
	
	public Menu(int pantallaAncho, int pantallaAlto) {
		this.ancho = 200;
		this.alto = pantallaAlto;
		this.x = pantallaAncho - this.ancho/2;
		this.y = pantallaAlto /2;
		//this.colorFondo = Color.red;
		Image fondoDelMenu = Herramientas.cargarImagen("image/fondoMadera.jpg");
		this.fondoMenu = fondoDelMenu.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
	}
	
	public void dibujar(Entorno entorno) {
        entorno.dibujarImagen(fondoMenu, x, y, 0);
        //entorno.dibujarRectangulo(x, y, ancho, alto, 0, colorFondo);

        // dibuja el recuadro para el título
        int tituloAlto = 50;
        int tituloY = y - alto / 2 + tituloAlto / 2;
        entorno.dibujarRectangulo(x, tituloY, ancho, tituloAlto, 0, new Color(0, 0, 0, 150)); // fondo semitransparente

        // muestra el texto "Hechizos"
        entorno.cambiarFont("Arial", 24, Color.white);
        entorno.escribirTexto("Hechizos", x - 45, tituloY + 8);
    }
	
	public int getAncho() {
		return ancho;
	}
}
