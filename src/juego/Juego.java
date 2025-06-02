package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;

	// Variables y métodos propios de cada grupo
	// ...
	private Image fondo;

	private int energia = 100;
	private int vida = 100;
	private Personaje personaje;

	private Menu menu;
	private Roca[] rocas;

	private Enemigo[] enemigos;
	private int totalEnemigosCreados = 10;
	private int maxEnemigos = 0;

	private Hechizo[] hechizos;
	private Boton[] botones;
	private Hechizo hechizoSeleccionado = null;

	// Variables para control de oleadas
	private int oleadaActual = 1;
	private int[] objetivosOleada = { 20, 40, 60 };
	private int enemigosEliminadosEnOleada = 0;
	private boolean esperandoInicioOleada;
	
	// Velocidades por oleada
    private double[] velocidadesOleada = {1.5, 1.8, 2.1};
    // Cantidad máxima de enemigos en pantalla por oleada
    private int[] maxEnemigosPantalla = {10, 15, 20};
    // Daño por colisión por oleada
    private int[] danioPorOleada = {10, 15, 20};
    
 // Variables para sistema de recompensas
    private boolean mostrandoRecompensas;
    private Boton[] botonesRecompensa;
    
    private Pocion[] pociones;
    private double probabilidadPocion = 0.3; // 30% de chance
    private int maxPociones = 10;

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Gondolf - Land of Bats", 1200, 720);
		this.fondo = Herramientas.cargarImagen("image/fondoTierra.jpg");

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
		
		// Sumamos en maxEnemigos los objetivos de cada oleada
//        for (int objetivo : objetivosOleada) {
//            maxEnemigos += objetivo;
//        }
        
		// creando el enemigo y evitando que pasen por encima del menú cuando aparezcan
		this.enemigos = new Enemigo[30]; // creo un arreglo para almacenar enemigos
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
			enemigos[i] = new Enemigo(ex, ey, 20, 20, /*Color.MAGENTA,*/ velocidadesOleada[0]); // creo el enemigo
		}

		// viejo
//		this.boton1 = new Boton(entorno.ancho() - 100, entorno.alto() / 2 - 80, 130, 65);
//		this.boton2 = new Boton(entorno.ancho() - 100, entorno.alto() / 2, 130, 65);

		// nuevo
		// Creo los hechizos
		hechizos = new Hechizo[] { new Hechizo("Bomba de Agua", 0, 20, Color.blue,0),
				new Hechizo("Tormenta de Fuego", 20, 150, Color.red,0),
				new Hechizo("Transmutacion Final",20,175,Color.cyan,100)};
		
		// Creo los botones
		int menuDerecha = entorno.ancho() - menu.getAncho();
		botones = new Boton[] { new Boton(menuDerecha + 100, 300, 120, 40, "Bomba de agua"),
				new Boton(menuDerecha + 100, 350, 120, 40, "Tormenta de Fuego"),
				new Boton(menuDerecha + 100,400,120,40,"Transmutacion Final")};

		// Estado inicial
		this.energia = 100;
		this.vida = 100;
		this.esperandoInicioOleada = true;
		
		this.botonesRecompensa = new Boton[] {
	            new Boton(entorno.ancho()/2 - 150, entorno.alto()/2 + 50, 200, 40, "Vida +20"),
	            new Boton(entorno.ancho()/2 + 150, entorno.alto()/2 + 50, 200, 40, "Maná +20"),
	            new Boton(entorno.ancho()/2, entorno.alto()/2 + 120, 200, 40, "Velocidad x1.5") };
		
		this.pociones = new Pocion[maxPociones];
		
		ReproducirMusica.reproducirMusicaLoop("/sound/goldCobra.wav"); // Importo la clase

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
		entorno.dibujarImagen(fondo, entorno.ancho() / 2, entorno.alto() / 2, 0);
		
		if (mostrandoRecompensas) {
			mostrarPantallaRecompensas();
			return;
		}
		
		if (esperandoInicioOleada) {
            mostrarPantallaInicioOleada();
            return;
        }
        
        if (enemigosEliminadosEnOleada >= objetivosOleada[oleadaActual - 1]) {
        	controlarFinDeOleada();
        	return;
        }
		
		if (vida <= 0) {
			mostrarPantallaPerdida();
			return;
		}
		for (int i = 0; i < pociones.length; i++) {
	        if (pociones[i] != null) {
	            if (pociones[i].colisionaConPersonaje(personaje)) {
	                if (pociones[i].getTipo().equals("vida")) {
	                    vida += pociones[i].getCantidad();
	                    if (vida > 100) vida = 100;
	                } else {
	                    energia += pociones[i].getCantidad();
	                    if (energia > 100) energia = 100;
	                }
	                pociones[i] = null;
	            }
	        }
	    }
		dibujarObjetos();
		procesarMovimientoPersonaje();
		actualizarEnemigos();
		manejoDeClicksYHechizos();
	}
	
	private void generarPocion(int x, int y) {
	    if (Math.random() < probabilidadPocion) {
	        // Verificar si la posición está dentro de alguna roca
	        boolean posicionValida = true;
	        for (Roca roca : rocas) {
	            if (roca != null) {
	                int rocaX1 = roca.getX() - roca.getAncho() / 2;
	                int rocaX2 = roca.getX() + roca.getAncho() / 2;
	                int rocaY1 = roca.getY() - roca.getAlto() / 2;
	                int rocaY2 = roca.getY() + roca.getAlto() / 2;
	                
	                // Verificar si el punto (x,y) está dentro de la roca
	                if (x >= rocaX1 && x <= rocaX2 && y >= rocaY1 && y <= rocaY2) {
	                    posicionValida = false;
	                    break;
	                }
	            }
	        }
	        
	        if (posicionValida) {
	            String tipo = Math.random() < 0.5 ? "vida" : "mana";
	            int cantidad = 5;
	            
	            for (int i = 0; i < pociones.length; i++) {
	                if (pociones[i] == null) {
	                    pociones[i] = new Pocion(x, y, tipo, cantidad);
	                    break;
	                }
	            }
	        }
	    }
	}
	
	private void mostrarPantallaPerdida() {
		entorno.dibujarRectangulo(entorno.ancho()/2, entorno.alto()/2, entorno.ancho(), entorno.alto(), 0, Color.black);
		entorno.cambiarFont("Arial", 50, Color.red);
		entorno.escribirTexto("PERDISTE", entorno.ancho() / 2 - 140, entorno.alto() / 2);
	}
	
	private void controlarFinDeOleada() {
		if (oleadaActual < 3) {
			oleadaActual++;
			enemigosEliminadosEnOleada = 0;
			esperandoInicioOleada = true;
			mostrandoRecompensas = true;
		} else {
			entorno.dibujarRectangulo(entorno.ancho()/2, entorno.alto()/2, entorno.ancho(), entorno.alto(), 0, Color.black);
			entorno.cambiarFont("Arial", 50, Color.green);
			entorno.escribirTexto("GANASTE", entorno.ancho() / 2 - 140, entorno.alto() / 2);
		}
	}
	
	private void mostrarPantallaRecompensas() {
        entorno.dibujarRectangulo(entorno.ancho()/2, entorno.alto()/2, 
                                 entorno.ancho(), entorno.alto(), 0, new Color(0, 0, 0, 180));
        
        // Texto
        entorno.cambiarFont("Arial", 40, Color.white);
        entorno.escribirTexto("¡Estar por comenzar la oleada " + oleadaActual + "!", 
                             entorno.ancho()/2 - 283, entorno.alto()/2 - 80);
        entorno.escribirTexto("Elige una recompensa:", 
                             entorno.ancho()/2 - 195, entorno.alto()/2 - 20);
        
        // Dibujar botones de recompensa
        for (Boton boton : botonesRecompensa) {
            boton.dibujar(entorno);
        }
        
        // Manejar selección de recompensa
        if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            int mx = entorno.mouseX();
            int my = entorno.mouseY();
            
            for (int i = 0; i < botonesRecompensa.length; i++) {
                if (botonesRecompensa[i].fueClickeado(mx, my)) {
                    aplicarRecompensa(i);
                    prepararSiguienteOleada();
                    break;
                }
            }
        }
    }

    private void aplicarRecompensa(int opcion) {
        switch (opcion) {
            case 0: // Vida +20
                vida += 20;
                break;
            case 1: // Maná +20
                energia += 20;
                break;
            case 2: // Velocidad x1.5
                personaje.aumentarVelocidad(1.5);
                break;
        }
    }

    private void prepararSiguienteOleada() {
    	enemigosEliminadosEnOleada = 0;
        mostrandoRecompensas = false;
        esperandoInicioOleada = true;
    }
	
	private void procesarMovimientoPersonaje() {
		int limiteDerecho = entorno.ancho() - menu.getAncho();
		int velocidad = personaje.getVelocidad(); // Obtener velocidad actual
		if ((entorno.estaPresionada(entorno.TECLA_DERECHA) || entorno.estaPresionada('d'))
				&& !personaje.colisionaPorDerecha(limiteDerecho)
				&& !colisionaConRocaAlMover(velocidad, 0)) {
			personaje.moverDerecha();
		}
		if ((entorno.estaPresionada(entorno.TECLA_IZQUIERDA) || entorno.estaPresionada('a'))
				&& !personaje.colisionaPorIzquierda(entorno)
				&& !colisionaConRocaAlMover(-velocidad, 0)) {
			personaje.moverIzquierda();
		}
		if ((entorno.estaPresionada(entorno.TECLA_ARRIBA) || entorno.estaPresionada('w'))
				&& !personaje.colisionaPorArriba(entorno)
				&& !colisionaConRocaAlMover(0, -velocidad)) {
			personaje.moverArriba();
		}
		if ((entorno.estaPresionada(entorno.TECLA_ABAJO) || entorno.estaPresionada('s'))
				&& !personaje.colisionaPorAbajo(entorno)
				&& !colisionaConRocaAlMover(0, velocidad)) {
			personaje.moverAbajo();
		}
	}

	private void actualizarEnemigos() {
	    int enemigosActivos = 0;

	    for (int i = 0; i < enemigos.length; i++) {
	        if (enemigos[i] != null) {
	            if (personaje.colisionaConEnemigo(enemigos[i])) {
	                generarPocion(enemigos[i].getX(), enemigos[i].getY());
	                
	                enemigos[i] = null;
	                enemigosEliminadosEnOleada++;
	                vida -= danioPorOleada[oleadaActual - 1];
	                personaje.reproducirSonidoDamage();
	            } else {
					enemigos[i].moverHaciaPersonaje(personaje.getX(), personaje.getY());
					enemigosActivos++;
				}
			}
		}

		int maxActual = maxEnemigosPantalla[oleadaActual - 1];
		for (int i = 0; i < enemigos.length && enemigosActivos < maxActual && totalEnemigosCreados < maxEnemigos; i++) {
			if (enemigos[i] == null) {
				enemigos[i] = crearEnemigoAleatorio();
				totalEnemigosCreados++;
				enemigosActivos++;
			}
		}
	}
	
	private void manejoDeClicksYHechizos() {
		if (!entorno.mousePresente() || !entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO))
			return;

		int mx = entorno.mouseX();
		int my = entorno.mouseY();
		int limiteDerecho = entorno.ancho() - menu.getAncho();
		boolean clicEnBoton = false;

		for (int i = 0; i < botones.length; i++) {
			if (botones[i].fueClickeado(mx, my)) {
				hechizoSeleccionado = hechizos[i];
				for (Boton b : botones) b.setSeleccionado(false);
				botones[i].setSeleccionado(true);
				clicEnBoton = true;
				break;
			}
		}

		if (!clicEnBoton && hechizoSeleccionado != null && mx < limiteDerecho) {
			if (energia >= hechizoSeleccionado.getCosto()) {
				energia -= hechizoSeleccionado.getCosto();
				hechizoSeleccionado.dibujar(entorno, mx, my);

				for (int i = 0; i < enemigos.length; i++) {
					if (enemigos[i] != null && hechizoSeleccionado.enRango(mx, my, enemigos[i])) {
						if (hechizoSeleccionado.getNombre().equals("Transmutacion Final")) {
							hechizoSeleccionado.aplicarEfectoRalentizacion(enemigos[i]);
						} else {
							generarPocion(enemigos[i].getX(), enemigos[i].getY());
							enemigos[i] = null;
							enemigosEliminadosEnOleada++;
						}
					}
				}

				hechizoSeleccionado = null;
				for (Boton b : botones) b.setSeleccionado(false);
			}
		}
	}


	
	private void mostrarPantallaInicioOleada() {
		// Fondo
        entorno.dibujarRectangulo(entorno.ancho()/2, entorno.alto()/2, entorno.ancho(), entorno.alto(), 0, Color.black);
        
        // Texto de oleada
        entorno.cambiarFont("Arial", 50, Color.white);
        entorno.escribirTexto("Oleada " + this.oleadaActual, entorno.ancho()/2 - 100, entorno.alto()/2 - 50);
        
     // Botón para comenzar
        int botonX = entorno.ancho()/2;
        int botonY = entorno.alto()/2 + 50;
        int botonAncho = 200;
        int botonAlto = 60;
        
        // Dibujar el botón
        entorno.dibujarRectangulo(botonX, botonY, botonAncho, botonAlto, 0, Color.gray);
        
        // Dibujar el texto centrado sobre el botón
        entorno.cambiarFont("Arial", 30, Color.white);
        
        // Calcular posición para centrar el texto
        int textoX = botonX - 70;  // Mitad del ancho del texto aprox
        int textoY = botonY + 10;  // Ajuste vertical
        
        entorno.escribirTexto("Comenzar", textoX, textoY);
               
        
        // Detectar clic en el botón
        if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            int mouseX = entorno.mouseX();
            int mouseY = entorno.mouseY();
            
            // Verificar si se hizo clic en el botón
            if (mouseX > entorno.ancho()/2 - 100 && mouseX < entorno.ancho()/2 + 100 &&
                mouseY > entorno.alto()/2 + 20 && mouseY < entorno.alto()/2 + 80) {
                
                prepararOleada();
                esperandoInicioOleada = false;
            }
        }
    }
	
	private void prepararOleada() {
        // Reiniciar enemigos
        for (int i = 0; i < enemigos.length; i++) {
            enemigos[i] = null;
        }
        
        // reinicio contador de enemigos creados para esta oleada
        totalEnemigosCreados = 0;
        
        //establezco el límite máximo de eneigos para esta oleada
        maxEnemigos = objetivosOleada[oleadaActual - 1];
        
        // Crear nuevos enemigos para la oleada
        for (int i = 0; i < 10 && i < enemigos.length; i++) {
            enemigos[i] = crearEnemigoAleatorio();
            totalEnemigosCreados++;
        }
        //Limpiar pociones
        for (int i = 0; i < pociones.length; i++) {
            pociones[i] = null;
        }
        //totalEnemigosCreados = 10;
    }

	public void dibujarObjetos() {
		// Podria hacer una condicional que diga IF (PERSONAJE != NULL && asi con todos
		// los objetos...)
		entorno.dibujarImagen(fondo, entorno.ancho() / 2, entorno.alto() / 2, 0);
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
		for (Pocion pocion : pociones) {
	        if (pocion != null) {
	            pocion.dibujar(entorno);
	        }
	    }

		this.entorno.cambiarFont("Arial", 20, Color.white);
		this.entorno.escribirTexto("Maná: " + this.energia, entorno.ancho() - 150, 500);
		this.entorno.escribirTexto("Vida: " + this.vida, entorno.ancho() - 148, 530);
		// Mostramos información de oleada
        if (this.oleadaActual == 3) {
        	this.entorno.escribirTexto("¡Última oleada!", entorno.ancho() - 150, 560);
        } else {
        	this.entorno.escribirTexto("Oleada: " + this.oleadaActual + "/3", entorno.ancho() - 150, 560);
        }
        this.entorno.escribirTexto("Objetivo: " + this.enemigosEliminadosEnOleada + "/" + this.objetivosOleada[oleadaActual - 1], entorno.ancho() - 150, 590);
        
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
		int borde = (int) (Math.random() * 4); // elijo aleatoriamente uno de los 4 bordes de la pnatalla
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
		return new Enemigo(ex, ey, 20, 20, /*Color.magenta,*/ velocidadesOleada[oleadaActual - 1]); 

	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}