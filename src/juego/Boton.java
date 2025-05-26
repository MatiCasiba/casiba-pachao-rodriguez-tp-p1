package juego;
import java.awt.Color;

import entorno.Entorno;

public class Boton {
    private int x;
    private int y;
    private int ancho;
    private int alto;
    //private Color color;
    private String texto;
    private boolean seleccionado;
    
    public Boton(int x, int y, int ancho, int alto, String texto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        //color = Color.green;
        this.texto = texto;
        this.seleccionado = false;
    }
    
    // OPCION 2
    public void dibujar(Entorno entorno) {
        entorno.dibujarRectangulo(x, y, ancho, alto, 0, seleccionado ? Color.yellow : Color.gray);
        entorno.cambiarFont("Arial", 12, Color.black);
        entorno.escribirTexto(texto, x - ancho / 2 + 5, y + 5);
    }
    
    public boolean fueClickeado(int mouseX, int mouseY) {
        return mouseX >= x - ancho / 2 && mouseX <= x + ancho / 2 && mouseY >= y - alto / 2 && mouseY <= y + alto / 2;
    }
    
    public void setSeleccionado(boolean sel) {
        this.seleccionado = sel;
    }
    
    public boolean estaSeleccionado() {
        return seleccionado;
    }
    
    public String getTexto() {
        return texto;
    }

}