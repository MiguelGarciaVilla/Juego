package model;
import view.VidaListener;

import java.util.ArrayList; // Necesario
import java.util.List; // Necesario
// Hacemos que Jugador implemente las interfaces
public abstract class Jugador implements Herible, Curable {
    protected int vidaInicial;
    protected String id;
    protected String usuario;
    protected int nivelDePoder;
    protected int vida;
    protected TipoJugador tipo;
    protected Objeto objeto;
    protected Equipo equipo;
    private final List<view.VidaListener> listeners = new ArrayList<>();// Añadido: Referencia a su equipo

    public Jugador(String id, String usuario, int nivelDePoder, int vida, TipoJugador tipo, Objeto objeto) {
        this.vidaInicial = vida;
        this.id = id;
        this.usuario = usuario;
        this.nivelDePoder = nivelDePoder;
        this.vida = vida;
        this.tipo = tipo;
        this.objeto = objeto;
    }
    public void addVidaListener(view.VidaListener listener) {
        this.listeners.add(listener);
    }
    public void notificarCambioVida() {
        for (view.VidaListener listener : listeners) {
            listener.onVidaChanged(this.vida, this.vidaInicial);
        }
    }

    public abstract int calcularDaño();

    public void atacar(Jugador objetivo) {

        if (this.equipo == objetivo.getEquipo()) {
            System.out.println(this.usuario + " no puede atacar a un compañero de equipo.");
            return;
        }

        // Aquí podríamos añadir lógica de RANGO más adelante
        // if(this.objeto.getRango() < distancia(this, objetivo)) { ... }

        int dañoCalculado = this.calcularDaño();
        System.out.println(this.usuario + " ataca a " + objetivo.getUsuario() + " haciendo " + dañoCalculado + " de daño.");
        objetivo.recibirDaño(dañoCalculado);
    }


    @Override
    public void recibirDaño(int daño) {
        this.vida -= daño;
        if (this.vida < 0) {
            this.vida = 0;
        }
        System.out.println(this.usuario + " recibe " + daño + " de daño. Vida restante: " + this.vida);

        // ¡NUEVO!: Notificar a la GUI después de cambiar el estado
        notificarCambioVida();
    }


    @Override
    public abstract void recibirCuracion(Jugador jugador);

    public int getVidaInicial() {
        return vidaInicial;
    }

    public void setVidaInicial(int vidaInicial) {
        this.vidaInicial = vidaInicial;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getNivelDePoder() {
        return nivelDePoder;
    }

    public void setNivelDePoder(int nivelDePoder) {
        this.nivelDePoder = nivelDePoder;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public TipoJugador getTipo() {
        return tipo;
    }

    public void setTipo(TipoJugador tipo) {
        this.tipo = tipo;
    }

    public Objeto getObjeto() {
        return objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    public List<VidaListener> getListeners() {
        return listeners;
    }

    public int getVida() {
        return vida;
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    public String getUsuario() {
        return usuario;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
}
