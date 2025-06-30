
package clases;

import java.util.Map;

public interface ConEstadisticas {
    double getValorEstadistica(String clave);
    Map<String, Double> getEstadisticas();
    void setEstadisticas(Map<String, Double> estadisticas);
}
