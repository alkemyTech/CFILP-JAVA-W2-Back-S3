package com.AlkemyPocket.repository;

import com.AlkemyPocket.model.Extraccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtraccionRepository extends JpaRepository<Extraccion, Integer> {
    List<Extraccion> findByCuentaOrigen_NumeroCuenta(String numeroCuenta);
}
