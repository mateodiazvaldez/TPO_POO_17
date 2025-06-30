// src/app/Main.java
package app;

import model.*;
import service.GestorDeDatos;
import ui.UI;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        GestorDeDatos gestor = new GestorDeDatos();
        try {
            // 1) Cargar todas las fuentes de datos
            List<Jugador> jugadores = gestor.cargarJugadores("data/jugadores.csv");
            List<Club>    clubes    = gestor.cargarClubes("data/clubes.csv");
            List<Partido> partidos  = gestor.cargarPartidos("data/partidos.csv");

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
