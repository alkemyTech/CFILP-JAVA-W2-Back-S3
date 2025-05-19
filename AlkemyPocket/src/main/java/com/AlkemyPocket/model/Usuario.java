package com.AlkemyPocket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*; // Importa las anotaciones de JPA, para poder "decorar" la clase con @. Imagina que la clase es una caja grande con cosas dentro y con las anotaciones lo que hacemos es indicar como usarla, cómo usar cada cosa.
import java.time.LocalDateTime; // Importa la clase para manejar fecha y hora.

@Entity // Marca la clase como una ENTIDAD (una tabla en la base de datos).
@Table(name = "usuario") // Dice que la tabla de la base de datos se llama 'usuario'.
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario {

    @Id // Marca este campo como PRIMARY KEY.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Dice que el id se genera automáticamente (autoincremental).
    @Column(name = "id_usuario") // Asocia este campo a la columna 'id_usuario' de la tabla. Pareciera que si el nombre no lo determinamos arriba por defecto toma el nombre del atributo. Y creo que suele pasar cuando el nombre de la columna SQL tiene un guion bajo.
    private Integer id;

    @Column(nullable = false) // Columna 'nombre' que NO puede ser null.
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true) // Columna 'email' que NO puede ser null y debe ser único.
    private String email;

    private String telefono; // Columna 'telefono' (nullable por defecto = sí puede ser null)

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(nullable = false)
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rol = RolUsuario.cliente;

    // Siguen los constructores normales, getters y setters.

    public Usuario() {}


    public Usuario(String nombre, String apellido, String email, String telefono, LocalDateTime fechaCreacion, String contrasenia) {
        this(nombre, apellido, email, telefono, fechaCreacion, contrasenia, RolUsuario.cliente);
    }


    public Usuario(String nombre, String apellido, String email, String telefono, LocalDateTime fechaCreacion, String contrasenia, RolUsuario rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fechaCreacion = fechaCreacion;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; } // Recordar que Set contraseña deberia validar. Supongo que podriamos dejar la validacion de caracteres para el FRONT y la validación del contenido para el BACK.

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

}

