// src/app/Main.java
package main;

import ui.UI;

import java.util.List;

import clases.*;
import datos.GestorDeDatos;

public class Main {
    public static void main(String[] args) {
        GestorDeDatos gestorDeDatos = new GestorDeDatos();

        try {
            // 1) Cargar datos desde archivos
            List<Jugador> jugadores = gestorDeDatos.cargarJugadores("archivos/jugadores.csv");
            List<Club> clubes = gestorDeDatos.cargarClubes("archivos/clubes.csv");
            List<Partido> partidos = gestorDeDatos.cargarPartidos("archivos/partidos.csv");

            // 2) Asignar jugadores a cada club y calcular valor total del plantel
            for (Club club : clubes) {
                List<Jugador> jugadoresDelClub = gestorDeDatos.filtrarJugadores(jugadores, club.getNombre());
                club.setJugadores(jugadoresDelClub);

                int valorPlantel = 0;
                for (Jugador jugador : jugadoresDelClub) {
                    valorPlantel += jugador.getValorMercado();
                }

                club.setValorPlantel(valorPlantel);
            }

            // 3) Construir la liga con los datos cargados
            Liga liga = new Liga("Primera División");
            liga.setJugadores(jugadores);
            liga.setClubes(clubes);
            liga.setPartidos(partidos);

            // 4) Iniciar la interfaz de usuario
            UI interfaz = new UI();
            interfaz.menuPrincipal(liga, gestorDeDatos);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
