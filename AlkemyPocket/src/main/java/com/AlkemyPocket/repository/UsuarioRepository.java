package com.AlkemyPocket.repository;

import com.AlkemyPocket.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // No hace falta escribir nada, JpaRepository ya trae CRUD listo
}

