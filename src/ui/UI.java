// src/ui/UI.java
package ui;

import model.*;
import service.GestorDeDatos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class UI extends JFrame {
    private GestorDeDatos gestor;
    private List<Jugador> jugadores;
    private List<Club> clubes;
    private List<Partido> partidos;
    private Liga liga;

    public UI() {
        super("Sistema de Estadísticas de Fútbol");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        gestor = new GestorDeDatos();
        try {
            jugadores = gestor.cargarJugadores("data/jugadores.csv");
            partidos  = gestor.cargarPartidos("data/partidos.csv");
            // supongo DT en un CSV o lo extraigo de clubes:
            // para simplificar, creemos un DT genérico
            clubes     = gestor.cargarClubes("data/clubes.csv", List.of());
            liga = new Liga("Primera División");
            liga.setJugadores(jugadores);
            liga.setClubes(clubes);
            liga.setPartidos(partidos);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar datos:\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        initComponents();
    }

    private void initComponents() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 2, 10, 10));

        JButton bJug = new JButton("Ver Jugadores");
        JButton bClub = new JButton("Ver Clubes");
        JButton bLiga = new JButton("Ver Liga");
        JButton bSalir = new JButton("Salir");

        bJug.addActionListener(e -> mostrarJugadores());
        bClub.addActionListener(e -> mostrarClubes());
        bLiga.addActionListener(e -> mostrarLiga());
        bSalir.addActionListener(e -> System.exit(0));

        p.add(bJug);
        p.add(bClub);
        p.add(bLiga);
        p.add(bSalir);

        getContentPane().add(p, BorderLayout.CENTER);
    }

    private void mostrarJugadores() {
        String[] nombres = jugadores.stream()
                                    .map(Jugador::getNombre)
                                    .toArray(String[]::new);
        String sel = (String) JOptionPane.showInputDialog(
            this, "Seleccione un jugador:", "Jugadores",
            JOptionPane.PLAIN_MESSAGE, null,
            nombres, nombres[0]);
        if (sel != null) {
            Jugador j = gestor.buscarJugador(jugadores, sel);
            StringBuilder sb = new StringBuilder();
            sb.append("Nombre: ").append(j.getNombre()).append("\n")
              .append("Club: ").append(j.getClub()).append("\n")
              .append("Edad: ").append(j.getEdad()).append("\n")
              .append("Goles: ").append(j.getGoles()).append("\n")
              .append("Asistencias: ").append(j.getAsistencias()).append("\n")
              .append("Vallas: ").append(j.getVallas()).append("\n")
              .append("Valor mercado: ").append(j.getValorMercado()).append("\n\n")
              .append("Otras estadísticas:\n");
            j.getEstadisticas().forEach((k,v) ->
                sb.append(" - ").append(k).append(": ").append(v).append("\n")
            );
            JOptionPane.showMessageDialog(this, sb.toString(),
                "Estadísticas de " + j.getNombre(), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void mostrarClubes() {
        String[] nombres = clubes.stream()
                                 .map(Club::getNombre)
                                 .toArray(String[]::new);
        String sel = (String) JOptionPane.showInputDialog(
            this, "Seleccione un club:", "Clubes",
            JOptionPane.PLAIN_MESSAGE, null,
            nombres, nombres[0]);
        if (sel != null) {
            Club c = gestor.buscarClub(clubes, sel);
            Object[] opciones = {"Ver Estadísticas", "Ver Jugadores"};
            int op = JOptionPane.showOptionDialog(this,
                "¿Qué desea ver de " + c.getNombre() + "?",
                "Club " + c.getNombre(),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);
            if (op == JOptionPane.YES_OPTION) {
                // estadísticas del club
                StringBuilder sb = new StringBuilder();
                sb.append("PJ: ").append(c.getPj()).append("\n")
                  .append("PG: ").append(c.getPg()).append("\n")
                  .append("PE: ").append(c.getPe()).append("\n")
                  .append("PP: ").append(c.getPp()).append("\n")
                  .append("GF: ").append(c.getGf()).append("\n")
                  .append("GC: ").append(c.getGc()).append("\n")
                  .append("Puntos: ").append(c.getPuntos()).append("\n");
                JOptionPane.showMessageDialog(this, sb.toString(),
                    "Estadísticas de " + c.getNombre(), JOptionPane.INFORMATION_MESSAGE);
            } else if (op == JOptionPane.NO_OPTION) {
                // jugadores del club
                List<Jugador> list = gestor.filtrarJugadores(jugadores, c.getNombre());
                String[] noms = list.stream()
                                    .map(Jugador::getNombre)
                                    .toArray(String[]::new);
                JOptionPane.showMessageDialog(this,
                    String.join("\n", noms),
                    "Jugadores de " + c.getNombre(),
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void mostrarLiga() {
        // tabla de posiciones
        List<Club> tabla = liga.getTablaPosiciones();
        StringBuilder sb = new StringBuilder("Posiciones:\n");
        int pos = 1;
        for (Club c : tabla) {
            sb.append(pos++).append(". ")
              .append(c.getNombre())
              .append(" - Pts: ").append(c.getPuntos())
              .append(" GF:").append(c.getGf())
              .append(" GC:").append(c.getGc())
              .append("\n");
        }
        // también ofrecer ver partidos
        int op = JOptionPane.showOptionDialog(this,
            sb.toString(),
            "Tabla de Posiciones",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"Ver partidos", "Cerrar"},
            "Cerrar");
        if (op == JOptionPane.YES_OPTION) {
            StringBuilder ps = new StringBuilder("Partidos:\n");
            for (Partido p : partidos) {
                ps.append(p.getFecha())
                  .append(" | ").append(p.getEquipoLocal())
                  .append(" ").append(p.getGolesLocal())
                  .append(" - ").append(p.getGolesVisitante())
                  .append(" ").append(p.getEquipoVisitante())
                  .append("\n");
            }
            JOptionPane.showMessageDialog(this,
                ps.toString(),
                "Partidos de la Liga",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
