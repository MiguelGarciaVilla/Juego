package view;

// Interfaz que la GUI usará para "escuchar" los cambios en el Modelo.
public interface VidaListener {
    // Se llamará cada vez que la vida de un jugador cambie.
    void onVidaChanged(int vidaActual, int vidaMaxima);
}
