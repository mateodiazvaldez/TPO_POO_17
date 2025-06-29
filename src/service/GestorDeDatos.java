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
                Jugador j = new Jugador(
                    t[0],
                    Integer.parseInt(t[3]),
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

    public List<Club> cargarClubes(String ruta) throws Exception {
        List<Club> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea = br.readLine(); // cabecera
            while ((linea = br.readLine()) != null) {
                String[] t = linea.split(",");
                DT dt = new DT(t[0], Double.parseDouble(t[1]), t[2]);
                Club c = new Club(
                    t[2], dt,
                    0.0, 0.0, 0.0, 0.0,
                    0.0, new ArrayList<>(), 0
                );
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
                Partido p = new Partido(
                    t[0], t[1], t[2],
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

    public List<Partido> filtrarPartidos(List<Partido> todos, String club) {
        return todos.stream()
                    .filter(p -> p.getEquipoLocal().equalsIgnoreCase(club)
                              || p.getEquipoVisitante().equalsIgnoreCase(club))
                    .collect(Collectors.toList());
    }

    public List<Jugador> ordenarJugadores(List<Jugador> lista,
                                          int criterio,
                                          boolean mayorAMenor) {
        Comparator<Jugador> cmp;
        switch (criterio) {
            case 1: cmp = Comparator.comparingDouble(Jugador::getGoles); break;
            case 2: cmp = Comparator.comparingDouble(Jugador::getAsistencias); break;
            case 3: cmp = Comparator.comparingDouble(Jugador::getVallas); break;
            case 4: cmp = Comparator.comparingInt(Jugador::getValorMercado); break;
            default: throw new IllegalArgumentException("Criterio inválido");
        }
        if (mayorAMenor) cmp = cmp.reversed();
        return lista.stream().sorted(cmp).collect(Collectors.toList());
    }

    public List<Club> ordenarClubes(List<Club> lista,
                                    int criterio,
                                    boolean mayorAMenor) {
        Comparator<Club> cmp;
        switch (criterio) {
            case 1: cmp = Comparator.comparingDouble(Club::getPg); break;
            case 2: cmp = Comparator.comparingDouble(Club::getPp); break;
            case 3: cmp = Comparator.comparingDouble(Club::getPuntos); break;
            case 4: cmp = Comparator.comparingInt(Club::getValorPlantel); break;
            default: throw new IllegalArgumentException("Criterio inválido");
        }
        if (mayorAMenor) cmp = cmp.reversed();
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

    /**
     * Calcula PJ, PG, PE, PP y puntos según los partidos cargados en la liga.
     */
    public void calcularClasificacion(Liga liga) {
        // Reiniciar contadores
        for (Club c : liga.getClubes()) {
            c.setPj(0.0);
            c.setPg(0.0);
            c.setPe(0.0);
            c.setPp(0.0);
            c.setPuntos(0.0);
        }
        // Recuento de partidos
        for (Partido p : liga.getPartidos()) {
            Club local  = buscarClub(liga.getClubes(), p.getEquipoLocal());
            Club visita = buscarClub(liga.getClubes(), p.getEquipoVisitante());
            if (local == null || visita == null) continue;

            local.setPj(local.getPj() + 1);
            visita.setPj(visita.getPj() + 1);

            if (p.getGolesLocal() > p.getGolesVisitante()) {
                local.setPg(local.getPg() + 1);
                local.setPuntos(local.getPuntos() + 3);
                visita.setPp(visita.getPp() + 1);
            } else if (p.getGolesLocal() < p.getGolesVisitante()) {
                visita.setPg(visita.getPg() + 1);
                visita.setPuntos(visita.getPuntos() + 3);
                local.setPp(local.getPp() + 1);
            } else {
                local.setPe(local.getPe() + 1);
                visita.setPe(visita.getPe() + 1);
                local.setPuntos(local.getPuntos() + 1);
                visita.setPuntos(visita.getPuntos() + 1);
            }
        }
    }

    /**
     * Devuelve la tabla de posiciones ordenada por puntos descendente,
     * mostrando solo Pts, PG, PP y PE.
     */
    public List<Club> getTablaPosiciones(Liga liga) {
        calcularClasificacion(liga);
        List<Club> tabla = new ArrayList<>(liga.getClubes());
        tabla.sort(Comparator.comparingDouble(Club::getPuntos).reversed());
        return tabla;
    }
}
