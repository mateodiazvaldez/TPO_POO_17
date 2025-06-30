package datos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import clases.*;

public class GestorDeDatos {

    public List<Jugador> cargarJugadores(String ruta) throws Exception {
        List<Jugador> jugadores = new ArrayList<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(ruta))) {
            String cabecera = lector.readLine();
            String[] columnas = cabecera.split(",");
            List<Integer> indicesExtras = new ArrayList<>();
            List<String> nombresExtras = new ArrayList<>();

            for (int i = 0; i < columnas.length; i++) {
                String columna = columnas[i].trim();
                if (!(columna.equals("nombre") || columna.equals("club") ||
                      columna.equals("goles") || columna.equals("edad") ||
                      columna.equals("asistencias") || columna.equals("vallas") ||
                      columna.equals("valorMercado"))) {
                    indicesExtras.add(i);
                    nombresExtras.add(columna);
                }
            }

            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");
                Jugador jugador = new Jugador(
                    datos[0].trim(),                            // nombre
                    Integer.parseInt(datos[3].trim()),          // edad
                    datos[1].trim(),                            // club
                    Double.parseDouble(datos[2].trim()),        // goles
                    Double.parseDouble(datos[4].trim()),        // asistencias
                    Double.parseDouble(datos[5].trim()),        // vallas
                    Integer.parseInt(datos[6].trim())           // valor mercado
                );
                for (int i = 0; i < indicesExtras.size(); i++) {
                    int indice = indicesExtras.get(i);
                    double valor = Double.parseDouble(datos[indice].trim());
                    jugador.agregarEstadistica(nombresExtras.get(i), valor);
                }
                jugadores.add(jugador);
            }
        }
        return jugadores;
    }

    public List<Club> cargarClubes(String ruta) throws Exception {
        List<Club> clubes = new ArrayList<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(ruta))) {
            String linea = lector.readLine(); // cabecera
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");
                DT directorTecnico = new DT(
                    datos[0].trim(),
                    Double.parseDouble(datos[1].trim()),
                    datos[2].trim()
                );
                Club club = new Club(
                    datos[2].trim(),
                    directorTecnico,
                    0.0, 0.0, 0.0, 0.0,
                    0.0,
                    new ArrayList<>(),
                    0
                );
                clubes.add(club);
            }
        }
        return clubes;
    }

    public List<Partido> cargarPartidos(String ruta) throws Exception {
        List<Partido> partidos = new ArrayList<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(ruta))) {
            String cabecera = lector.readLine();
            String[] columnas = cabecera.split(",");
            List<Integer> indicesExtras = new ArrayList<>();
            List<String> nombresExtras = new ArrayList<>();

            for (int i = 0; i < columnas.length; i++) {
                String columna = columnas[i].trim();
                if (!(columna.equals("fecha") || columna.equals("equipoLocal") ||
                      columna.equals("equipoVisitante") || columna.equals("golesLocal") ||
                      columna.equals("golesVisitante"))) {
                    indicesExtras.add(i);
                    nombresExtras.add(columna);
                }
            }

            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");
                Partido partido = new Partido(
                    datos[0].trim(),                          // fecha
                    datos[1].trim(),                          // equipo local
                    datos[2].trim(),                          // equipo visitante
                    Double.parseDouble(datos[3].trim()),      // goles local
                    Double.parseDouble(datos[4].trim())       // goles visitante
                );
                for (int i = 0; i < indicesExtras.size(); i++) {
                    int indice = indicesExtras.get(i);
                    double valor = Double.parseDouble(datos[indice].trim());
                    partido.agregarEstadistica(nombresExtras.get(i), valor);
                }
                partidos.add(partido);
            }
        }
        return partidos;
    }

    public List<Jugador> filtrarJugadores(List<Jugador> todos, String club) {
        List<Jugador> resultado = new ArrayList<>();
        for (Jugador jugador : todos) {
            if (jugador.getClub().trim().equalsIgnoreCase(club.trim())) {
                resultado.add(jugador);
            }
        }
        return resultado;
    }

    public List<Partido> filtrarPartidos(List<Partido> todos, String club) {
        List<Partido> resultado = new ArrayList<>();
        for (Partido partido : todos) {
            if (partido.getEquipoLocal().trim().equalsIgnoreCase(club.trim()) ||
                partido.getEquipoVisitante().trim().equalsIgnoreCase(club.trim())) {
                resultado.add(partido);
            }
        }
        return resultado;
    }

    public List<Jugador> ordenarJugadores(List<Jugador> jugadores, int criterio, boolean mayorAMenor) {
        List<Jugador> copia = new ArrayList<>(jugadores);
        Comparator<Jugador> comparador;
        switch (criterio) {
            case 1: comparador = Comparator.comparingDouble(Jugador::getGoles); break;
            case 2: comparador = Comparator.comparingDouble(Jugador::getAsistencias); break;
            case 3: comparador = Comparator.comparingDouble(Jugador::getVallas); break;
            case 4: comparador = Comparator.comparingInt(Jugador::getValorMercado); break;
            default: throw new IllegalArgumentException("Criterio inválido");
        }
        if (mayorAMenor) {
            comparador = comparador.reversed();
        }
        Collections.sort(copia, comparador);
        return copia;
    }

    public List<Club> ordenarClubes(List<Club> clubes, int criterio, boolean mayorAMenor) {
        List<Club> copia = new ArrayList<>(clubes);
        Comparator<Club> comparador;
        switch (criterio) {
            case 1: comparador = Comparator.comparingDouble(Club::getPg); break;
            case 2: comparador = Comparator.comparingDouble(Club::getPp); break;
            case 3: comparador = Comparator.comparingDouble(Club::getPuntos); break;
            case 4: comparador = Comparator.comparingInt(Club::getValorPlantel); break;
            default: throw new IllegalArgumentException("Criterio inválido");
        }
        if (mayorAMenor) {
            comparador = comparador.reversed();
        }
        Collections.sort(copia, comparador);
        return copia;
    }

    public Club buscarClub(List<Club> clubes, String nombre) {
        for (Club club : clubes) {
            if (club.getNombre().trim().equalsIgnoreCase(nombre.trim())) {
                return club;
            }
        }
        return null;
    }

    public Jugador buscarJugador(List<Jugador> jugadores, String nombre) {
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().trim().equalsIgnoreCase(nombre.trim())) {
                return jugador;
            }
        }
        return null;
    }

    public void calcularClasificacion(Liga liga) {
        for (Club club : liga.getClubes()) {
            club.setPj(0.0);
            club.setPg(0.0);
            club.setPe(0.0);
            club.setPp(0.0);
            club.setPuntos(0.0);
        }

        for (Partido partido : liga.getPartidos()) {
            Club local = buscarClub(liga.getClubes(), partido.getEquipoLocal());
            Club visitante = buscarClub(liga.getClubes(), partido.getEquipoVisitante());
            if (local == null || visitante == null) continue;

            local.setPj(local.getPj() + 1);
            visitante.setPj(visitante.getPj() + 1);

            if (partido.getGolesLocal() > partido.getGolesVisitante()) {
                local.setPg(local.getPg() + 1);
                local.setPuntos(local.getPuntos() + 3);
                visitante.setPp(visitante.getPp() + 1);
            } else if (partido.getGolesLocal() < partido.getGolesVisitante()) {
                visitante.setPg(visitante.getPg() + 1);
                visitante.setPuntos(visitante.getPuntos() + 3);
                local.setPp(local.getPp() + 1);
            } else {
                local.setPe(local.getPe() + 1);
                visitante.setPe(visitante.getPe() + 1);
                local.setPuntos(local.getPuntos() + 1);
                visitante.setPuntos(visitante.getPuntos() + 1);
            }
        }
    }

    public List<Club> getTablaPosiciones(Liga liga) {
        calcularClasificacion(liga);
        List<Club> tabla = new ArrayList<>(liga.getClubes());
        Collections.sort(tabla, Comparator.comparingDouble(Club::getPuntos).reversed());
        return tabla;
    }
}
