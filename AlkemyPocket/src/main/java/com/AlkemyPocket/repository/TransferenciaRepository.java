package com.AlkemyPocket.repository;

import com.AlkemyPocket.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Integer> {
    List<Transferencia> findByCuentaOrigen_NumeroCuentaOrCuentaDestino_NumeroCuenta(String cuentaOrigen, String cuentaDestino);
}
