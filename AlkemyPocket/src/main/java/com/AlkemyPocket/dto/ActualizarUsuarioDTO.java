package com.AlkemyPocket.dto;

import com.AlkemyPocket.model.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;

public class ActualizarUsuarioDTO {

    @Schema(example = "Juan")
    private String nombre;

    @Schema(example = "Pérez")
    private String apellido;

    @Schema(example = "juan.perez@example.com")
    private String email;

    @Schema(example = "+5491122334455")
    private String telefono;

    @Schema(example = "12345678")
    private String contrasenia;

    @Schema(example = "cliente")
    private RolUsuario rol;

    public ActualizarUsuarioDTO() {
    }

    public ActualizarUsuarioDTO(String nombre, String apellido, String email, String telefono, String contrasenia, RolUsuario rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

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

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

}
