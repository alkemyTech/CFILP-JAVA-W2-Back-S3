package com.AlkemyPocket.repository;

import com.AlkemyPocket.model.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositoRepository extends JpaRepository<Deposito, Integer> {
    List<Deposito> findByCuentaDestino_NumeroCuenta(String numeroCuenta);
}
