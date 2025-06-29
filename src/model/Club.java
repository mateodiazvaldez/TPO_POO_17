// src/model/Club.java
package model;

import java.util.ArrayList;
import java.util.List;

public class Club {
    private String nombre;
    private DT dt;
    private double pj;
    private double pg;
    private double pe;
    private double pp;
    private double gf;
    private double gc;
    private double puntos;
    private List<Jugador> jugadores;
    private int valorPlantel;

    public Club(String nombre, DT dt) {
        this.nombre = nombre;
        this.dt = dt;
        this.pj = this.pg = this.pe = this.pp = this.gf = this.gc = this.puntos = 0;
        this.jugadores = new ArrayList<>();
        this.valorPlantel = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public DT getDt() {
        return dt;
    }

    public void setDt(DT dt) {
        this.dt = dt;
    }

    public double getPj() {
        return pj;
    }

    public void setPj(double pj) {
        this.pj = pj;
    }

    public double getPg() {
        return pg;
    }

    public void setPg(double pg) {
        this.pg = pg;
    }

    public double getPe() {
        return pe;
    }

    public void setPe(double pe) {
        this.pe = pe;
    }

    public double getPp() {
        return pp;
    }

    public void setPp(double pp) {
        this.pp = pp;
    }

    public double getGf() {
        return gf;
    }

    public void setGf(double gf) {
        this.gf = gf;
    }

    public double getGc() {
        return gc;
    }

    public void setGc(double gc) {
        this.gc = gc;
    }

    public double getPuntos() {
        return puntos;
    }

    public void setPuntos(double puntos) {
        this.puntos = puntos;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public void agregarJugador(Jugador j) {
        jugadores.add(j);
    }

    public int getValorPlantel() {
        return valorPlantel;
    }

    public void setValorPlantel(int valorPlantel) {
        this.valorPlantel = valorPlantel;
    }
}
