package model;

import java.util.ArrayList;

public class Equipo {
    protected String nombre;
    protected ArrayList<Jugador> jugadores;

    public Equipo(String nombre) {
        this.nombre = nombre;
        jugadores = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }


}
