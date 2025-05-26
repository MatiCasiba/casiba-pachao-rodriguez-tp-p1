package juego;

import java.awt.Color;

import entorno.Entorno;

public class Hechizo {
    String nombre;
    int costo;
    int radio;
    Color color;
    
    public Hechizo(String nombre, int costo, int radio, Color color) {
        this.nombre = nombre;
        this.costo = costo;
        this.radio = radio;
        this.color = color;
    }
    
    public void dibujar(Entorno entorno, int x, int y) {
        entorno.dibujarCirculo(x, y, radio * 2, this.color);
    }
    
    public boolean enRango(int hechizoX, int hechizoY, Enemigo enemigo) {
        double dx = hechizoX - enemigo.getX();
        double dy = hechizoY - enemigo.getY();
        return Math.sqrt(dx * dx + dy * dy) <= radio;
    }
    
    public int getCosto() {
        return costo;
    }
    public String getNombre() {
        return nombre;
    }

}