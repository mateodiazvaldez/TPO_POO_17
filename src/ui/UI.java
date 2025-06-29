package ui;

import model.*;
import service.GestorDeDatos;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;
import javax.swing.table.DefaultTableModel;

public class UI {
    private final GestorDeDatos gestor = new GestorDeDatos();
    private final Liga liga;

    public UI() {
        try {
            List<Jugador> jugadores = gestor.cargarJugadores("data/jugadores.csv");
            List<Club>    clubes    = gestor.cargarClubes("data/clubes.csv");
            List<Partido> partidos  = gestor.cargarPartidos("data/partidos.csv");
            // 1) Asignar jugadores a cada club y calcular valorPlantel
            for (Club c : clubes) {
                List<Jugador> subs = jugadores.stream()
                    .filter(j -> j.getClub().equalsIgnoreCase(c.getNombre()))
                    .toList();
                c.setJugadores(subs);
                c.setValorPlantel(gestor.calcularValorPlantel(c));
            }
            // crear Liga
            liga = new Liga("Primera División");
            liga.setJugadores(jugadores);
            liga.setClubes(clubes);
            liga.setPartidos(partidos);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error al cargar datos: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
        menuPrincipal();
    }

    public void menuPrincipal() {
        String[] opciones = {"Ver Liga","Ver Clubes","Ver Jugadores","Ver Partidos","Salir"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(null,
                "Menú Principal", "TP Fútbol",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opciones, opciones[0]);
            if (sel == 4 || sel == JOptionPane.CLOSED_OPTION) break;
            switch (sel) {
                case 0 -> menuLiga();
                case 1 -> menuClubes();
                case 2 -> menuJugadores();
                case 3 -> menuPartidos();
            }
        }
    }

    private void menuLiga() {
        String[] ops = {"Tabla de Posiciones","Ver Partidos","Volver"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(null,
                "Ver Liga", "Liga",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, ops, ops[0]);
            if (sel == 2 || sel == JOptionPane.CLOSED_OPTION) break;
            if (sel == 0) {
                mostrarTablaPosiciones(gestor.getTablaPosiciones(liga));
            } else {
                listarPartidos(liga.getPartidos());
            }
        }
    }

    private void menuClubes() {
        String[] ops = {"Listar Clubes","Ordenar Clubes","Comparar Clubes","Seleccionar Club","Volver"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(null,
                "Ver Clubes", "Clubes",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, ops, ops[0]);
            if (sel == 4 || sel == JOptionPane.CLOSED_OPTION) break;
            switch (sel) {
                case 0 -> listarClubes(liga.getClubes());
                case 1 -> ordenarClubes();
                case 2 -> compararClubes();
                case 3 -> seleccionarClub();
            }
        }
    }

    private void menuJugadores() {
        menuVerJugadores(liga.getJugadores());
    }

    private void menuVerJugadores(List<Jugador> lista) {
        String[] ops = {"Listar Jugadores","Ordenar Jugadores","Comparar Jugadores","Seleccionar Jugador","Volver"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(null,
                "Ver Jugadores", "Jugadores",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, ops, ops[0]);
            if (sel == 4 || sel == JOptionPane.CLOSED_OPTION) break;
            switch (sel) {
                case 0 -> listarJugadores(lista);
                case 1 -> ordenarJugadores(lista);
                case 2 -> compararJugadores();
                case 3 -> seleccionarJugador(lista);
            }
        }
    }

    private void menuPartidos() {
        String[] ops = {"Listar Todos","Filtrar por Club","Volver"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(null,
                "Ver Partidos", "Partidos",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, ops, ops[0]);
            if (sel == 2 || sel == JOptionPane.CLOSED_OPTION) break;
            if (sel == 0) {
                listarPartidos(liga.getPartidos());
            } else {
                Club c = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Selecciona Club");
                if (c != null)
                    listarPartidos(gestor.filtrarPartidos(liga.getPartidos(), c.getNombre()));
            }
        }
    }

    // —————————————
    // Helpers

    private <T> T seleccionarDeLista(List<T> items, Function<T,String> labeler, String title) {
        DefaultListModel<String> model = new DefaultListModel<>();
        items.forEach(i -> model.addElement(labeler.apply(i)));
        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setPreferredSize(new Dimension(250, 200));
        int ok = JOptionPane.showConfirmDialog(null, scroll, title,
                                               JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION && list.getSelectedIndex()>=0)
            return items.get(list.getSelectedIndex());
        return null;
    }

    private String formatear(double x) {
        return x == (long)x ? String.valueOf((long)x) : String.format("%.2f", x);
    }

    // —————————————
    // Salidas en JTable o texto

    private void mostrarTablaPosiciones(List<Club> tabla) {
        String[] cols = {"Club","Pts","PG","PP","PE"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        for (Club c : tabla) {
            m.addRow(new Object[]{
                c.getNombre(),
                formatear(c.getPuntos()),
                formatear(c.getPg()),
                formatear(c.getPp()),
                formatear(c.getPe())
            });
        }
        JTable table = new JTable(m);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(400, 200));
        JOptionPane.showMessageDialog(null, scroll,
            "Tabla de Posiciones", JOptionPane.PLAIN_MESSAGE);
    }

    private void listarClubes(List<Club> clubes) {
        String[] cols = {"Club","Dt","VP"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        for (Club c : clubes) {
            m.addRow(new Object[]{ c.getNombre(), c.getDt().getNombre(), c.getValorPlantel() });
        }
        JTable table = new JTable(m);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(400, 200));
        JOptionPane.showMessageDialog(null, scroll,
            "Clubes", JOptionPane.PLAIN_MESSAGE);
    }

    private void ordenarClubes() {
        String[] opts = {"PG","PP","Pts","ValorPlantel"};
        int sel = JOptionPane.showOptionDialog(null,
            "Criterio de orden","Ordenar Clubes",
            JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,
            null,opts,opts[0]);
        if (sel==-1) return;
        boolean desc = JOptionPane.showConfirmDialog(null,
            "Orden descendente?","Dirección",
            JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;
        mostrarTablaPosiciones(
            gestor.ordenarClubes(liga.getClubes(), sel+1, desc)
        );
    }

    private void compararClubes() {
        Club c1 = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Club 1");
        Club c2 = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Club 2");
        if (c1==null || c2==null || c1==c2) return;
        mostrarTablaPosiciones(List.of(c1,c2));
    }

    private void seleccionarClub() {
        Club c = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Elige Club");
        if (c==null) return;
        String[] ops = {"Ver DT","Ver Estadísticas","Ver Jugadores","Volver"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(null,
                "Club " + c.getNombre(),"Opciones Club",
                JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,
                null,ops,ops[0]);
            if (sel<0 || sel==3) break;
            switch(sel) {
                case 0 -> // mostrar nombre de DT
                    JOptionPane.showMessageDialog(null,
                        "DT: " + c.getDt().getNombre(),
                        "Director Técnico", JOptionPane.INFORMATION_MESSAGE);
                case 1 -> listarEstadisticasClub(c);
                case 2 -> menuVerJugadores(
                             gestor.filtrarJugadores(liga.getJugadores(), c.getNombre()));
            }
        }
    }

    private void listarEstadisticasClub(Club c) {
        String[] cols = {"PG","PP","PE","Pts","VP"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        m.addRow(new Object[]{
            formatear(c.getPg()),
            formatear(c.getPp()),
            formatear(c.getPe()),
            formatear(c.getPuntos()),
            c.getValorPlantel()
        });
        JTable t = new JTable(m);
        JScrollPane scroll = new JScrollPane(t);
        scroll.setPreferredSize(new Dimension(400,80));
        JOptionPane.showMessageDialog(null, scroll,
            "Estadísticas Club", JOptionPane.PLAIN_MESSAGE);
    }

    private void listarPartidos(List<Partido> ps) {
        String[] cols = {"Fecha","Local","GL","GV","Visitante"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        for (Partido p : ps) {
            m.addRow(new Object[]{
                p.getFecha(),
                p.getEquipoLocal(),
                formatear(p.getGolesLocal()),
                formatear(p.getGolesVisitante()),
                p.getEquipoVisitante()
            });
        }
        JTable t = new JTable(m);
        JScrollPane scroll = new JScrollPane(t);
        scroll.setPreferredSize(new Dimension(500,200));
        JOptionPane.showMessageDialog(null, scroll,
            "Partidos", JOptionPane.PLAIN_MESSAGE);
    }

    private void listarJugadores(List<Jugador> js) {
        String[] cols = {"Jugador","Goles","Asis","Vallas","Valor"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        for (Jugador j : js) {
            m.addRow(new Object[]{
                j.getNombre(),
                formatear(j.getGoles()),
                formatear(j.getAsistencias()),
                formatear(j.getVallas()),
                j.getValorMercado()
            });
        }
        JTable t = new JTable(m);
        JScrollPane scroll = new JScrollPane(t);
        scroll.setPreferredSize(new Dimension(500,200));
        JOptionPane.showMessageDialog(null, scroll,
            "Jugadores", JOptionPane.PLAIN_MESSAGE);
    }

    private void ordenarJugadores(List<Jugador> lista) {
        String[] opts = {"Goles","Asistencias","Vallas","ValorMercado"};
        int sel = JOptionPane.showOptionDialog(null,
            "Criterio de orden","Ordenar Jugadores",
            JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,
            null,opts,opts[0]);
        if (sel==-1) return;
        boolean desc = JOptionPane.showConfirmDialog(null,
            "Orden descendente?","Dirección",
            JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;
        listarJugadores(gestor.ordenarJugadores(lista, sel+1, desc));
    }

    private void compararJugadores() {
        Jugador j1 = seleccionarDeLista(liga.getJugadores(), Jugador::getNombre, "Jugador 1");
        Jugador j2 = seleccionarDeLista(liga.getJugadores(), Jugador::getNombre, "Jugador 2");
        if (j1==null || j2==null || j1==j2) return;
        listarJugadores(List.of(j1, j2));
    }

    private void seleccionarJugador(List<Jugador> lista) {
        while (true) {
            Jugador j = seleccionarDeLista(lista, Jugador::getNombre, "Elige Jugador");
            if (j==null) break;
            String[] ops = {"Ver Estadísticas","Seleccionar Otro","Volver"};
            int sel = JOptionPane.showOptionDialog(null,
                "Jugador " + j.getNombre(),"Opciones Jugador",
                JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,
                null,ops,ops[0]);
            if (sel<0 || sel==2) break;
            if (sel==0) listarEstadisticasJugador(j);
        }
    }

    private void listarEstadisticasJugador(Jugador j) {
        String[] cols = {"Goles","Asis","Vallas","Valor"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        m.addRow(new Object[]{
            formatear(j.getGoles()),
            formatear(j.getAsistencias()),
            formatear(j.getVallas()),
            j.getValorMercado()
        });
        j.getEstadisticas().forEach((k,v) ->
            m.addRow(new Object[]{k, formatear(v), "", ""})
        );
        JTable t = new JTable(m);
        JScrollPane scroll = new JScrollPane(t);
        scroll.setPreferredSize(new Dimension(400,200));
        JOptionPane.showMessageDialog(null, scroll,
            "Estadísticas Jugador", JOptionPane.PLAIN_MESSAGE);
    }
}