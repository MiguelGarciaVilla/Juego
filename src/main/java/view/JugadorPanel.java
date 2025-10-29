package view;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import model.Jugador;

// 1. Implementa la interfaz para "escuchar" los cambios
public class JugadorPanel extends VBox implements VidaListener {

    private final Jugador jugador;
    private final Label nombreLabel;
    private final Label vidaLabel;
    private final ProgressBar barraVida;

    public JugadorPanel(Jugador jugador) {
        this.jugador = jugador;

        // --- Configuración Visual ---
        this.setSpacing(5); // Espaciado entre elementos

        // Etiqueta para el nombre del jugador
        nombreLabel = new Label(jugador.getUsuario() + " (" + jugador.getTipo() + ")");
        nombreLabel.setStyle("-fx-font-weight: bold;");

        // Barra de progreso (La barra de vida)
        barraVida = new ProgressBar(1.0); // Inicialmente 100% de vida
        barraVida.setPrefWidth(200); // Ancho de la barra

        // Etiqueta para el valor numérico de la vida
        vidaLabel = new Label("Vida: " + jugador.getVida() + "/" + jugador.getVidaInicial());

        // Añadir elementos al panel
        this.getChildren().addAll(nombreLabel, barraVida, vidaLabel);

        // --- Conexión al Modelo ---
        // 2. Registramos este panel como Listener del Jugador
        this.jugador.addVidaListener(this);
    }

    // 3. Este método se llama AUTOMÁTICAMENTE cuando la vida del Jugador cambia
    @Override
    public void onVidaChanged(int vidaActual, int vidaMaxima) {
        // Importante: Las actualizaciones de GUI deben hacerse en el hilo de JavaFX
        Platform.runLater(() -> {
            // Actualizar la barra de progreso
            double progreso = (double) vidaActual / vidaMaxima;
            barraVida.setProgress(progreso);

            // Actualizar el texto
            vidaLabel.setText("Vida: " + vidaActual + "/" + vidaMaxima);

            // Si el jugador muere, cambiar el estilo
            if (vidaActual <= 0) {
                barraVida.setStyle("-fx-accent: black;");
                nombreLabel.setText(jugador.getUsuario() + " (ELIMINADO)");
            } else if (progreso < 0.3) {
                // Vida baja (menos del 30%)
                barraVida.setStyle("-fx-accent: red;");
            } else if (progreso < 0.7) {
                // Vida media
                barraVida.setStyle("-fx-accent: orange;");
            } else {
                // Vida alta
                barraVida.setStyle("-fx-accent: green;");
            }
        });
    }
}