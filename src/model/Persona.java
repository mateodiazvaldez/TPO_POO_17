
package model;

public abstract class Persona {
    private String nombre;
    private Double edad;
    private String club;
    private String nacionalidad = "";

    public Persona(String nombre, Double edad, String club) {
        this.nombre = nombre;
        this.edad = edad;
        this.club = club;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getEdad() {
        return edad;
    }

    public void setEdad(Double edad) {
        this.edad = edad;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }
}
