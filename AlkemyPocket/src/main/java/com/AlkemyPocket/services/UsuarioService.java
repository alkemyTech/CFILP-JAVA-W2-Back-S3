package com.AlkemyPocket.services;

import com.AlkemyPocket.dto.ActualizarUsuarioDTO; // DATA TRANSFER OBJECT
import com.AlkemyPocket.model.RolUsuario;
import com.AlkemyPocket.model.Usuario;
import com.AlkemyPocket.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Con esto indicamos que este es el servicio.
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CuentaService cuentaService;

    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = usuarioRepository.findAll();
        if (lista.isEmpty()) {
            throw new RuntimeException("No hay nadie registrado en la aplicación Alkemy Pocket.");
        } else {
            return lista;
        }
    }

    public Usuario obtenerPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("error"));
    }

    public Usuario crearUsuario(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (usuario.getApellido() == null || usuario.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (usuario.getTelefono() == null || usuario.getTelefono().isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }
        if (usuario.getFechaCreacion() == null) {
            throw new IllegalArgumentException("La fecha de creación es obligatoria");
        }
        if (usuario.getContrasenia() == null || usuario.getContrasenia().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        cuentaService.crearCuenta(nuevoUsuario.getId(), null);
        return nuevoUsuario;

    }

    public Usuario actualizarUsuario(Integer id, ActualizarUsuarioDTO usuarioActualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            if (usuarioActualizado.getNombre() != null) usuario.setNombre(usuarioActualizado.getNombre());
            if (usuarioActualizado.getApellido() != null) usuario.setApellido(usuarioActualizado.getApellido());
            if (usuarioActualizado.getEmail() != null) usuario.setEmail(usuarioActualizado.getEmail());
            if (usuarioActualizado.getTelefono() != null) usuario.setTelefono(usuarioActualizado.getTelefono());
            if (usuarioActualizado.getContrasenia() != null) usuario.setContrasenia(usuarioActualizado.getContrasenia());
            if (usuarioActualizado.getRol() != null) usuario.setRol(usuarioActualizado.getRol());
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + id));
    }

    public void eliminarUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("El usuario con ID " + id + " no existe");
        }
        try {
            usuarioRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el usuario: " + e.getMessage());
        }
    }
}
