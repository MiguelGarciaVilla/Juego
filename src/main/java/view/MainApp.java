package view;

import controller.Arena;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainApp extends Application {

    private Stage primaryStage;
    private Arena arena;
    private List<ConfiguracionEquipoPanel> panelesEquipo1 = new ArrayList<>();
    private List<ConfiguracionEquipoPanel> panelesEquipo2 = new ArrayList<>();

    // Contador para generar IDs únicos de jugadores
    private int idCounter = 1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mostrarPantallaConfiguracion();
    }

    // --- PANTALLA 1: CONFIGURACIÓN ---

    private void mostrarPantallaConfiguracion() {
        primaryStage.setTitle("Configuración de la Batalla");

        BorderPane root = new BorderPane();
        HBox mainLayout = new HBox(30); // Dos equipos, lado a lado
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);

        // --- Panel de Configuración del Equipo 1 ---
        VBox equipo1Config = new VBox(15);
        equipo1Config.setStyle("-fx-border-color: navy; -fx-border-width: 2; -fx-padding: 10;");
        equipo1Config.getChildren().add(new Label("=== EQUIPO 1: Los Vengadores ==="));
        for (int i = 1; i <= 3; i++) {
            ConfiguracionEquipoPanel panel = new ConfiguracionEquipoPanel(i);
            panelesEquipo1.add(panel);
            equipo1Config.getChildren().add(panel);
        }

        // --- Panel de Configuración del Equipo 2 ---
        VBox equipo2Config = new VBox(15);
        equipo2Config.setStyle("-fx-border-color: maroon; -fx-border-width: 2; -fx-padding: 10;");
        equipo2Config.getChildren().add(new Label("=== EQUIPO 2: La Liga de la Justicia ==="));
        for (int i = 1; i <= 3; i++) {
            ConfiguracionEquipoPanel panel = new ConfiguracionEquipoPanel(i);
            panelesEquipo2.add(panel);
            equipo2Config.getChildren().add(panel);
        }

        mainLayout.getChildren().addAll(equipo1Config, equipo2Config);
        // --- CÓDIGO CORREGIDO AQUÍ ---
        ScrollPane scrollPane = new ScrollPane(mainLayout);
        // Permite que el contenido se desborde y se pueda desplazar
        scrollPane.setFitToWidth(true);
        // Importante: No dejar que el ScrollPane se estire innecesariamente
        // scrollPane.setFitToHeight(true); // NO es necesario aquí si el VBox es muy alto

        root.setCenter(scrollPane); // <--- Ahora el centro es el ScrollPane, no mainLayout
        // --- FIN CÓDIGO CORREGIDO ---

        Button iniciarButton = new Button("¡Comenzar Batalla!");
        iniciarButton.setOnAction(e -> iniciarJuegoConConfiguracion());

        VBox bottomBox = new VBox(iniciarButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 950, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- LÓGICA DE TRANSICIÓN ---

    private void iniciarJuegoConConfiguracion() {
        // 1. Recopilar Datos de los 6 jugadores
        List<String[]> datosEquipo1 = panelesEquipo1.stream()
                .map(ConfiguracionEquipoPanel::getDatosJugador)
                .collect(Collectors.toList());

        List<String[]> datosEquipo2 = panelesEquipo2.stream()
                .map(ConfiguracionEquipoPanel::getDatosJugador)
                .collect(Collectors.toList());

        // 2. Crear el Modelo (Arena, Equipos, Jugadores)
        List<Equipo> equipos = crearModeloConDatos(datosEquipo1, datosEquipo2);

        // 3. Lanzar la Pantalla de Batalla
        mostrarPantallaBatalla(equipos);
    }

    // --- PANTALLA 2: BATALLA ---

    private void mostrarPantallaBatalla(List<Equipo> equipos) {
        primaryStage.setTitle("Arena de Batalla FX - ¡LUCHA!");

        BorderPane root = new BorderPane();

        // Etiqueta de Título/Resultado
        Label resultadoLabel = new Label("¡Batalla Lista! Presiona Iniciar.");
        resultadoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        VBox topBox = new VBox(10, resultadoLabel);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10));
        root.setTop(topBox);

        // Contenedor principal para ambos equipos (Horizontal)
        HBox arenaLayout = new HBox(50);
        arenaLayout.setPadding(new Insets(20));
        arenaLayout.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane(arenaLayout);
        scrollPane.setFitToWidth(true); // Asegura que se estire a lo ancho
        scrollPane.setFitToHeight(true); // Permite que el contenido determine el tamaño
        root.setCenter(scrollPane);

        // Contenedores para cada equipo (Vertical)
        VBox equipo1Panel = new VBox(15);
        equipo1Panel.setPrefWidth(250);
        equipo1Panel.setStyle("-fx-border-color: navy; -fx-border-width: 2; -fx-padding: 10;");

        VBox equipo2Panel = new VBox(15);
        equipo2Panel.setPrefWidth(250);
        equipo2Panel.setStyle("-fx-border-color: maroon; -fx-border-width: 2; -fx-padding: 10;");

        arenaLayout.getChildren().addAll(equipo1Panel, equipo2Panel);

        // 1. Crear los paneles de jugadores y ASIGNARLOS al equipo correcto
        int i = 0;
        for (Equipo equipo : equipos) {
            VBox panelContenedor = (i == 0) ? equipo1Panel : equipo2Panel;

            Label nombreEquipo = new Label("EQUIPO: " + equipo.getNombre());
            nombreEquipo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            panelContenedor.getChildren().add(nombreEquipo);

            for (Jugador jugador : equipo.getJugadores()) {
                JugadorPanel panel = new JugadorPanel(jugador);
                panelContenedor.getChildren().add(panel);
            }
            i++;
        }

        // Botón de inicio
        Button startButton = new Button("¡Iniciar Batalla!");
        VBox bottomPanel = new VBox(10, startButton);
        bottomPanel.setAlignment(Pos.CENTER);
        root.setBottom(bottomPanel);

        // 2. Conectar el botón a la lógica
        startButton.setOnAction(event -> {
            startButton.setDisable(true);
            resultadoLabel.setText("¡Batalla en curso! Observa las barras...");

            // Usamos un hilo para no congelar la GUI
            new Thread(() -> {
                arena.iniciarBatalla();

                // Una vez que termina, actualizamos el botón y mostramos el ganador
                Platform.runLater(() -> {
                    startButton.setDisable(false);
                    resultadoLabel.setText("¡LA BATALLA HA TERMINADO! Ganador: " + arena.encontrarEquipoGanador().getNombre());
                });
            }).start();
        });

        Scene scene = new Scene(root, 900, 850);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- FACTORY (CREACIÓN DEL MODELO) ---

    private List<Equipo> crearModeloConDatos(List<String[]> datos1, List<String[]> datos2) {
        Equipo equipo1 = new Equipo("Team 1");
        for (String[] datos : datos1) {
            equipo1.getJugadores().add(crearJugador(datos));
        }

        Equipo equipo2 = new Equipo("Team 2");
        for (String[] datos : datos2) {
            equipo2.getJugadores().add(crearJugador(datos));
        }

        this.arena = new Arena();
        this.arena.agregarEquipo(equipo1);
        this.arena.agregarEquipo(equipo2);

        List<Equipo> listaEquipos = new ArrayList<>();
        listaEquipos.add(equipo1);
        listaEquipos.add(equipo2);
        return listaEquipos;
    }

    private Jugador crearJugador(String[] datos) {
        // [0] Nombre, [1] Tipo, [2] Objeto, [3] NivelPoder, [4] Vida, [5] CuracionBase
        String id = String.valueOf(idCounter++); // Generamos un ID único
        String nombre = datos[0].isEmpty() ? "Héroe " + id : datos[0];
        TipoJugador tipo = TipoJugador.valueOf(datos[1]);
        int nivelPoder = Integer.parseInt(datos[3]);
        int vida = Integer.parseInt(datos[4]);
        int curacionBase = datos[5].isEmpty() ? 0 : Integer.parseInt(datos[5]);

        Objeto objeto = crearObjeto(datos[2]);

        // Factory del Jugador
        switch (tipo) {
            case ARQUERO:
                return new Arquero(id, nombre, nivelPoder, vida, objeto);
            case ESPARTANO:
                return new Espartano(id, nombre, nivelPoder, vida, objeto);
            case ABUELA:
                return new Abuela(id, nombre, nivelPoder, vida, objeto);
            case MAGO:
                // El Mago requiere el parámetro extra de capacidadCurativa
                return new Mago(id, nombre, nivelPoder, vida, objeto, curacionBase);
            default:
                throw new IllegalArgumentException("Tipo de jugador no válido: " + tipo);
        }
    }

    private Objeto crearObjeto(String nombreObjeto) {
        // Valores de daño y rango hardcodeados para el ejemplo
        switch (nombreObjeto) {
            case "Tecnicas del infinito":
                return new BaculoDeCuracion("Tecnicas del infinito", 5, 0);
            case "Espadas del caos":
                return new HojaDelOlimpo("Espadas del caos", 5, 20);
            case "Arco Infinito":
                return new ArcoInfinito("Arco Infinito", 10, 20);
            case "Hoja del Olimpo":
                return new HojaDelOlimpo("Hoja del Olimpo", 1, 10);
            case "Chancla Nuclear":
                return new ChanclaNuclear("Chancla Nuclear", 8, 25);
            case "Báculo de Curación":
                // El báculo de curación no debería hacer daño de ataque
                return new BaculoDeCuracion("Báculo de Curación", 5, 0);

            default:
                throw new IllegalArgumentException("Objeto no válido: " + nombreObjeto);
        }
    }
}