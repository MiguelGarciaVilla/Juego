package model;

public class Mago extends Jugador {

    private int capacidadCurativa;

    public Mago(String id, String usuario, int nivelDePoder, int vida, Objeto objeto, int capacidadCurativa) {
        super(id, usuario, nivelDePoder, vida, TipoJugador.MAGO, objeto);
        this.capacidadCurativa = capacidadCurativa;
    }

    // El Mago no hace daño de ataque, como dijiste
    @Override
    public int calcularDaño() {
        return 0;
    }

    // El Mago no puede atacar, así que sobrescribimos el método
    @Override
    public void atacar(Jugador objetivo) {
        System.out.println(this.usuario + " es un Mago y no puede atacar.");
    }

    // Método para CURAR a un aliado
    public void curar(Jugador aliado) {
        // Solo puede curar a compañeros de equipo
        if (this.equipo != aliado.getEquipo()) {
            System.out.println(this.usuario + " no puede curar a un enemigo.");
            return;
        }

        System.out.println(this.usuario + " cura a " + aliado.getUsuario() + ".");
        aliado.recibirCuracion(this); // 'this' es el Mago que cura
    }

    @Override
    public void recibirCuracion(Jugador jugador) {
        if (jugador instanceof Mago) {
            Mago mago = (Mago) jugador;
            int curacion = mago.getCapacidadCurativa();
            int nuevaVida = this.vida + curacion;

            // LA LÍNEA CLAVE: Aseguramos que la vida NUNCA exceda la vida inicial.
            this.vida = Math.min(nuevaVida, this.vidaInicial);

            System.out.println(this.usuario + " recibe " + curacion + " de curación. Vida actual: " + this.vida);
            // notificarCambioVida(); // Asegúrate que esta línea también esté
            System.out.println(this.usuario + " recibe " + mago.getCapacidadCurativa() + " de curación. Vida actual: " + this.vida);

            // ¡NUEVO!
            notificarCambioVida();
        }
    }

    public int getCapacidadCurativa() {
        // La curación también escala con el nivel de poder
        return this.capacidadCurativa * this.nivelDePoder;
    }
}