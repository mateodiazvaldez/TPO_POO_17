// src/model/Liga.java
package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Liga {
    private String nombre;
    private List<Club> clubes;
    private List<Jugador> jugadores;
    private List<Partido> partidos;

    public Liga(String nombre) {
        this.nombre = nombre;
        this.clubes = new ArrayList<>();
        this.jugadores = new ArrayList<>();
        this.partidos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Club> getClubes() {
        return clubes;
    }

    public void setClubes(List<Club> clubes) {
        this.clubes = clubes;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }

    public void agregarClub(Club c) {
        clubes.add(c);
    }

    public void agregarJugador(Jugador j) {
        jugadores.add(j);
    }

    public void agregarPartido(Partido p) {
        partidos.add(p);
    }

    public Club buscarClubPorNombre(String nombre) {
        return clubes.stream()
            .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
            .findFirst().orElse(null);
    }

    /**
     * Calcula PJ, PG, PE, PP, GF, GC y puntos para cada club
     * según la lista de partidos cargada.
     */
    public void calcularClasificacion() {
        // reset
        for (Club c : clubes) {
            c.setPj(0); c.setPg(0); c.setPe(0);
            c.setPp(0); c.setGf(0); c.setGc(0);
            c.setPuntos(0);
        }
        for (Partido p : partidos) {
            Club local = buscarClubPorNombre(p.getEquipoLocal());
            Club visita = buscarClubPorNombre(p.getEquipoVisitante());
            if (local == null || visita == null) continue;

            // PJ
            local.setPj(local.getPj() + 1);
            visita.setPj(visita.getPj() + 1);
            // GF / GC
            local.setGf(local.getGf() + p.getGolesLocal());
            local.setGc(local.getGc() + p.getGolesVisitante());
            visita.setGf(visita.getGf() + p.getGolesVisitante());
            visita.setGc(visita.getGc() + p.getGolesLocal());
            // resultados
            if (p.getGolesLocal() > p.getGolesVisitante()) {
                local.setPg(local.getPg() + 1);
                visita.setPp(visita.getPp() + 1);
                local.setPuntos(local.getPuntos() + 3);
            } else if (p.getGolesLocal() < p.getGolesVisitante()) {
                visita.setPg(visita.getPg() + 1);
                local.setPp(local.getPp() + 1);
                visita.setPuntos(visita.getPuntos() + 3);
            } else {
                local.setPe(local.getPe() + 1);
                visita.setPe(visita.getPe() + 1);
                local.setPuntos(local.getPuntos() + 1);
                visita.setPuntos(visita.getPuntos() + 1);
            }
        }
    }

    /** Devuelve la tabla ordenada por puntos (descendente). */
    public List<Club> getTablaPosiciones() {
        calcularClasificacion();
        clubes.sort(Comparator.comparingDouble(Club::getPuntos).reversed());
        return clubes;
    }
}
