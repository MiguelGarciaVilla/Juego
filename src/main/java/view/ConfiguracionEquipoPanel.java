package view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.TipoJugador;
// Asumo que tienes una forma de obtener todos los nombres de Objetos
import model.Objeto;

public class ConfiguracionEquipoPanel extends GridPane {

    private final TextField nombreField = new TextField();
    private final ComboBox<TipoJugador> tipoCombo = new ComboBox<>();
    private final ComboBox<String> objetoCombo = new ComboBox<>();
    private final TextField nivelPoderField = new TextField("1");
    private final TextField vidaField = new TextField("100");
    private final TextField curacionField = new TextField("10"); // Solo para Magos
    private final Label curacionLabel = new Label("Curación Base:");


    public ConfiguracionEquipoPanel(int jugadorNum) {
        this.setPadding(new Insets(10));
        this.setHgap(10);
        this.setVgap(8);

        // Título del Jugador
        Label title = new Label("Jugador " + jugadorNum + ":");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        this.add(title, 0, 0, 2, 1);

        // Configuración de los ComboBox
        tipoCombo.setItems(FXCollections.observableArrayList(TipoJugador.values()));
        tipoCombo.getSelectionModel().selectFirst(); // Selecciona el primero por defecto

        // *** IMPORTANTE: NECESITAS OBTENER LOS NOMBRES DE TUS OBJETOS ***
        // Aquí simulamos los nombres de los objetos que tienes
        objetoCombo.setItems(FXCollections.observableArrayList(
                "Arco Infinito", "Hoja del Olimpo", "Chancla Nuclear", "Báculo de Curación", "Espadas del caos", "Tecnicas del infinito"
        ));
        objetoCombo.getSelectionModel().selectFirst();

        // Listener para la curación: solo mostrar si es Mago
        tipoCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean esMago = newVal == TipoJugador.MAGO;
            curacionLabel.setVisible(esMago);
            curacionField.setVisible(esMago);
        });

        // Inicialmente ocultar la curación
        curacionLabel.setVisible(tipoCombo.getValue() == TipoJugador.MAGO);
        curacionField.setVisible(tipoCombo.getValue() == TipoJugador.MAGO);

        // --- Layout (Filas) ---
        this.add(new Label("Nombre:"), 0, 1);
        this.add(nombreField, 1, 1);

        this.add(new Label("Tipo:"), 0, 2);
        this.add(tipoCombo, 1, 2);

        this.add(new Label("Arma/Objeto:"), 0, 3);
        this.add(objetoCombo, 1, 3);

        this.add(new Label("Nivel Poder:"), 0, 4);
        this.add(nivelPoderField, 1, 4);

        this.add(new Label("Vida Base:"), 0, 5);
        this.add(vidaField, 1, 5);

        this.add(curacionLabel, 0, 6);
        this.add(curacionField, 1, 6);

        // Asegurar que solo se permitan números en los campos relevantes
        setupNumericField(nivelPoderField);
        setupNumericField(vidaField);
        setupNumericField(curacionField);
    }

    private void setupNumericField(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                field.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    // Método para obtener los datos del jugador configurado
    // Este método devolverá un array de Strings con todos los datos necesarios
    public String[] getDatosJugador() {
        return new String[]{
                nombreField.getText(),
                tipoCombo.getValue().toString(),
                objetoCombo.getValue(),
                nivelPoderField.getText(),
                vidaField.getText(),
                curacionField.getText()
        };
    }
}