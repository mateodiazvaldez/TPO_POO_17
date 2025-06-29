// src/ui/UI.java
package ui;

import model.*;
import service.GestorDeDatos;

import javax.swing.JOptionPane;
import java.util.List;

public class UI {
    private GestorDeDatos gestor;
    private Liga liga;

    public UI() {
        gestor = new GestorDeDatos();
        try {
            List<Jugador> jugadores = gestor.cargarJugadores("data/jugadores.csv");
            List<Club> clubes    = gestor.cargarClubes("data/clubes.csv");
            List<Partido> partidos= gestor.cargarPartidos("data/partidos.csv");
            liga = new Liga("Primera División");
            liga.setJugadores(jugadores);
            liga.setClubes(clubes);
            liga.setPartidos(partidos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error al cargar datos: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public void menuPrincipal() {
        while (true) {
            String s = JOptionPane.showInputDialog(
                "## Menú Principal\n" +
                "1. Ver Liga\n" +
                "2. Ver Clubes\n" +
                "3. Ver Jugadores\n" +
                "4. Ver Partidos\n" +
                "5. Salir"
            );
            if (s == null) return;
            switch (s) {
                case "1": menuLiga(); break;
                case "2": menuClubes(); break;
                case "3": menuJugadores(); break;
                case "4": menuPartidos(); break;
                case "5": System.exit(0);
                default: /* ignorar */ ;
            }
        }
    }

    public void menuLiga() {
        String s = JOptionPane.showInputDialog(
            "## Ver Liga\n" +
            "1. Mostrar Tabla de Posiciones\n" +
            "2. Ver Partidos de la Liga\n" +
            "3. Volver"
        );
        if (s == null || s.equals("3")) return;
        switch (s) {
            case "1":
                gestor.calcularClasificacion(liga);
                mostrarTabla(liga.getClubes());
                break;
            case "2":
                listarPartidos(liga.getPartidos());
                break;
            default:
        }
    }

    public void menuClubes() {
        String s = JOptionPane.showInputDialog(
            "## Ver Clubes\n" +
            "1. Listar Clubes\n" +
            "2. Ordenar Clubes\n" +
            "3. Comparar Clubes\n" +
            "4. Seleccionar Club\n" +
            "5. Volver"
        );
        if (s == null || s.equals("5")) return;
        switch (s) {
            case "1": listarClubes(liga.getClubes()); break;
            case "2": ordenarClubes(); break;
            case "3": compararClubes(); break;
            case "4": seleccionarClub(); break;
            default:
        }
    }

    private void ordenarClubes() {
        String input = JOptionPane.showInputDialog(
            "Ordenar Clubes por:\n" +
            "1) PG\n2) PP\n3) Puntos\n4) Valor Plantel"
        );
        if (input == null) return;
        int criterio = Integer.parseInt(input);
        boolean desc = JOptionPane.showConfirmDialog(null,
            "¿Mayor a Menor?","Dirección",
            JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;
        List<Club> orden = gestor.ordenarClubes(
            liga.getClubes(), criterio, desc
        );
        mostrarTabla(orden);
    }

    private void compararClubes() {
        String c1 = JOptionPane.showInputDialog("Nombre primer club:");
        String c2 = JOptionPane.showInputDialog("Nombre segundo club:");
        Club club1 = gestor.buscarClub(liga.getClubes(), c1);
        Club club2 = gestor.buscarClub(liga.getClubes(), c2);
        if (club1!=null && club2!=null) {
            String msg =
                club1.getNombre()+": Pts=" + club1.getPuntos() + "\n" +
                club2.getNombre()+": Pts=" + club2.getPuntos();
            JOptionPane.showMessageDialog(null, msg,
                "Comparación", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void seleccionarClub() {
        String c = JOptionPane.showInputDialog("Nombre del club:");
        Club club = gestor.buscarClub(liga.getClubes(), c);
        if (club == null) return;
        String opt = JOptionPane.showInputDialog(
            "1) Ver Estadísticas\n" +
            "2) Ver Jugadores\n" +
            "3) Volver"
        );
        if ("1".equals(opt)) {
            gestor.calcularClasificacion(liga);
            listarEstadisticasClub(club);
        } else if ("2".equals(opt)) {
            listarJugadores(
                gestor.filtrarJugadores(liga.getJugadores(), club.getNombre())
            );
        }
    }

    public void menuJugadores() {
        String s = JOptionPane.showInputDialog(
            "## Ver Jugadores\n" +
            "1. Listar Jugadores\n" +
            "2. Ordenar Jugadores\n" +
            "3. Comparar Jugadores\n" +
            "4. Seleccionar Jugador\n" +
            "5. Volver"
        );
        if (s == null || s.equals("5")) return;
        switch (s) {
            case "1": listarJugadores(liga.getJugadores()); break;
            case "2": ordenarJugadores(); break;
            case "3": compararJugadores(); break;
            case "4": seleccionarJugador(); break;
            default:
        }
    }

    private void ordenarJugadores() {
        String input = JOptionPane.showInputDialog(
            "Ordenar Jugadores por:\n" +
            "1) Goles\n2) Asistencias\n3) Vallas\n4) Valor Mercado"
        );
        if (input == null) return;
        int criterio = Integer.parseInt(input);
        boolean desc = JOptionPane.showConfirmDialog(null,
            "¿Mayor a Menor?","Dirección",
            JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;
        listarJugadores(
            gestor.ordenarJugadores(liga.getJugadores(), criterio, desc)
        );
    }

    private void compararJugadores() {
        String j1 = JOptionPane.showInputDialog("Nombre primer jugador:");
        String j2 = JOptionPane.showInputDialog("Nombre segundo jugador:");
        Jugador p1 = gestor.buscarJugador(liga.getJugadores(), j1);
        Jugador p2 = gestor.buscarJugador(liga.getJugadores(), j2);
        if (p1!=null && p2!=null) {
            String msg =
                p1.getNombre()+": Goles=" + p1.getGoles() + "\n" +
                p2.getNombre()+": Goles=" + p2.getGoles();
            JOptionPane.showMessageDialog(null, msg,
                "Comparación", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void seleccionarJugador() {
        String j = JOptionPane.showInputDialog("Nombre del jugador:");
        Jugador p = gestor.buscarJugador(liga.getJugadores(), j);
        if (p == null) return;
        String opt = JOptionPane.showInputDialog(
            "1) Ver Estadísticas\n" +
            "2) Volver"
        );
        if ("1".equals(opt)) {
            listarEstadisticasJugador(p);
        }
    }

    public void menuPartidos() {
        String s = JOptionPane.showInputDialog(
            "## Ver Partidos\n" +
            "1. Listar Todos los Partidos\n" +
            "2. Filtrar Partidos por Club\n" +
            "3. Volver"
        );
        if (s == null || s.equals("3")) return;
        switch (s) {
            case "1": listarPartidos(liga.getPartidos()); break;
            case "2":
                String c = JOptionPane.showInputDialog("Nombre del club:");
                listarPartidos(
                    gestor.filtrarPartidos(liga.getPartidos(), c)
                );
                break;
        }
    }

    public void listarPartidos(List<Partido> partidos) {
        StringBuilder sb = new StringBuilder("Partidos:\n");
        for (Partido p : partidos) {
            sb.append(p.getFecha())
              .append(" ").append(p.getEquipoLocal())
              .append(" ").append(p.getGolesLocal())
              .append("-").append(p.getGolesVisitante())
              .append(" ").append(p.getEquipoVisitante())
              .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public void listarClubes(List<Club> clubes) {
        StringBuilder sb = new StringBuilder("Clubes:\n");
        for (Club c : clubes) {
            sb.append(c.getNombre()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public void listarJugadores(List<Jugador> jugadores) {
        StringBuilder sb = new StringBuilder("Jugadores:\n");
        for (Jugador j : jugadores) {
            sb.append(j.getNombre()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public void mostrarTabla(List<Club> clubes) {
        StringBuilder sb = new StringBuilder("Posiciones:\n");
        for (Club c : clubes) {
            sb.append(c.getNombre())
              .append(" Pts=").append(c.getPuntos()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public void listarEstadisticasJugador(Jugador j) {
        StringBuilder sb = new StringBuilder("Estadísticas de ")
            .append(j.getNombre()).append(":\n")
            .append("Goles: ").append(j.getGoles()).append("\n")
            .append("Asistencias: ").append(j.getAsistencias()).append("\n")
            .append("Vallas: ").append(j.getVallas()).append("\n")
            .append("Valor Mercado: ").append(j.getValorMercado()).append("\n");
        j.getEstadisticas().forEach((k,v) ->
            sb.append(k).append(": ").append(v).append("\n")
        );
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public void listarEstadisticasClub(Club c) {
        StringBuilder sb = new StringBuilder("Estadísticas de ")
            .append(c.getNombre()).append(":\n")
            .append("PJ: ").append(c.getPj()).append("\n")
            .append("PG: ").append(c.getPg()).append("\n")
            .append("PE: ").append(c.getPe()).append("\n")
            .append("PP: ").append(c.getPp()).append("\n")
            .append("Puntos: ").append(c.getPuntos()).append("\n")
            .append("Valor Plantel: ").append(c.getValorPlantel()).append("\n");
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}
