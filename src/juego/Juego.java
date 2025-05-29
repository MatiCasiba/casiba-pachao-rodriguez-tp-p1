package juego;

import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;

	// Variables y métodos propios de cada grupo
	// ...
	private int energia = 100;
	private int vida = 100;
	private Personaje personaje;
	private Menu menu;
	private Roca[] rocas;
	private Enemigo[] enemigos;
	private int totalEnemigosCreados = 10;
	private int maxEnemigos = 50;
	private int enemigosEliminados = 0;
	private Hechizo[] hechizos;
	private Boton[] botones;
	private Hechizo hechizoSeleccionado = null;

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Gondolf - Land of Bats", 1200, 720);

		// Inicializar lo que haga falta para el juego
		// ...
		this.personaje = new Personaje(entorno.ancho() / 2, 250, 20, 40, Color.white); // 250(posicion) 20(ancho)
																						// 40(alto del rectangulo)
		this.menu = new Menu(entorno.ancho(), entorno.alto()); // voy a iniciar menu después del personaje

		// creo las rocas y lo adapto al espacio de pantalla del juego, para que no
		// toque con el menú
		int limiteDerecho = entorno.ancho() - menu.getAncho();
		this.rocas = new Roca[] { new Roca(150, 150, 60, 60, Color.gray),
				new Roca(limiteDerecho - 150, 150, 60, 60, Color.gray),
				new Roca(150, entorno.alto() - 150, 60, 60, Color.gray),
				new Roca(limiteDerecho - 150, entorno.alto() - 150, 60, 60, Color.gray),
				new Roca((limiteDerecho) / 2, entorno.alto() / 2, 60, 60, Color.gray) };

		// creando el enemigo y evitando que pasen por encima del menú cuando aparezcan
		this.enemigos = new Enemigo[maxEnemigos]; // creo un arreglo para almacenar 50 enemigos
		int menuXInicio = entorno.ancho() - menu.getAncho(); // calculo dónde empieza el menú para evitar que los
																// enemigos aparezcan encima
		for (int i = 0; i < totalEnemigosCreados; i++) { // generamos 10 enemigos
			int borde = (int) (Math.random() * 4); // elijo al azar un borde: 0=izq, 1=der, 2=arriba, 3=abajo
			int ex = 0;
			int ey = 0;
			switch (borde) {
			case 0:
				ex = 0;
				ey = (int) (Math.random() * entorno.alto());
				break;
			case 1:
				ex = menuXInicio - 10; // se coloca antes de que empiece el menú
				ey = (int) (Math.random() * entorno.alto());
				break;
			case 2:
				ex = (int) (Math.random() * (menuXInicio - 20)); // x aleatorio
				ey = 0;
				break;
			case 3:
				ex = (int) (Math.random() * (menuXInicio - 20));
				ey = entorno.alto();
				break;
			}
			enemigos[i] = new Enemigo(ex, ey, 20, 20, Color.MAGENTA); // creo el enemigo
		}

		// viejo
//		this.boton1 = new Boton(entorno.ancho() - 100, entorno.alto() / 2 - 80, 130, 65);
//		this.boton2 = new Boton(entorno.ancho() - 100, entorno.alto() / 2, 130, 65);

		// nuevo
		// Creo los hechizos
		hechizos = new Hechizo[] { new Hechizo("Bomba de Agua", 0, 30, Color.blue),
				new Hechizo("Tormenta de Fuego", 20, 70, Color.red) };
		// Creo los botones
		int menuDerecha = entorno.ancho() - menu.getAncho();
		botones = new Boton[] { new Boton(menuDerecha + 100, 300, 120, 40, "Bomba de agua"),
				new Boton(menuDerecha + 100, 350, 120, 40, "Tormenta de Fuego") };

		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y por lo
	 * tanto es el método más importante de esta clase. Aquí se debe actualizar el
	 * estado interno del juego para simular el paso del tiempo (ver el enunciado
	 * del TP para mayor detalle).
	 */
	public void tick() {
		if (vida <= 0) {
			entorno.cambiarFont("Arial", 50, Color.red);
			entorno.escribirTexto("PERDISTE", entorno.ancho() / 2 - 140, entorno.alto() / 2);
		} else if (enemigosEliminados >= maxEnemigos) {
			entorno.cambiarFont("Arial", 50, Color.green);
			entorno.escribirTexto("GANASTE", entorno.ancho() / 2 - 140, entorno.alto() / 2);
		} else {
			// Procesamiento de un instante de tiempo
			// ...

			// con esto voy a dibujar todos los objetos en pantalla
			this.dibujarObjetos();

			// calculo el limite derecho hasta donde el personaje puede moverse (antes de
			// tocar menú)
			int limiteDerecho = entorno.ancho() - menu.getAncho();

			// asingo teclas para el movimiento del personaje
			if (entorno.estaPresionada(entorno.TECLA_DERECHA) && !personaje.colisionaPorDerecha(limiteDerecho)
					&& !colisionaConRocaAlMover(5, 0)) {
				personaje.moverDerecha();
			}
			if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && !personaje.colisionaPorIzquierda(entorno)
					&& !colisionaConRocaAlMover(-5, 0)) {
				personaje.moverIzquierda();
			}
			if (entorno.estaPresionada(entorno.TECLA_ARRIBA) && !personaje.colisionaPorArriba(entorno)
					&& !colisionaConRocaAlMover(0, -5)) {
				personaje.moverArriba();
			}
			if (entorno.estaPresionada(entorno.TECLA_ABAJO) && !personaje.colisionaPorAbajo(entorno)
					&& !colisionaConRocaAlMover(0, 5)) {
				personaje.moverAbajo();
			}

			int enemigosActivos = 0;
			// muevo enemigos existentes y los cuento
			for (int i = 0; i < enemigos.length; i++) {
				if (enemigos[i] != null) {
					// si colisiona con el personaje, elimino al enemigo
					if (personaje.colisionaConEnemigo(enemigos[i]) /* || hechizoTocaAlEnemigo */) {
						enemigos[i] = null;
						enemigosEliminados++;
						vida -= 10;
					} else {
						enemigos[i].moverHaciaPersonaje(personaje.getX(), personaje.getY());
						enemigosActivos++;
					}
				}
			}
			// si hay menos de 10 enemigos activos y todavia no alcanza el maximo
			for (int i = 0; i < enemigos.length && enemigosActivos < 10 && totalEnemigosCreados < maxEnemigos; i++) {
				if (enemigos[i] == null) {
					enemigos[i] = crearEnemigoAleatorio();
					totalEnemigosCreados++;
					enemigosActivos++;
				}
			}

			// detecto click izquierdo del mouse y que esté dentro del entorno
			if (entorno.mousePresente() && entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
				// quiero obtener las posicio
				int mx = entorno.mouseX();
				int my = entorno.mouseY();

				boolean clicEnBoton = false;

				for (int i = 0; i < botones.length; i++) {
					if (botones[i].fueClickeado(mx, my)) {
						hechizoSeleccionado = hechizos[i];
						for (Boton b : botones)
							b.setSeleccionado(false);
						botones[i].setSeleccionado(true);
						clicEnBoton = true;
						break;
					}
				}

				if (!clicEnBoton && hechizoSeleccionado != null) {
					// verifico si tengo suficiente energia, si no es el gratuito
					if (energia >= hechizoSeleccionado.getCosto()) {
						energia -= hechizoSeleccionado.getCosto();

						// dibuhjo hechizo
						hechizoSeleccionado.dibujar(entorno, mx, my);

						// elimino enemigos en área
						for (int i = 0; i < enemigos.length; i++) {
							if (enemigos[i] != null && hechizoSeleccionado.enRango(mx, my, enemigos[i])) {
								enemigos[i] = null;
								enemigosEliminados++; // con esto debería de contar correctamente
							}
						}

						// reseteo hechizo
						hechizoSeleccionado = null;
						for (Boton b : botones)
							b.setSeleccionado(false);
					}
				}
			}
		}
	}

	public void dibujarObjetos() {
		// Podria hacer una condicional que diga IF (PERSONAJE != NULL && asi con todos
		// los objetos...)
		this.personaje.dibujar(entorno);
		this.menu.dibujar(entorno);
		for (Roca roca : rocas) {
			if (roca != null) {
				roca.dibujar(entorno);
			}
		}
		for (Enemigo enemigo : enemigos) {
			if (enemigo != null) {
				enemigo.dibujar(entorno);
			}
		}

		for (Boton boton : botones) {
			if (boton != null) {
				boton.dibujar(entorno);
			}
		}

		this.entorno.cambiarFont("Arial", 20, Color.white);
		this.entorno.escribirTexto("Maná: " + this.energia, entorno.ancho() - 150, 500);
		this.entorno.escribirTexto("Vida: " + this.vida, entorno.ancho() - 148, 530);
		this.entorno.escribirTexto("Bajas: " + this.enemigosEliminados, entorno.ancho() - 150, 560);

	}

	// me creo una nueva función para las colisiones del personaje con las rocas
	private boolean colisionaConRocaAlMover(int dx, int dy) {
		// voy a calcular la nueva posición simulada del personaje si se moviera (dx,
		// dy) pixeles
		int futuroX = personaje.getX() + dx;
		int futuroY = personaje.getY() + dy;

		// creo un personaje siulado que no se ve en pantalla, este personaje está en la
		// posición futura, pero con el mismo tamaño y color
		Personaje simulado = new Personaje(futuroX, futuroY, personaje.getAncho(), personaje.getAlto(), Color.white);
		// voy a recorrer todas las rocas y veo si colisionaría con alguna
		for (Roca roca : rocas) {
			if (simulado.colisionaConRoca(roca)) {
				return true; // si colisiona con alguna roca, no permito el movimiento
			}
		}
		return false;
	}

	// N U E V O
	// con este metodo me genero un nuevo enemigo en una posición aleatoria desde
	// algún borde de la pantalla
	// me aseguro de que no aparezca encima del menú lateral
	private Enemigo crearEnemigoAleatorio() {
		// elijo aleatoriamente uno de los 4 bordes de la pnatalla
		int borde = (int) (Math.random() * 4);
		// donde guardo la posición X e Y del nuevo enemigo
		int ex = 0;
		int ey = 0;
		// calculo donde va a empezar el menú en x, para no generar enemigos encima de
		// él
		int menuXInicio = entorno.ancho() - menu.getAncho();

		// segun el borde, elijo la posicion adecuada para que aparezca el enemigo
		switch (borde) {
		case 0:
			ex = 0;
			ey = (int) (Math.random() * entorno.alto());
			break;
		case 1:
			ex = menuXInicio - 10; // x justo antes del menú
			ey = (int) (Math.random() * entorno.alto());
			break;
		case 2:
			ex = (int) (Math.random() * (menuXInicio - 20));
			ey = 0;
			break;
		case 3:
			ex = (int) (Math.random() * (menuXInicio - 20));
			ey = entorno.alto();
			break;
		}
		// devuelvo el nuevo objeto enemgio creado en esa posición
		return new Enemigo(ex, ey, 20, 20, Color.magenta);

	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}