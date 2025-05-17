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
	private Enemigo[] enemigos;
	
	
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
			new Roca(150, 150, 60, 60, Color.gray),
			new Roca(limiteDerecho - 150, 150, 60, 60, Color.gray),
			new Roca(150, entorno.alto() - 150, 60, 60, Color.gray),
			new Roca(limiteDerecho - 150, entorno.alto() - 150, 60, 60, Color.gray),
			new Roca((limiteDerecho)/2, entorno.alto() / 2, 60, 60, Color.gray)
		};
		
		// creando el enemigo y evitando que pasen por encima del menú cuando aparezcan
		this.enemigos = new Enemigo[10]; // creo un arreglo para almacenar 10 enemigos
		int menuXInicio = entorno.ancho() - menu.getAncho(); // calculo dónde empieza el menú para evitar que los enemigos aparezcan encima
		for(int i = 0; i < enemigos.length; i++) {
			int borde = (int)(Math.random()*4); //elijo al azar un borde: 0=izq, 1=der, 2=arriba, 3=abajo
			int ex = 0;
			int ey = 0;
			switch(borde) {
				case 0:
					ex=0;
					ey=(int)(Math.random()* entorno.alto());
					break;
				case 1:
					ex= menuXInicio - 10; // se coloca antes de que empiece el menú
					ey= (int)(Math.random()* entorno.alto());
					break;
				case 2:
					ex= (int)(Math.random()* (menuXInicio -20)); //x aleatorio
					ey= 0;
					break;
				case 3:
					ex= (int)(Math.random()* (menuXInicio - 20));
					ey= entorno.alto();
					break;
			}
			enemigos[i] = new Enemigo(ex, ey, 20, 20, Color.MAGENTA); // creo el enemigo
		}
		
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
		
		// muevo el enemigo hacia el personaje
		for(int i = 0; i < enemigos.length; i++) {
			if (enemigos[i] != null) {
				if (this.personaje.colisionaConEnemigo(enemigos[i])) {
					enemigos[i] = null;
				} else {
					enemigos[i].moverHacia(personaje.getX(), personaje.getY()); // los enemigos persiguen al persoaje
				}
			}
		}
		
	}
	
	public void dibujarObjetos() {
		this.personaje.dibujar(entorno);
		this.menu.dibujar(entorno);
		for(Roca roca : rocas) {
			roca.dibujar(entorno);
		}
		for(Enemigo enemigo : enemigos) {
			if (enemigo != null) {
				enemigo.dibujar(entorno);
			}
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
