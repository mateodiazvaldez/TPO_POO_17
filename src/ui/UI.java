// src/ui/UI.java
package ui;

import model.*;
import service.GestorDeDatos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class UI {

    public void menuPrincipal(Liga liga, GestorDeDatos gestor) {
        String[] opts = {"Ver Liga","Ver Clubes","Ver Jugadores","Ver Partidos","Salir"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(
                null, "Menú Principal","TP Fútbol",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opts, opts[0]
            );
            if (sel == 4 || sel == JOptionPane.CLOSED_OPTION) break;
            switch (sel) {
                case 0 -> menuLiga(liga, gestor);
                case 1 -> menuClubes(liga, gestor);
                case 2 -> menuVerJugadores(liga.getJugadores(), gestor);
                case 3 -> menuPartidos(liga, gestor);
            }
        }
    }

    private void menuLiga(Liga liga, GestorDeDatos gestor) {
        String[] ops = {"Tabla de Posiciones","Ver Partidos","Volver"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(
                null, "Ver Liga","Liga",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, ops, ops[0]
            );
            if (sel == 2 || sel == JOptionPane.CLOSED_OPTION) break;
            if (sel == 0) {
                mostrarTablaPosiciones(liga, gestor);
            } else {
                menuPartidos(liga, gestor);
            }
        }
    }

    private void menuClubes(Liga liga, GestorDeDatos gestor) {
        String[] ops = {"Listar Clubes","Ordenar Clubes","Comparar Clubes","Seleccionar Club","Volver"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(
                null, "Ver Clubes","Clubes",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, ops, ops[0]
            );
            if (sel == 4 || sel == JOptionPane.CLOSED_OPTION) break;
            switch (sel) {
                case 0 -> {
                    listarClubes(liga);
                    listarPartidos(liga.getPartidos());
                }
                case 1 -> ordenarClubes(liga, gestor);
                case 2 -> compararClubes(liga);
                case 3 -> seleccionarClub(liga, gestor);
            }
        }
    }

    private void menuVerJugadores(List<Jugador> lista, GestorDeDatos gestor) {
        String[] ops = {"Listar Jugadores","Ordenar Jugadores","Comparar Jugadores","Seleccionar Jugador","Volver"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(
                null, "Ver Jugadores","Jugadores",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, ops, ops[0]
            );
            if (sel == 4 || sel == JOptionPane.CLOSED_OPTION) break;
            switch (sel) {
                case 0 -> listarJugadores(lista);
                case 1 -> ordenarJugadores(lista, gestor);
                case 2 -> compararJugadores(lista);
                case 3 -> seleccionarJugador(lista);
            }
        }
    }
    private void seleccionarPartido(List<Partido> partidos) {
        Partido p = seleccionarDeLista(
            partidos,
            pt -> String.format("%s: %s %s-%s %s",
                 pt.getFecha(),
                 pt.getEquipoLocal(), pt.getGolesLocal(),
                 pt.getGolesVisitante(), pt.getEquipoVisitante()
            ),
            "Elige Partido"
        );
        if (p != null) {
            listarEstadisticasPartido(p);
        }
    }
    private void listarEstadisticasPartido(Partido p) {
        // Dos columnas: estadística → valor
        String[] cols = {"Estadística", "Valor"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);

        // Recorremos sólo el Map de extras
        for (Map.Entry<String, Double> e : p.getEstadisticas().entrySet()) {
            m.addRow(new Object[]{ e.getKey(), e.getValue() });
        }

        JTable table = new JTable(m);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(400, 200));
        JOptionPane.showMessageDialog(
            null, scroll,
            "Extras de " + p.getFecha() + " (" +
                p.getEquipoLocal() + " vs " + p.getEquipoVisitante() + ")",
            JOptionPane.PLAIN_MESSAGE
        );
    }
    private void menuPartidos(Liga liga, GestorDeDatos gestor) {
        String[] ops = {"Listar Todos","Filtrar por Club","Seleccionar Partido","Volver"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(
                null, "Ver Partidos", "Partidos",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, ops, ops[0]
            );
            if (sel == 3 || sel == JOptionPane.CLOSED_OPTION) break;

            switch (sel) {
                case 0 -> {
                    // General
                    listarPartidos(liga.getPartidos());
                }
                case 1 -> {
                    // Filtrado
                    Club c = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Selecciona Club");
                    if (c != null) {
                        List<Partido> fil = gestor.filtrarPartidos(liga.getPartidos(), c.getNombre());
                        listarPartidos(fil);
                        // Y aquí permitimos ver extras de cualquiera de esos filtrados:
                        seleccionarPartido(fil);
                    }
                }
                case 2 -> {
                    // Selección directa de cualquier partido
                    seleccionarPartido(liga.getPartidos());
                }
            }
        }
    }

    // — Tabla de posiciones con ValorPlantel incluido
    private void mostrarTablaPosiciones(Liga liga, GestorDeDatos gestor) {
        List<Club> tabla = gestor.getTablaPosiciones(liga);
        String[] cols = {"Club","Pts","PG","PP","PE","ValorPlantel"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        for (Club c : tabla) {
            m.addRow(new Object[]{
                c.getNombre(), c.getPuntos(),
                c.getPg(), c.getPp(), c.getPe(),
                c.getValorPlantel()
            });
        }
        showTableDialog(new JTable(m), "Tabla de Posiciones");
    }

    private void listarClubes(Liga liga) {
        String[] cols = {"Club","DT (edad)","ValorPlantel"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        for (Club c : liga.getClubes()) {
            m.addRow(new Object[]{
                c.getNombre(),
                c.getDt().getNombre() + " (" + c.getDt().getEdad().intValue() + " años)",
                c.getValorPlantel()
            });
        }
        showTableDialog(new JTable(m), "Clubes");
    }

    private void ordenarClubes(Liga liga, GestorDeDatos gestor) {
        // 1) Recalcular clasificación con todos los partidos
        gestor.calcularClasificacion(liga);

        // 2) Preguntar criterio de orden
        String[] opts = {"PG","PP","Pts","ValorPlantel"};
        int sel = JOptionPane.showOptionDialog(
            null,
            "Criterio de orden",
            "Ordenar Clubes",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            opts,
            opts[0]
        );
        if (sel < 0) return;

        // 3) ¿Descendente?
        boolean desc = JOptionPane.showConfirmDialog(
            null,
            "Orden descendente?",
            "Dirección",
            JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;

        // 4) Obtener lista ordenada
        List<Club> orden = gestor.ordenarClubes(
            liga.getClubes(),
            sel + 1,   // criterios: 1=PG,2=PP,3=Pts,4=ValorPlantel
            desc
        );

        // 5) Mostrar tabla con ValorPlantel
        String[] cols = {"Club","Pts","PG","PP","PE","ValorPlantel"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        for (Club c : orden) {
            m.addRow(new Object[]{
                c.getNombre(),
                c.getPuntos(),
                c.getPg(),
                c.getPp(),
                c.getPe(),
                c.getValorPlantel()
            });
        }
        showTableDialog(new JTable(m), "Clubes Ordenados");
    }

    private void compararClubes(Liga liga) {
        Club c1 = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Club 1");
        Club c2 = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Club 2");
        if (c1==null || c2==null || c1==c2) return;
        String[] cols = {"Club","Pts","PG","PP","PE","ValorPlantel"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        for (Club c : List.of(c1,c2)) {
            m.addRow(new Object[]{
                c.getNombre(), c.getPuntos(),
                c.getPg(), c.getPp(), c.getPe(),
                c.getValorPlantel()
            });
        }
        showTableDialog(new JTable(m), "Comparación Clubes");
    }

    private void seleccionarClub(Liga liga, GestorDeDatos gestor) {
        Club c = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Elige Club");
        if (c==null) return;
        String[] ops = {"Ver DT","Ver Estadísticas","Ver Jugadores","Volver"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(
                null, "Club "+c.getNombre(),"Opciones Club",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, ops, ops[0]
            );
            if (sel<0 || sel==3) break;
            switch (sel) {
                case 0 -> JOptionPane.showMessageDialog(
                    null,
                    "DT: "+c.getDt().getNombre()+" ("+c.getDt().getEdad().intValue()+" años)",
                    "Director Técnico", JOptionPane.INFORMATION_MESSAGE
                );
                case 1 -> {
                    gestor.calcularClasificacion(liga);
                    listarEstadisticasClub(c);
                }
                case 2 -> menuVerJugadores(
                    gestor.filtrarJugadores(liga.getJugadores(), c.getNombre()),
                    gestor
                );
            }
        }
    }

    private void listarEstadisticasClub(Club c) {
        String[] cols = {"PG","PP","PE","Pts","ValorPlantel"};
        DefaultTableModel m = new DefaultTableModel(cols,0);
        m.addRow(new Object[]{
            c.getPg(), c.getPp(), c.getPe(),
            c.getPuntos(), c.getValorPlantel()
        });
        showTableDialog(new JTable(m), "Estadísticas Club");
    }

    private void listarPartidos(List<Partido> ps) {
        String[] cols = {"Fecha","Local","GL","GV","Visitante"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        for (Partido p : ps) {
            m.addRow(new Object[]{
                p.getFecha(),
                p.getEquipoLocal(),
                p.getGolesLocal(),
                p.getGolesVisitante(),
                p.getEquipoVisitante()
            });
        }
        showTableDialog(new JTable(m), "Partidos");
    }

    private void listarJugadores(List<Jugador> js) {
        String[] cols = {"Jugador","Goles","Asis","Vallas","Valor"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        for (Jugador j : js) {
            m.addRow(new Object[]{
                j.getNombre(),
                j.getGoles(),
                j.getAsistencias(),
                j.getVallas(),
                j.getValorMercado()
            });
        }
        showTableDialog(new JTable(m), "Jugadores");
    }

    private void ordenarJugadores(List<Jugador> lista, GestorDeDatos gestor) {
        String[] opts = {"Goles","Asistencias","Vallas","ValorMercado"};
        int sel = JOptionPane.showOptionDialog(
            null, "Criterio de orden","Ordenar Jugadores",
            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
            null, opts, opts[0]
        );
        if (sel<0) return;
        boolean desc = JOptionPane.showConfirmDialog(
            null, "Orden descendente?","Dirección", JOptionPane.YES_NO_OPTION
        )==JOptionPane.YES_OPTION;
        List<Jugador> orden = gestor.ordenarJugadores(lista, sel+1, desc);
        listarJugadores(orden);
    }

    private void compararJugadores(List<Jugador> lista) {
        Jugador j1 = seleccionarDeLista(lista, Jugador::getNombre, "Jugador 1");
        Jugador j2 = seleccionarDeLista(lista, Jugador::getNombre, "Jugador 2");
        if (j1==null||j2==null||j1==j2) return;
        listarJugadores(List.of(j1,j2));
    }

    private void seleccionarJugador(List<Jugador> lista) {
        while (true) {
            Jugador j = seleccionarDeLista(lista, Jugador::getNombre, "Elige Jugador");
            if (j==null) break;
            String[] ops = {"Ver Estadísticas","Seleccionar Otro","Volver"};
            int sel = JOptionPane.showOptionDialog(
                null, "Jugador "+j.getNombre(),"Opciones Jugador",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, ops, ops[0]
            );
            if (sel<0||sel==2) break;
            if (sel==0) listarEstadisticasJugador(j);
        }
    }

    private void listarEstadisticasJugador(Jugador j) {
        // Definimos dos columnas: Nombre de la estadística y Valor
        String[] cols = {"Estadística", "Valor"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);

        // 1) Campos fijos
        m.addRow(new Object[]{"Goles",          j.getGoles()});
        m.addRow(new Object[]{"Asistencias",    j.getAsistencias()});
        m.addRow(new Object[]{"Vallas",         j.getVallas()});
        m.addRow(new Object[]{"Valor Mercado",  j.getValorMercado()});

        // 2) Estadísticas extra
        for (Map.Entry<String, Double> e : j.getEstadisticas().entrySet()) {
            m.addRow(new Object[]{ e.getKey(), e.getValue() });
        }

        // 3) Mostramos la tabla
        JTable table = new JTable(m);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(400, 200));
        JOptionPane.showMessageDialog(
            null, scroll, "Estadísticas de " + j.getNombre(),
            JOptionPane.PLAIN_MESSAGE
        );
    }

    // — utilidades comunes

    private <T> T seleccionarDeLista(List<T> items, Function<T,String> labeler, String title) {
        DefaultListModel<String> model = new DefaultListModel<>();
        items.forEach(i -> model.addElement(labeler.apply(i)));
        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setPreferredSize(new Dimension(250,200));
        int ok = JOptionPane.showConfirmDialog(null, scroll, title,
                                               JOptionPane.OK_CANCEL_OPTION);
        if (ok==JOptionPane.OK_OPTION && list.getSelectedIndex()>=0)
            return items.get(list.getSelectedIndex());
        return null;
    }

    private void showTableDialog(JTable table, String title) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500,200));
        JOptionPane.showMessageDialog(null, scroll, title, JOptionPane.PLAIN_MESSAGE);
    }
}
