package com.AlkemyPocket.controllers;

import com.AlkemyPocket.dto.ActualizarUsuarioDTO;
import com.AlkemyPocket.dto.LoginRespuestaDTO;
import com.AlkemyPocket.dto.LoginSolicitudDTO;
import com.AlkemyPocket.model.Usuario; // Importa la clase usuario porque manipula objetos "Usuario".
import com.AlkemyPocket.services.UsuarioService; // Importa la clase UsuarioService porque llama a sus métodos y esta importación permite usarlo como tipo de dato. (Ver despues la inyección).
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired; // Permite inyectar automáticamente dependencias (en nuestro caso creamos instancias de la clase UsuarioServcie).
import org.springframework.http.ResponseEntity; // Clase de Spring que representa toda la respuesta HTTP. Permite responder con códigos de respuesta y demas.
import org.springframework.http.HttpStatus; // Permite reflejar de manera mas clara el código de respuesta de ResponseEntity.
import org.springframework.web.bind.annotation.*; // Trae las anotaciones de Spring MVC necesarias para decorar la clase.

import java.util.List; // Importa tipo de dato que va a ir en las respuestas.

@Tag(name = "Usuarios")
@RestController // Convertimos la clase en un controlador REST, significa que los métodos devuelven directamente JSON (no vistas HTML).
@RequestMapping("/AlkemyPocket/usuarios") // Define la base para todas las rutas => Tenemos que ver de cambiarlo a AlkemyPocket/usuarios o similar.
public class UsuarioController {


    @Autowired //Inyecta automáticamente una instancia de UsuarioService para usar sus métodos. Para ésto hace falta tanto traer la anotacion como el tipo de dato, como vimos arriba.
    private UsuarioService usuarioService;

    // GET CON MANEJO DE EXCEPCIÓN HANDLER.

    @Operation(summary = "Obtener todos los usuarios registrados en la API.")
    @GetMapping // Declara que si el metodo es GET a la base de ruta a secas se ejecuta el siguiente metodo. NOTA IMPORTANTE: Debe coincidir la firma del metodo controlador con la firma del metodo del servicio que este llama.
    public ResponseEntity<List<Usuario>> obtenerTodos() {
            List<Usuario> usuarios = usuarioService.obtenerTodos();
            return ResponseEntity.ok(usuarios);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> manejarRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // GET CON MANEJO DE EXCEPCIÓN TRY/CATCH.

    @Operation(summary = "Obtener usuario por ID")
    @GetMapping("/{id}") // Declara que si el metodo es GET a la base de ruta + ID se ejecuta el siguiente metodo.
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            Usuario usuario = usuarioService.obtenerPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encuentra el usuario con el ID " + id);
        }
    }

    // POST DE CREACIÓN CON MANEJO DE TRY CATCH Y VALIDACIONES EN EL SERVICIO.

    @Operation(summary = "Crear un usuario nuevo y automaticamente se crea una cuenta.")
    @PostMapping // Declara que si el metodo es POST a la base de ruta a secas se ejecuta el siguiente metodo.
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) { // La notación significa que recibe un objeto tipo Usuario en el cuerpo de la petición y lo convierte en un JSON.
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(usuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // PUT DE ACTUALIZACION CON DTO Y MANEJO DE ERRORES POSIBLES.

    @Operation(summary = "Actualizar datos de un usuario")
    @PutMapping("/{id}") // Declara que si el metodo es PUT a la base de ruta + ID se ejecuta el siguiente metodo.
    public ResponseEntity<?> actualizarUsuario(@PathVariable Integer id, @RequestBody ActualizarUsuarioDTO usuario) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioService.actualizarUsuario(id, usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el usuario");
        }
    }

    // DELETE CON MANEJO DE EXCEPCIONES A TRAVES DE HANDLERS.

    @Operation(summary = "Eliminar un usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> manejarNoEncontrado(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarErrores(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error: " + ex.getMessage());
    }

    @Operation(summary = "Logueo del Usuario con su mail y contraseña.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginSolicitudDTO solicitaLogin) {
        try {
            LoginRespuestaDTO response = usuarioService.login(solicitaLogin.getEmail(), solicitaLogin.getContrasenia());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
