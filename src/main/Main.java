// src/app/Main.java
package main;

import ui.UI;

import java.util.List;

import clases.*;
import datos.GestorDeDatos;

public class Main {
    public static void main(String[] args) {
        GestorDeDatos gestor = new GestorDeDatos();
        try {
            // 1) Cargar todas las fuentes de datos
            List<Jugador> jugadores = gestor.cargarJugadores("archivos/jugadores.csv");
            List<Club>    clubes    = gestor.cargarClubes("archivos/clubes.csv");
            List<Partido> partidos  = gestor.cargarPartidos("archivos/partidos.csv");

            // 2) Asignar jugadores a cada club y calcular valorPlantel
            for (Club c : clubes) {
                List<Jugador> subs = gestor.filtrarJugadores(jugadores, c.getNombre());
                c.setJugadores(subs);
                int suma = 0;
                for (Jugador j : subs) {
                    suma += j.getValorMercado();
                }
                c.setValorPlantel(suma);
            }

            // 3) Construir la liga con todos los datos
            Liga liga = new Liga("Primera División");
            liga.setJugadores(jugadores);
            liga.setClubes(clubes);
            liga.setPartidos(partidos);

            // 4) Arrancar UI
            new UI().menuPrincipal(liga, gestor);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
