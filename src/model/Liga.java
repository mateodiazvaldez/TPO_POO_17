
package model;

import java.util.List;

public class Liga {
    private String nombre;
    private List<Club> clubes;
    private List<Jugador> jugadores;
    private List<Partido> partidos;

    public Liga(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Club> getClubes() { return clubes; }
    public void setClubes(List<Club> clubes) { this.clubes = clubes; }

    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> jugadores) { this.jugadores = jugadores; }

    public List<Partido> getPartidos() { return partidos; }
    public void setPartidos(List<Partido> partidos) { this.partidos = partidos; }
}
