package es.franciscorodalf.juegoahorcado.backend.model;

import java.util.Objects;

/**
 * Clase que representa la entidad Usuario en el sistema.
 * Contiene la información básica de un usuario como email, nombre, contraseña y nivel.
 */
public class UsuarioEntity {

    private String email;       
    private String nombre;      
    private String contrasenia;  
    private String nivel;        

    /**
     * Constructor por defecto
     */
    public UsuarioEntity() {}

    /**
     * Constructor con parámetros básicos sin nivel
     * @param email Email del usuario
     * @param nombre Nombre del usuario
     * @param contrasenia Contraseña del usuario
     */
    public UsuarioEntity(String email, String nombre, String contrasenia) {
        this.email = email;
        this.nombre = nombre;
        this.contrasenia = contrasenia;
    }

    /**
     * Constructor completo con todos los parámetros
     * @param email Email del usuario
     * @param nombre Nombre del usuario
     * @param contrasenia Contraseña del usuario
     * @param nivel Nivel actual del usuario
     */
    public UsuarioEntity(String email, String nombre, String contrasenia, String nivel) {
        this.email = email;
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.nivel = nivel;
    }

    // Getters y setters

    
    /**
     * Obtiene el email del usuario
     * @return Email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email del usuario
     * @param email Nuevo email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el nombre del usuario
     * @return Nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario
     * @param nombre Nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la contraseña del usuario
     * @return Contraseña del usuario
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * Establece la contraseña del usuario
     * @param contrasenia Nueva contraseña
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    /**
     * Obtiene el nivel actual del usuario
     * @return Nivel del usuario
     */
    public String getNivel() {
        return nivel;
    }

    /**
     * Establece el nivel del usuario
     * @param nivel Nuevo nivel
     */
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    /**
     * Compara si dos usuarios son iguales basándose en su email
     * @param o Objeto a comparar
     * @return true si son iguales, false si no
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioEntity)) return false;
        UsuarioEntity that = (UsuarioEntity) o;
        return Objects.equals(email, that.email);
    }

    /**
     * Genera un código hash basado en el email
     * @return Código hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    /**
     * Representación en texto del usuario
     * @return Cadena con nombre, email y nivel del usuario
     */
    @Override
    public String toString() {
        return nombre + " - " + email + " - " + nivel;
    }
}
