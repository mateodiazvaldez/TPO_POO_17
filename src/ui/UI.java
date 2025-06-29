package ui;

import model.*;
import service.GestorDeDatos;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;

public class UI {
    private final GestorDeDatos gestor = new GestorDeDatos();
    private final Liga liga;

    public UI() {
        try {
            List<Jugador> jugadores = gestor.cargarJugadores("data/jugadores.csv");
            List<Club>    clubes    = gestor.cargarClubes("data/clubes.csv");
            List<Partido> partidos  = gestor.cargarPartidos("data/partidos.csv");
            liga = new Liga("Primera División");
            liga.setJugadores(jugadores);
            liga.setClubes(clubes);
            liga.setPartidos(partidos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
        menuPrincipal();
    }

    public void menuPrincipal() {
        while (true) {
            String op = JOptionPane.showInputDialog(
                "Menú Principal\n" +
                "1) Ver Liga\n" +
                "2) Ver Clubes\n" +
                "3) Ver Jugadores\n" +
                "4) Ver Partidos\n" +
                "5) Salir\n" +
                "Elige opción:"
            );
            if (op == null || op.equals("5")) break;
            switch (op) {
                case "1": menuLiga();     break;
                case "2": menuClubes();   break;
                case "3": menuJugadores();break;
                case "4": menuPartidos(); break;
            }
        }
    }

    public void menuLiga() {
        while (true) {
            String op = JOptionPane.showInputDialog(
                "Ver Liga\n" +
                "1) Tabla de Posiciones\n" +
                "2) Ver Partidos\n" +
                "3) Volver\n" +
                "Elige opción:"
            );
            if (op == null || op.equals("3")) break;
            switch (op) {
                case "1" ->
                    mostrarTablaPosiciones(gestor.getTablaPosiciones(liga));
                case "2" ->
                    listarPartidos(liga.getPartidos());
            }
        }
    }

    public void menuClubes() {
        while (true) {
            String op = JOptionPane.showInputDialog(
                "Ver Clubes\n" +
                "1) Listar Clubes\n" +
                "2) Ordenar Clubes\n" +
                "3) Comparar Clubes\n" +
                "4) Seleccionar Club\n" +
                "5) Volver\n" +
                "Elige opción:"
            );
            if (op == null || op.equals("5")) break;
            switch (op) {
                case "1": listarClubes(liga.getClubes());    break;
                case "2": ordenarClubes();                   break;
                case "3": compararClubes();                  break;
                case "4": seleccionarClub();                 break;
            }
        }
    }

    public void menuJugadores() {
        menuVerJugadores(liga.getJugadores());
    }

    private void menuVerJugadores(List<Jugador> lista) {
        while (true) {
            String op = JOptionPane.showInputDialog(
                "Ver Jugadores\n" +
                "1) Listar Jugadores\n" +
                "2) Ordenar Jugadores\n" +
                "3) Comparar Jugadores\n" +
                "4) Seleccionar Jugador\n" +
                "5) Volver\n" +
                "Elige opción:"
            );
            if (op == null || op.equals("5")) break;
            switch (op) {
                case "1": listarJugadores(lista);            break;
                case "2": ordenarJugadores(lista);           break;
                case "3": compararJugadores();               break;
                case "4": seleccionarJugador(lista);         break;
            }
        }
    }

    public void menuPartidos() {
        while (true) {
            String op = JOptionPane.showInputDialog(
                "Ver Partidos\n" +
                "1) Listar Todos\n" +
                "2) Filtrar por Club\n" +
                "3) Volver\n" +
                "Elige opción:"
            );
            if (op == null || op.equals("3")) break;
            switch (op) {
                case "1":
                    listarPartidos(liga.getPartidos());
                    break;
                case "2":
                    Club club = seleccionarDeLista(
                        liga.getClubes(), Club::getNombre, "Selecciona Club"
                    );
                    if (club != null) {
                        listarPartidos(
                            gestor.filtrarPartidos(
                                liga.getPartidos(), club.getNombre()
                            )
                        );
                    }
                    break;
            }
        }
    }

    // -----------------------------------
    // Helpers de selección y formateo

    private <T> T seleccionarDeLista(List<T> items,
                                     Function<T,String> labeler,
                                     String title) {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (T it : items) model.addElement(labeler.apply(it));
        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setPreferredSize(new Dimension(250, 200));
        int res = JOptionPane.showConfirmDialog(
            null, scroll, title, JOptionPane.OK_CANCEL_OPTION
        );
        if (res == JOptionPane.OK_OPTION && list.getSelectedIndex() >= 0) {
            return items.get(list.getSelectedIndex());
        }
        return null;
    }

    private String formatear(double x) {
        return x == (long)x
            ? String.valueOf((long)x)
            : String.format("%.2f", x);
    }

    // -----------------------------------
    // Listados y comparaciones

    private void mostrarTablaPosiciones(List<Club> tabla) {
        StringBuilder sb = new StringBuilder("Club | Pts | PG | PP | PE\n");
        for (Club c : tabla) {
            sb.append(c.getNombre()).append(" | ")
              .append(formatear(c.getPuntos())).append(" | ")
              .append(formatear(c.getPg())).append(" | ")
              .append(formatear(c.getPp())).append(" | ")
              .append(formatear(c.getPe())).append("\n");
        }
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(300, 200));
        JOptionPane.showMessageDialog(
            null, scroll, "Tabla de Posiciones", JOptionPane.PLAIN_MESSAGE
        );
    }

    private void listarClubes(List<Club> clubes) {
        JTextArea area = new JTextArea();
        for (Club c : clubes) {
            area.append(c.getNombre());
            area.append("\n");
        }
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(200, 200));
        JOptionPane.showMessageDialog(
            null, scroll, "Clubes", JOptionPane.PLAIN_MESSAGE
        );
    }

    private void ordenarClubes() {
        String op = JOptionPane.showInputDialog(
            "Ordenar Clubes por:\n" +
            "1) PG\n2) PP\n3) Puntos\n4) Valor Plantel\n" +
            "Elige opción:"
        );
        if (op == null) return;
        int crit = Integer.parseInt(op);
        boolean desc = JOptionPane.showConfirmDialog(
            null, "¿Orden descendente?", "Dirección",
            JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
        mostrarTablaPosiciones(
            gestor.ordenarClubes(liga.getClubes(), crit, desc)
        );
    }

    private void compararClubes() {
        Club c1 = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Club 1");
        Club c2 = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Club 2");
        if (c1 != null && c2 != null) {
            mostrarTablaPosiciones(List.of(c1, c2));
        }
    }

    private void seleccionarClub() {
        Club club = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Elige Club");
        if (club == null) return;
        while (true) {
            String op = JOptionPane.showInputDialog(
                "Club " + club.getNombre() + "\n" +
                "1) Ver Estadísticas\n" +
                "2) Ver Jugadores\n" +
                "3) Volver\n" +
                "Elige opción:"
            );
            if (op == null || op.equals("3")) break;
            if (op.equals("1")) {
                listarEstadisticasClub(club);
            } else if (op.equals("2")) {
                menuVerJugadores(
                    gestor.filtrarJugadores(
                        liga.getJugadores(), club.getNombre()
                    )
                );
            }
        }
    }

    private void listarEstadisticasClub(Club c) {
        String msg = String.format(
            "PG: %s | PP: %s | PE: %s | Pts: %s | ValorPlantel: %d",
            formatear(c.getPg()), formatear(c.getPp()),
            formatear(c.getPe()), formatear(c.getPuntos()),
            c.getValorPlantel()
        );
        JOptionPane.showMessageDialog(
            null, msg, "Estadísticas Club", JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void listarPartidos(List<Partido> partidos) {
        JTextArea area = new JTextArea();
        for (Partido p : partidos) {
            area.append(String.format(
                "%s: %s %s-%s %s\n",
                p.getFecha(),
                p.getEquipoLocal(), formatear(p.getGolesLocal()),
                formatear(p.getGolesVisitante()), p.getEquipoVisitante()
            ));
        }
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(300, 200));
        JOptionPane.showMessageDialog(
            null, scroll, "Partidos", JOptionPane.PLAIN_MESSAGE
        );
    }

    private void listarJugadores(List<Jugador> jugadores) {
        JTextArea area = new JTextArea();
        for (Jugador j : jugadores) {
            area.append(String.format(
                "%s – Goles: %s, Asis: %s, Vallas: %s, Valor: %d\n",
                j.getNombre(),
                formatear(j.getGoles()), formatear(j.getAsistencias()),
                formatear(j.getVallas()), j.getValorMercado()
            ));
        }
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(300, 200));
        JOptionPane.showMessageDialog(
            null, scroll, "Jugadores", JOptionPane.PLAIN_MESSAGE
        );
    }

    private void ordenarJugadores(List<Jugador> lista) {
        String op = JOptionPane.showInputDialog(
            "Ordenar Jugadores por:\n" +
            "1) Goles\n2) Asistencias\n3) Vallas\n4) Valor Mercado\n" +
            "Elige opción:"
        );
        if (op == null) return;
        int crit = Integer.parseInt(op);
        boolean desc = JOptionPane.showConfirmDialog(
            null, "¿Orden descendente?", "Dirección",
            JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
        listarJugadores(gestor.ordenarJugadores(lista, crit, desc));
    }

    private void compararJugadores() {
        Jugador j1 = seleccionarDeLista(liga.getJugadores(), Jugador::getNombre, "Jugador 1");
        Jugador j2 = seleccionarDeLista(liga.getJugadores(), Jugador::getNombre, "Jugador 2");
        if (j1 != null && j2 != null) {
            listarEstadisticasJugador(j1);
            listarEstadisticasJugador(j2);
        }
    }

    private void seleccionarJugador(List<Jugador> lista) {
        while (true) {
            Jugador j = seleccionarDeLista(lista, Jugador::getNombre, "Elige Jugador");
            if (j == null) break;
            String op = JOptionPane.showInputDialog(
                "Jugador " + j.getNombre() + "\n" +
                "1) Ver Estadísticas\n" +
                "2) Seleccionar Otro\n" +
                "3) Volver\n" +
                "Elige opción:"
            );
            if (op == null || op.equals("3")) break;
            if (op.equals("1")) listarEstadisticasJugador(j);
        }
    }

    private void listarEstadisticasJugador(Jugador j) {
        StringBuilder sb = new StringBuilder(j.getNombre() + " stats:\n");
        sb.append("Goles: ").append(formatear(j.getGoles()))
          .append(" | Asis: ").append(formatear(j.getAsistencias()))
          .append(" | Vallas: ").append(formatear(j.getVallas()))
          .append(" | Valor: ").append(j.getValorMercado()).append("\n");
        j.getEstadisticas().forEach((k, v) ->
            sb.append(k).append(": ").append(formatear(v)).append("\n")
        );
        JOptionPane.showMessageDialog(
            null, sb.toString(),
            "Estadísticas Jugador", JOptionPane.INFORMATION_MESSAGE
        );
    }
}
