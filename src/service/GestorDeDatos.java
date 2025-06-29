// src/service/GestorDeDatos.java
package service;

import model.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class GestorDeDatos {

    public List<Jugador> cargarJugadores(String ruta) throws Exception {
        List<Jugador> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea = br.readLine(); // cabecera
            while ((linea = br.readLine()) != null) {
                String[] t = linea.split(",");
                // nombre,club,goles,edad,asistencias,vallas,valorMercado
                Jugador j = new Jugador(
                    t[0],
                    Double.parseDouble(t[3]),
                    t[1],
                    Double.parseDouble(t[2]),
                    Double.parseDouble(t[4]),
                    Double.parseDouble(t[5]),
                    Integer.parseInt(t[6])
                );
                lista.add(j);
            }
        }
        return lista;
    }

    public List<Club> cargarClubes(String ruta, List<DT> dts) throws Exception {
        List<Club> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea = br.readLine(); // cabecera
            while ((linea = br.readLine()) != null) {
                String[] t = linea.split(",");
                // nombreDT, edadDT, clubNombre
                DT dt = dts.stream()
                          .filter(x -> x.getNombre().equalsIgnoreCase(t[0]))
                          .findFirst()
                          .orElse(new DT(t[0], Double.parseDouble(t[1]), ""));
                Club c = new Club(t[2], dt);
                lista.add(c);
            }
        }
        return lista;
    }

    public List<Partido> cargarPartidos(String ruta) throws Exception {
        List<Partido> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea = br.readLine(); // cabecera
            while ((linea = br.readLine()) != null) {
                String[] t = linea.split(",");
                // fecha, equipoLocal, equipoVisitante, golesLocal, golesVisitante
                Partido p = new Partido(
                    t[0],
                    t[1],
                    t[2],
                    Double.parseDouble(t[3]),
                    Double.parseDouble(t[4])
                );
                lista.add(p);
            }
        }
        return lista;
    }

    public List<Jugador> filtrarJugadores(List<Jugador> todos, String club) {
        return todos.stream()
                    .filter(j -> j.getClub().equalsIgnoreCase(club))
                    .collect(Collectors.toList());
    }

    public List<Jugador> ordenarJugadores(List<Jugador> lista, String estadistica, boolean mayorAMenor) {
        Comparator<Jugador> cmp = Comparator.comparingDouble(j -> j.getValorEstadistica(estadistica));
        if (mayorAMenor) cmp = cmp.reversed();
        return lista.stream().sorted(cmp).collect(Collectors.toList());
    }

    public List<Club> ordenarClubes(List<Club> lista, Comparator<Club> cmp) {
        return lista.stream().sorted(cmp).collect(Collectors.toList());
    }

    public Club buscarClub(List<Club> lista, String nombre) {
        return lista.stream()
                    .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                    .findFirst().orElse(null);
    }

    public Jugador buscarJugador(List<Jugador> lista, String nombre) {
        return lista.stream()
                    .filter(j -> j.getNombre().equalsIgnoreCase(nombre))
                    .findFirst().orElse(null);
    }
}
