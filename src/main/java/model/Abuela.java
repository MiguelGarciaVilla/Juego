package model;

public class Abuela extends Jugador {
    public Abuela(String id, String usuario, int nivelDePoder, int vida, Objeto objeto) {
        super(id, usuario, nivelDePoder, vida, TipoJugador.ABUELA, objeto);
    }

    @Override
    public int calcularDaño() {
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
}