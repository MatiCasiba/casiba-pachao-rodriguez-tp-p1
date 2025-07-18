package juego;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entorno.Entorno;

public class Enemigo {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	//private Color color; ya no lo usamos
	private boolean ralentizado;
	private int tiempoRalentizado;
	
    private BufferedImage spriteSheet; // Imagen completa
    private int frameActual = 0;       // Índice de frame (0 a 2)
    private int cantidadFrames = 3;    // Total de frames
    private int ticks = 0;             // Para controlar la velocidad de la animación
    private int velocidadAnimacion = 10; // Menor número = animación más rápida
	
    public Enemigo(int x, int y, int ancho, int alto, /*Color color  ,*/ double velocidad) {
        this.x = x;
        this.y = y;
        // cambio estos valores para agrandar la imagen en pantalla
        this.ancho = 50;
        this.alto = 70;
        this.ralentizado= false;
        this.tiempoRalentizado=0;
       

        try {
            this.spriteSheet = ImageIO.read(getClass().getResource("/image/spriteBat.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    public void dibujar(Entorno entorno) {
        //entorno.dibujarRectangulo(x, y, ancho, alto, 0, color);

        if (spriteSheet == null) return;

        int frameWidth = spriteSheet.getWidth() / cantidadFrames;
        int frameHeight = spriteSheet.getHeight();

        // Actualizar frame cada ciertos ticks
        ticks++;
        if (ticks >= velocidadAnimacion) {
            frameActual = (frameActual + 1) % cantidadFrames;
            ticks = 0;
        }

        BufferedImage frame = spriteSheet.getSubimage(
            frameActual * frameWidth, 0, frameWidth, frameHeight
        );

        entorno.dibujarImagen(frame.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH), x, y, 0);
    }
	
	// función para hacer que los enemigos persigan al personaje
	public void moverHaciaPersonaje(int objetivoX, int objetivoY) {
		//Velocidad normal: 3.0, ralentizado:15 : 1.5
		double velocidadActual=ralentizado ? 1.5 : 3.0;
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
			this.x += (int)(dirX * velocidadActual);
	        this.y += (int)(dirY * velocidadActual);
			
		}
		if (ralentizado) {
			tiempoRalentizado--;
			if (tiempoRalentizado <= 0) {
				ralentizado=false;
			}
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
	public void ralentizar(int duracion) {
		this.ralentizado = true;
		this.tiempoRalentizado=duracion;
	}
	
}
