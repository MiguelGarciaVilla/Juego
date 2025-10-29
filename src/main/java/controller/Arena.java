package controller; // Es buena idea poner esto en un paquete 'controller'

import model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Arena {

    private List<Equipo> equiposEnBatalla;
    private Random rand = new Random();

    public Arena() {
        equiposEnBatalla = new ArrayList<>();
    }

    public void agregarEquipo(Equipo equipo) {
        equiposEnBatalla.add(equipo);
        // Asignar el equipo a cada jugador
        for (Jugador j : equipo.getJugadores()) {
            j.setEquipo(equipo);
        }
    }

    // Método principal para simular la batalla
    public void iniciarBatalla() {
        System.out.println("¡COMIENZA LA BATALLA!");

        // La batalla continúa mientras más de un equipo siga vivo
        while (contarEquiposVivos() > 1) {

            // Cada equipo juega un turno
            for (Equipo equipoAtacante : equiposEnBatalla) {
                if (!equipoEstaVivo(equipoAtacante)) {
                    continue; // Este equipo fue eliminado, saltar turno
                }

                System.out.println("\n--- Turno del Equipo: " + equipoAtacante.getNombre() + " ---");

                // Cada jugador vivo del equipo realiza una acción
                for (Jugador jugador : equipoAtacante.getJugadores()) {
                    if (jugador.estaVivo()) {
                        ejecutarTurnoJugador(jugador);

                        // ¡NUEVO! Añadir un delay para que la GUI pueda actualizarse
                        try {
                            Thread.sleep(7000); // Pausa de 500 milisegundos (medio segundo)
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return; // Termina la batalla si es interrumpida
                        }
                    }
                }

                // Si después de este turno solo queda un equipo, termina la batalla
                if (contarEquiposVivos() <= 1) {
                    break;
                }
            }
        }

        // Anunciar al ganador
        Equipo ganador = encontrarEquipoGanador();
        if (ganador != null) {
            System.out.println("\n¡LA BATALLA HA TERMINADO!");
            System.out.println("El equipo ganador es: " + ganador.getNombre());
        } else {
            System.out.println("\nLA BATALLA HA TERMINADO EN EMPATE.");
        }
    }

    // Lógica para el turno de un solo jugador
    private void ejecutarTurnoJugador(Jugador jugador) {
        // Si es un Mago, cura a un aliado (o a sí mismo)
        if (jugador instanceof Mago) {
            Mago mago = (Mago) jugador;
            Jugador aliadoACurar = encontrarAliadoMasHerido(jugador.getEquipo());
            if (aliadoACurar != null) {
                mago.curar(aliadoACurar);
            }
        }
        // Si es cualquier otra clase, ataca
        else {
            Jugador objetivo = encontrarObjetivoEnemigo(jugador.getEquipo());
            if (objetivo != null) {
                jugador.atacar(objetivo);
            } else {
                System.out.println(jugador.getUsuario() + " no tiene a quién atacar.");
            }
        }
    }

    // --- Métodos de Ayuda (IA simple) ---

    private Jugador encontrarAliadoMasHerido(Equipo equipo) {
        return equipo.getJugadores().stream()
                .filter(Jugador::estaVivo) // Solo jugadores vivos
                .min((j1, j2) -> Integer.compare(j1.getVida(), j2.getVida())) // Encontrar el de menor vida
                .orElse(null); // Devuelve null si no hay aliados vivos
    }

    private Jugador encontrarObjetivoEnemigo(Equipo equipoAtacante) {
        List<Jugador> enemigosVivos = new ArrayList<>();
        for (Equipo equipo : equiposEnBatalla) {
            if (equipo != equipoAtacante) {
                enemigosVivos.addAll(
                        equipo.getJugadores().stream()
                                .filter(Jugador::estaVivo)
                                .collect(Collectors.toList())
                );
            }
        }

        if (enemigosVivos.isEmpty()) {
            return null; // No hay enemigos
        }

        // Elige un enemigo vivo al azar
        return enemigosVivos.get(rand.nextInt(enemigosVivos.size()));
    }

    private boolean equipoEstaVivo(Equipo equipo) {
        for (Jugador j : equipo.getJugadores()) {
            if (j.estaVivo()) {
                return true; // Si al menos uno está vivo, el equipo está vivo
            }
        }
        return false;
    }

    private int contarEquiposVivos() {
        int count = 0;
        for (Equipo e : equiposEnBatalla) {
            if (equipoEstaVivo(e)) {
                count++;
            }
        }
        return count;
    }

    public Equipo encontrarEquipoGanador() {
        for (Equipo e : equiposEnBatalla) {
            if (equipoEstaVivo(e)) {
                return e; // Devuelve el primer (y único) equipo vivo
            }
        }
        return null;
    }
}