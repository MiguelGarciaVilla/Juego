package model;

public abstract class Objeto {
    protected String nombre;
    protected int rango;
    protected int daño;

    public Objeto(String nombre, int rango, int daño) {
        this.nombre = nombre;
        this.rango = rango;
        this.daño= daño;
    }

    public int getDaño() {
        return daño;
    }

    public void setDaño(int daño) {
        this.daño = daño;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getRango() {
        return rango;
    }

    public void setRango(int rango) {
        this.rango = rango;
    }
}
