package com.AlkemyPocket.repository;

import com.AlkemyPocket.dto.ContactosFrecuentesDTO;
import com.AlkemyPocket.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, String> {
    Optional<Cuenta> findByCvu(String cvu);
    Optional<Cuenta> findByAlias(String alias);
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);


    @Query(value = """
        SELECT c.alias, c.cvu, u.nombre, u.apellido
        FROM cuenta c
        INNER JOIN usuario u ON u.id_usuario = c.id_usuario
        WHERE c.numero_cuenta IN (
            SELECT t.cuenta_destino
            FROM cuenta c2
            INNER JOIN transferencia t ON t.cuenta_origen = c2.numero_cuenta
            WHERE c2.id_usuario = :idUsuario
        )
        LIMIT 20
        """, nativeQuery = true)
    List<ContactosFrecuentesDTO> consultarContactosFrecuentes(@Param("idUsuario") Integer id_usuario);
}
