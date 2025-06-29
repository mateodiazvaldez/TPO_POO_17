// src/model/Partido.java
package model;

public class Partido {
    private String fecha;
    private String equipoLocal;
    private String equipoVisitante;
    private double golesLocal;
    private double golesVisitante;
    private Estadisticas estadisticas;

    public Partido(String fecha, String equipoLocal, String equipoVisitante,
                   double golesLocal, double golesVisitante) {
        this.fecha = fecha;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.estadisticas = new Estadisticas();
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(String equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public String getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(String equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public double getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(double golesLocal) {
        this.golesLocal = golesLocal;
    }

    public double getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(double golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public Estadisticas getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(Estadisticas estadisticas) {
        this.estadisticas = estadisticas;
    }

    public void agregarEstadistica(String clave, double valor) {
        this.estadisticas.agregarEstadistica(clave, valor);
    }
}
