package juego;


import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	
	// Variables y métodos propios de cada grupo
	// ...
	private Personaje personaje;
	private Menu menu;
	private Roca[] rocas;
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Gondolf - Land of Bats", 1200, 720);
		
		// Inicializar lo que haga falta para el juego
		// ...
		this.personaje = new Personaje(entorno.ancho()/2, 250, 20, 40, Color.white); //250(posicion) 20(ancho) 40(alto del rectangulo)
		this.menu = new Menu(entorno.ancho(), entorno.alto()); // voy a iniciar menu después del personaje
		
		// creo las rocas y lo adapto al espacio de pantalla del juego, para que no toque con el menú
		int limiteDerecho = entorno.ancho() - menu.getAncho();
		this.rocas = new Roca[] {
			new Roca(150, 100, 50, 50, Color.gray),
			new Roca(limiteDerecho - 150, 100, 50, 50, Color.gray),
			new Roca(150, entorno.alto() - 100, 50, 50, Color.gray),
			new Roca(limiteDerecho - 150, entorno.alto() - 100, 50, 50, Color.gray),
			new Roca((limiteDerecho)/2, entorno.alto() / 2, 60, 60, Color.gray)
		};
		
		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
		// Procesamiento de un instante de tiempo
		// ...
		
		// con esto voy a dibujar todos los objetos en pantalla
		this.dibujarObjetos();
		
		// calculo el limite derecho hasta donde el personaje puede moverse (antes de tocar menú)
		int limiteDerecho = entorno.ancho() - menu.getAncho();
		
		// asingo teclas para el movimiento del personaje
		if(entorno.estaPresionada(entorno.TECLA_DERECHA) && !personaje.colisionaPorDerecha(limiteDerecho) && !colisionaConRocaAlMover(5, 0)) {
			personaje.moverDerecha();
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && !personaje.colisionaPorIzquierda(entorno) && !colisionaConRocaAlMover(-5, 0)) {
			personaje.moverIzquierda();
		}
		if(entorno.estaPresionada(entorno.TECLA_ARRIBA) && !personaje.colisionaPorArriba(entorno) && !colisionaConRocaAlMover(0, -5)) {
			personaje.moverArriba();
		}
		if(entorno.estaPresionada(entorno.TECLA_ABAJO) && !personaje.colisionaPorAbajo(entorno) && !colisionaConRocaAlMover(0, 5)) {
			personaje.moverAbajo();
		}
		
	}
	
	public void dibujarObjetos() {
		this.personaje.dibujar(entorno);
		this.menu.dibujar(entorno);
		for(Roca roca : rocas) {
			roca.dibujar(entorno);
		}
	}
	
	// me creo una nueva función para las colisiones del personaje con las rocas
	private boolean colisionaConRocaAlMover(int dx, int dy) {
		// voy a calcular la nueva posición simulada del personaje si se moviera (dx, dy) pixeles
		int futuroX = personaje.getX() + dx;
		int futuroY = personaje.getY() + dy;
		
		// creo un personaje siulado que no se ve en pantalla, este personaje está en la posición futura, pero con el mismo tamaño y color
		Personaje simulado = new Personaje(futuroX, futuroY, personaje.getAncho(), personaje.getAlto(), Color.white);
		// voy a recorrer todas las rocas y veo si colisionaría con alguna
		for(Roca roca : rocas) {
			if(simulado.colisionaCon(roca)) {
				return true; // si colisiona con alguna roca, no permito el movimiento
			}
		}
		return false;
	}
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
