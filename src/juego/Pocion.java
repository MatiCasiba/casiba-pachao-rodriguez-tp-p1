package juego;

import java.awt.Color;
import entorno.Entorno;

public class Pocion {
    private int x;
    private int y;
    private int ancho;
    private int alto;
    private String tipo; // "vida" o "mana"
    private int cantidad; // cantidad a restaurar
    private Color color;

    public Pocion(int x, int y, String tipo, int cantidad) {
        this.x = x;
        this.y = y;
        this.ancho = 20;
        this.alto = 20;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.color = tipo.equals("vida") ? Color.GREEN : Color.BLUE;
    }

    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(x, y, ancho, alto, 0, color);
    }
    
    public boolean colisionaConPersonaje(Personaje personaje) {
        int px1 = personaje.getX() - personaje.getAncho() / 2;
        int px2 = personaje.getX() + personaje.getAncho() / 2;
        int py1 = personaje.getY() - personaje.getAlto() / 2;
        int py2 = personaje.getY() + personaje.getAlto() / 2;

        int pocionX1 = x - ancho / 2;
        int pocionX2 = x + ancho / 2;
        int pocionY1 = y - alto / 2;
        int pocionY2 = y + alto / 2;

        return px1 < pocionX2 && px2 > pocionX1 && py1 < pocionY2 && py2 > pocionY1;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }
}