// src/model/Estadisticas.java
package model;

import java.util.HashMap;
import java.util.Map;

public class Estadisticas implements ConEstadisticas {
    private Map<String, Double> estadisticas;

    public Estadisticas() {
        this.estadisticas = new HashMap<>();
    }

    public Estadisticas(Map<String, Double> estadisticas) {
        this.estadisticas = new HashMap<>(estadisticas);
    }

    @Override
    public double getValorEstadistica(String clave) {
        return estadisticas.getOrDefault(clave, 0.0);
    }

    @Override
    public Map<String, Double> getEstadisticas() {
        return estadisticas;
    }

    @Override
    public void setEstadisticas(Map<String, Double> estadisticas) {
        this.estadisticas = estadisticas;
    }

    public void agregarEstadistica(String clave, double valor) {
        estadisticas.put(clave, valor);
    }
}
