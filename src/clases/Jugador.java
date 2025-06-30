
package clases;

import java.util.HashMap;
import java.util.Map;

public class Jugador extends Persona implements ConEstadisticas {
    private double goles;
    private double asistencias;
    private double vallas;
    private int valorMercado;
    private Estadisticas estadisticas;

    public Jugador(String nombre, int edad, String club,
                   double goles, double asistencias, double vallas,
                   int valorMercado) {
        super(nombre, (double)edad, club);
        this.goles = goles;
        this.asistencias = asistencias;
        this.vallas = vallas;
        this.valorMercado = valorMercado;
        this.estadisticas = new Estadisticas(new HashMap<>());
    }

    public double getGoles() { return goles; }
    public void setGoles(double goles) { this.goles = goles; }

    public double getAsistencias() { return asistencias; }
    public void setAsistencias(double asistencias) { this.asistencias = asistencias; }

    public double getVallas() { return vallas; }
    public void setVallas(double vallas) { this.vallas = vallas; }

    public int getValorMercado() { return valorMercado; }
    public void setValorMercado(int valorMercado) { this.valorMercado = valorMercado; }

    @Override
    public double getValorEstadistica(String clave) {
        return estadisticas.getValorEstadistica(clave);
    }

    @Override
    public Map<String, Double> getEstadisticas() {
        return estadisticas.getEstadisticas();
    }

    @Override
    public void setEstadisticas(Map<String, Double> estadisticas) {
        this.estadisticas.setEstadisticas(estadisticas);
    }

    public void agregarEstadistica(String clave, double valor) {
        estadisticas.agregarEstadistica(clave, valor);
    }
}
