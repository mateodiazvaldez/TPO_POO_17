package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import clases.*;
import datos.GestorDeDatos;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class UI {

    public void menuPrincipal(Liga liga, GestorDeDatos gestorDeDatos) {
        String[] opciones = {"Ver Liga", "Ver Clubes", "Ver Jugadores", "Salir"};
        while (true) {
            int seleccion = JOptionPane.showOptionDialog(
                null, "Menú Principal", "Sistema de Estadisticas",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opciones, opciones[0]
            );
            if (seleccion == 3 || seleccion == JOptionPane.CLOSED_OPTION) break;
            switch (seleccion) {
                case 0 -> menuLiga(liga, gestorDeDatos);
                case 1 -> menuClubes(liga, gestorDeDatos);
                case 2 -> menuVerJugadores(liga.getJugadores(), gestorDeDatos);
            }
        }
    }

    private void menuLiga(Liga liga, GestorDeDatos gestorDeDatos) {
        String[] opciones = {"Tabla de Posiciones", "Ver Partidos", "Volver"};
        while (true) {
            int seleccion = JOptionPane.showOptionDialog(
                null, "Ver Liga", "Liga",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opciones, opciones[0]
            );
            if (seleccion == 2 || seleccion == JOptionPane.CLOSED_OPTION) break;
            if (seleccion == 0) {
                mostrarTablaPosiciones(liga, gestorDeDatos);
            } else {
                menuPartidos(liga, gestorDeDatos);
            }
        }
    }

    private void menuClubes(Liga liga, GestorDeDatos gestorDeDatos) {
        String[] opciones = {"Listar Clubes", "Ordenar Clubes", "Comparar Clubes", "Seleccionar Club", "Volver"};
        while (true) {
            int seleccion = JOptionPane.showOptionDialog(
                null, "Ver Clubes", "Clubes",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opciones, opciones[0]
            );
            if (seleccion == 4 || seleccion == JOptionPane.CLOSED_OPTION) break;
            switch (seleccion) {
                case 0 -> {
                    listarClubes(liga);
                    listarPartidos(liga.getPartidos());
                }
                case 1 -> ordenarClubes(liga, gestorDeDatos);
                case 2 -> compararClubes(liga);
                case 3 -> seleccionarClub(liga, gestorDeDatos);
            }
        }
    }

    private void menuVerJugadores(List<Jugador> listaJugadores, GestorDeDatos gestorDeDatos) {
        String[] opciones = {"Listar Jugadores", "Ordenar Jugadores", "Comparar Jugadores", "Seleccionar Jugador", "Volver"};
        while (true) {
            int seleccion = JOptionPane.showOptionDialog(
                null, "Ver Jugadores", "Jugadores",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opciones, opciones[0]
            );
            if (seleccion == 4 || seleccion == JOptionPane.CLOSED_OPTION) break;
            switch (seleccion) {
                case 0 -> listarJugadores(listaJugadores);
                case 1 -> ordenarJugadores(listaJugadores, gestorDeDatos);
                case 2 -> compararJugadores(listaJugadores);
                case 3 -> seleccionarJugador(listaJugadores);
            }
        }
    }

    private void seleccionarPartido(List<Partido> listaPartidos) {
        Partido partidoSeleccionado = seleccionarDeLista(
            listaPartidos,
            partido -> String.format("%s: %s %s-%s %s",
                 partido.getFecha(),
                 partido.getEquipoLocal(), partido.getGolesLocal(),
                 partido.getGolesVisitante(), partido.getEquipoVisitante()
            ),
            "Elige Partido"
        );
        if (partidoSeleccionado != null) {
            listarEstadisticasPartido(partidoSeleccionado);
        }
    }

    private void listarEstadisticasPartido(Partido partido) {
        String[] columnas = {"Estadística", "Valor"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);

        for (Map.Entry<String, Double> entrada : partido.getEstadisticas().entrySet()) {
            modeloTabla.addRow(new Object[]{ entrada.getKey(), entrada.getValue() });
        }

        JTable tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(400, 200));
        JOptionPane.showMessageDialog(
            null, scroll,
            "Extras de " + partido.getFecha() + " (" +
                partido.getEquipoLocal() + " vs " + partido.getEquipoVisitante() + ")",
            JOptionPane.PLAIN_MESSAGE
        );
    }

    private void menuPartidos(Liga liga, GestorDeDatos gestorDeDatos) {
        String[] opciones = {"Listar Todos", "Filtrar por Club", "Seleccionar Partido", "Volver"};
        while (true) {
            int seleccion = JOptionPane.showOptionDialog(
                null, "Ver Partidos", "Partidos",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opciones, opciones[0]
            );
            if (seleccion == 3 || seleccion == JOptionPane.CLOSED_OPTION) break;

            switch (seleccion) {
                case 0 -> {
                    listarPartidos(liga.getPartidos());
                }
                case 1 -> {
                    Club clubSeleccionado = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Selecciona Club");
                    if (clubSeleccionado != null) {
                        List<Partido> partidosFiltrados = gestorDeDatos.filtrarPartidos(liga.getPartidos(), clubSeleccionado.getNombre());
                        listarPartidos(partidosFiltrados);
                        seleccionarPartido(partidosFiltrados);
                    }
                }
                case 2 -> seleccionarPartido(liga.getPartidos());
            }
        }
    }

    private void mostrarTablaPosiciones(Liga liga, GestorDeDatos gestorDeDatos) {
        List<Club> tablaPosiciones = gestorDeDatos.getTablaPosiciones(liga);
        String[] columnas = {"Club", "Pts", "PG", "PP", "PE", "ValorPlantel"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        for (Club club : tablaPosiciones) {
            modeloTabla.addRow(new Object[]{
                club.getNombre(), club.getPuntos(),
                club.getPg(), club.getPp(), club.getPe(),
                club.getValorPlantel()
            });
        }
        mostrarDialogoTabla(new JTable(modeloTabla), "Tabla de Posiciones");
    }

    private void listarClubes(Liga liga) {
        String[] columnas = {"Club", "DT (edad)", "ValorPlantel"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        for (Club club : liga.getClubes()) {
            modeloTabla.addRow(new Object[]{
                club.getNombre(),
                club.getDt().getNombre() + " (" + club.getDt().getEdad().intValue() + " años)",
                club.getValorPlantel()
            });
        }
        mostrarDialogoTabla(new JTable(modeloTabla), "Clubes");
    }

    private void ordenarClubes(Liga liga, GestorDeDatos gestorDeDatos) {
        gestorDeDatos.calcularClasificacion(liga);

        String[] opciones = {"PG", "PP", "Pts", "ValorPlantel"};
        int seleccion = JOptionPane.showOptionDialog(
            null,
            "Criterio de orden",
            "Ordenar Clubes",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
        if (seleccion < 0) return;

        boolean descendente = JOptionPane.showConfirmDialog(
            null,
            "Orden descendente?",
            "Dirección",
            JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;

        List<Club> clubesOrdenados = gestorDeDatos.ordenarClubes(
            liga.getClubes(),
            seleccion + 1,
            descendente
        );

        String[] columnas = {"Club", "Pts", "PG", "PP", "PE", "ValorPlantel"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        for (Club club : clubesOrdenados) {
            modeloTabla.addRow(new Object[]{
                club.getNombre(),
                club.getPuntos(),
                club.getPg(),
                club.getPp(),
                club.getPe(),
                club.getValorPlantel()
            });
        }
        mostrarDialogoTabla(new JTable(modeloTabla), "Clubes Ordenados");
    }

    private void compararClubes(Liga liga) {
        Club club1 = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Club 1");
        Club club2 = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Club 2");
        if (club1 == null || club2 == null || club1 == club2) return;
        String[] columnas = {"Club", "Pts", "PG", "PP", "PE", "ValorPlantel"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        for (Club club : List.of(club1, club2)) {
            modeloTabla.addRow(new Object[]{
                club.getNombre(), club.getPuntos(),
                club.getPg(), club.getPp(), club.getPe(),
                club.getValorPlantel()
            });
        }
        mostrarDialogoTabla(new JTable(modeloTabla), "Comparación Clubes");
    }

    private void seleccionarClub(Liga liga, GestorDeDatos gestorDeDatos) {
        Club clubSeleccionado = seleccionarDeLista(liga.getClubes(), Club::getNombre, "Elige Club");
        if (clubSeleccionado == null) return;
        String[] opciones = {"Ver DT", "Ver Estadísticas", "Ver Jugadores", "Volver"};
        while (true) {
            int seleccion = JOptionPane.showOptionDialog(
                null, "Club " + clubSeleccionado.getNombre(), "Opciones Club",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opciones, opciones[0]
            );
            if (seleccion < 0 || seleccion == 3) break;
            switch (seleccion) {
                case 0 -> JOptionPane.showMessageDialog(
                    null,
                    "DT: " + clubSeleccionado.getDt().getNombre() + " (" + clubSeleccionado.getDt().getEdad().intValue() + " años)",
                    "Director Técnico", JOptionPane.INFORMATION_MESSAGE
                );
                case 1 -> {
                    gestorDeDatos.calcularClasificacion(liga);
                    listarEstadisticasClub(clubSeleccionado);
                }
                case 2 -> menuVerJugadores(
                    gestorDeDatos.filtrarJugadores(liga.getJugadores(), clubSeleccionado.getNombre()),
                    gestorDeDatos
                );
            }
        }
    }

    private void listarEstadisticasClub(Club club) {
        String[] columnas = {"PG", "PP", "PE", "Pts", "ValorPlantel"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        modeloTabla.addRow(new Object[]{
            club.getPg(), club.getPp(), club.getPe(),
            club.getPuntos(), club.getValorPlantel()
        });
        mostrarDialogoTabla(new JTable(modeloTabla), "Estadísticas Club");
    }

    private void listarPartidos(List<Partido> listaPartidos) {
        String[] columnas = {"Fecha", "Local", "GL", "GV", "Visitante"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        for (Partido partido : listaPartidos) {
            modeloTabla.addRow(new Object[]{
                partido.getFecha(),
                partido.getEquipoLocal(),
                partido.getGolesLocal(),
                partido.getGolesVisitante(),
                partido.getEquipoVisitante()
            });
        }
        mostrarDialogoTabla(new JTable(modeloTabla), "Partidos");
    }

    private void listarJugadores(List<Jugador> listaJugadores) {
        String[] columnas = {"Jugador", "Goles", "Asis", "Vallas", "Valor"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        for (Jugador jugador : listaJugadores) {
            modeloTabla.addRow(new Object[]{
                jugador.getNombre(),
                jugador.getGoles(),
                jugador.getAsistencias(),
                jugador.getVallas(),
                jugador.getValorMercado()
            });
        }
        mostrarDialogoTabla(new JTable(modeloTabla), "Jugadores");
    }

    private void ordenarJugadores(List<Jugador> listaJugadores, GestorDeDatos gestorDeDatos) {
        String[] opciones = {"Goles", "Asistencias", "Vallas", "ValorMercado"};
        int seleccion = JOptionPane.showOptionDialog(
            null, "Criterio de orden", "Ordenar Jugadores",
            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
            null, opciones, opciones[0]
        );
        if (seleccion < 0) return;
        boolean descendente = JOptionPane.showConfirmDialog(
            null, "Orden descendente?", "Dirección", JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
        List<Jugador> jugadoresOrdenados = gestorDeDatos.ordenarJugadores(listaJugadores, seleccion + 1, descendente);
        listarJugadores(jugadoresOrdenados);
    }

    private void compararJugadores(List<Jugador> listaJugadores) {
        Jugador jugador1 = seleccionarDeLista(listaJugadores, Jugador::getNombre, "Jugador 1");
        Jugador jugador2 = seleccionarDeLista(listaJugadores, Jugador::getNombre, "Jugador 2");
        if (jugador1 == null || jugador2 == null || jugador1 == jugador2) return;
        listarJugadores(List.of(jugador1, jugador2));
    }

    private void seleccionarJugador(List<Jugador> listaJugadores) {
        while (true) {
            Jugador jugadorSeleccionado = seleccionarDeLista(listaJugadores, Jugador::getNombre, "Elige Jugador");
            if (jugadorSeleccionado == null) break;
            String[] opciones = {"Ver Estadísticas", "Seleccionar Otro", "Volver"};
            int seleccion = JOptionPane.showOptionDialog(
                null, "Jugador " + jugadorSeleccionado.getNombre(), "Opciones Jugador",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opciones, opciones[0]
            );
            if (seleccion < 0 || seleccion == 2) break;
            if (seleccion == 0) listarEstadisticasJugador(jugadorSeleccionado);
        }
    }

    private void listarEstadisticasJugador(Jugador jugador) {
        String[] columnas = {"Estadística", "Valor"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);

        modeloTabla.addRow(new Object[]{"Goles", jugador.getGoles()});
        modeloTabla.addRow(new Object[]{"Asistencias", jugador.getAsistencias()});
        modeloTabla.addRow(new Object[]{"Vallas", jugador.getVallas()});
        modeloTabla.addRow(new Object[]{"Valor Mercado", jugador.getValorMercado()});

        for (Map.Entry<String, Double> entrada : jugador.getEstadisticas().entrySet()) {
            modeloTabla.addRow(new Object[]{ entrada.getKey(), entrada.getValue() });
        }

        JTable tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(400, 200));
        JOptionPane.showMessageDialog(
            null, scroll, "Estadísticas de " + jugador.getNombre(),
            JOptionPane.PLAIN_MESSAGE
        );
    }

    // — utilidades comunes

    private <T> T seleccionarDeLista(List<T> elementos, Function<T, String> formateador, String titulo) {
        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        elementos.forEach(e -> modeloLista.addElement(formateador.apply(e)));
        JList<String> lista = new JList<>(modeloLista);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setPreferredSize(new Dimension(250, 200));
        int ok = JOptionPane.showConfirmDialog(null, scroll, titulo,
                                               JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION && lista.getSelectedIndex() >= 0)
            return elementos.get(lista.getSelectedIndex());
        return null;
    }

    private void mostrarDialogoTabla(JTable tabla, String titulo) {
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(500, 200));
        JOptionPane.showMessageDialog(null, scroll, titulo, JOptionPane.PLAIN_MESSAGE);
    }
}
