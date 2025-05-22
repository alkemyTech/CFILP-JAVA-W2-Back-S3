package com.AlkemyPocket.repository;

import com.AlkemyPocket.model.Cuenta;
import com.AlkemyPocket.model.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarjetaRepository extends JpaRepository<Tarjeta, String> {
    List<Tarjeta> findByCuentas(Cuenta cuenta);
}
