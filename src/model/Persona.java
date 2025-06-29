// src/model/Persona.java
package model;

import java.util.Map;

public abstract class Persona implements ConEstadisticas {
    protected String nombre;
    protected double edad;
    protected String club;
    protected Estadisticas estadisticas;

    public Persona(String nombre, double edad, String club) {
        this.nombre = nombre;
        this.edad = edad;
        this.club = club;
        this.estadisticas = new Estadisticas();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getEdad() {
        return edad;
    }

    public void setEdad(double edad) {
        this.edad = edad;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    @Override
    public Map<String, Double> getEstadisticas() {
        return estadisticas.getEstadisticas();
    }

    @Override
    public void setEstadisticas(Map<String, Double> estadisticas) {
        this.estadisticas.setEstadisticas(estadisticas);
    }

    @Override
    public double getValorEstadistica(String clave) {
        return estadisticas.getValorEstadistica(clave);
    }
}
