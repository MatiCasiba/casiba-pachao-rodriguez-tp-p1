package juego;

import java.awt.Color;

import entorno.Entorno;

public class Hechizo {
    private String nombre;
    private int costo;
    private int radio;
    private Color color;
    private int duracionRalentizacion;
    
    public Hechizo(String nombre, int costo, int radio, Color color, int duracionRalentizacion) {
        this.nombre = nombre;
        this.costo = costo;
        this.radio = radio;
        this.color = color;
        this.duracionRalentizacion=duracionRalentizacion;
    }
    
    public void dibujar(Entorno entorno, int x, int y) {
        entorno.dibujarCirculo(x, y, radio * 2, this.color);
    }
    
    public boolean enRango(int centroX, int centroY, Enemigo enemigo) {
        //obtengo las dimensiones del enemigo
        int ex = enemigo.getX();
        int ey = enemigo.getY();
        int ancho = enemigo.getAncho();
        int alto = enemigo.getAlto();
        
        // calculo la distancia más corta desde el centro del hechizo hasta el rectangulo del enemigo
        int dx = Math.max(Math.abs(centroX - ex) - ancho /2, 0);
        int dy = Math.max(Math.abs(centroY - ey) - alto /2, 0);
        
        // calculo distancia al borde del rectángulo
        double distancia = Math.sqrt(dx * dx + dy * dy);
        
        //Si esta distancia está dentro del radio del hechizo, lña toca aunque sea el borde a borde
        return distancia <= this.radio;
    }
    
    public int getCosto() {
        return costo;
    }
    public String getNombre() {
        return nombre;
    }
    
    //Método para el hehcizo de ralentizacion
    public void aplicarEfectoRalentizacion(Enemigo enemigo) {
        if (this.duracionRalentizacion > 0) {
            enemigo.ralentizar(this.duracionRalentizacion);
        }
    }


}