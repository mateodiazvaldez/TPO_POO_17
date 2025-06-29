// src/model/Jugador.java
package model;

import java.util.Map;

public class Jugador extends Persona {
    private double goles;
    private double asistencias;
    private double vallas;
    private int valorMercado;

    public Jugador(String nombre, double edad, String club,
                   double goles, double asistencias, double vallas,
                   int valorMercado) {
        super(nombre, edad, club);
        this.goles = goles;
        this.asistencias = asistencias;
        this.vallas = vallas;
        this.valorMercado = valorMercado;
    }

    public double getGoles() {
        return goles;
    }

    public void setGoles(double goles) {
        this.goles = goles;
    }

    public double getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(double asistencias) {
        this.asistencias = asistencias;
    }

    public double getVallas() {
        return vallas;
    }

    public void setVallas(double vallas) {
        this.vallas = vallas;
    }

    public int getValorMercado() {
        return valorMercado;
    }

    public void setValorMercado(int valorMercado) {
        this.valorMercado = valorMercado;
    }

    public void agregarEstadistica(String clave, double valor) {
        this.estadisticas.agregarEstadistica(clave, valor);
    }

    @Override
    public Map<String, Double> getEstadisticas() {
        return this.estadisticas.getEstadisticas();
    }
}
