package com.AlkemyPocket.repository;

import com.AlkemyPocket.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, String> {
    Optional<Cuenta> findByCvu(String cvu);
    Optional<Cuenta> findByAlias(String alias);
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
}
