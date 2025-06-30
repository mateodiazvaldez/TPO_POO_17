
package clases;

import java.util.HashMap;
import java.util.Map;

public class Estadisticas {
    private Map<String, Double> estadisticas;

    public Estadisticas(Map<String, Double> estadisticas) {
        this.estadisticas = new HashMap<>(estadisticas);
    }

    public double getValorEstadistica(String clave) {
        return estadisticas.getOrDefault(clave, 0.0);
    }

    public Map<String, Double> getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(Map<String, Double> estadisticas) {
        this.estadisticas = new HashMap<>(estadisticas);
    }

    public void agregarEstadistica(String clave, double valor) {
        estadisticas.put(clave, valor);
    }
}
