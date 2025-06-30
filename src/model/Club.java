
package model;

import java.util.List;

public class Club {
    private String nombre;
    private DT dt;
    private Double pj, pg, pe, pp, puntos;
    private List<Jugador> jugadores;
    private int valorPlantel;

    public Club(String nombre, DT dt,
                Double pj, Double pg, Double pe, Double pp,
                Double puntos, List<Jugador> jugadores,
                int valorPlantel) {
        this.nombre = nombre;
        this.dt = dt;
        this.pj = pj;
        this.pg = pg;
        this.pe = pe;
        this.pp = pp;
        this.puntos = puntos;
        this.jugadores = jugadores;
        this.valorPlantel = valorPlantel;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public DT getDt() { return dt; }
    public void setDt(DT dt) { this.dt = dt; }

    public Double getPj() { return pj; }
    public void setPj(Double pj) { this.pj = pj; }

    public Double getPg() { return pg; }
    public void setPg(Double pg) { this.pg = pg; }

    public Double getPe() { return pe; }
    public void setPe(Double pe) { this.pe = pe; }

    public Double getPp() { return pp; }
    public void setPp(Double pp) { this.pp = pp; }

    public Double getPuntos() { return puntos; }
    public void setPuntos(Double puntos) { this.puntos = puntos; }

    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> jugadores) { this.jugadores = jugadores; }

    public int getValorPlantel() { return valorPlantel; }
    public void setValorPlantel(int valorPlantel) { this.valorPlantel = valorPlantel; }
}
