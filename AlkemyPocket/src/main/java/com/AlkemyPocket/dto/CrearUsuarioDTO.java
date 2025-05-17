package com.AlkemyPocket.dto;

import com.AlkemyPocket.model.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;

public class CrearUsuarioDTO {

    @Schema(example = "Ana")
    private String nombre;

    @Schema(example = "García")
    private String apellido;

    @Schema(example = "ana.garcia@example.com")
    private String email;

    @Schema(example = "+5491155443322")
    private String telefono;

    @Schema(example = "12345678")
    private String contrasenia;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
