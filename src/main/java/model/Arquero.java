package model;

public class Arquero extends Jugador {
    public Arquero(String id, String usuario, int nivelDePoder, int vida, Objeto objeto) {
        super(id, usuario, nivelDePoder, vida, TipoJugador.ARQUERO, objeto);
    }

    @Override
    public int calcularDaño() {
        // El daño depende de SU objeto y SU nivel
        return this.objeto.getDaño() * this.nivelDePoder;
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

            notificarCambioVida();
        }
    }

    // El método recibirDaño(int daño) ya está en la clase Jugador. ¡No hace falta reescribirlo.
    // El método atacar(Jugador objetivo) ya está en la clase Jugador.
}