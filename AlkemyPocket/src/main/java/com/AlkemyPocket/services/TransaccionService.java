package com.AlkemyPocket.services;

import com.AlkemyPocket.model.*;
import com.AlkemyPocket.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private ExtraccionRepository extraccionRepo;

    @Autowired
    private DepositoRepository depositoRepo;

    @Autowired
    private TransferenciaRepository transferenciaRepository;
    @Autowired
    private CuentaRepository cuentaRepository;


    public List<Transaccion> listarTransacciones() {
        List<Transaccion> lista = transaccionRepository.findAll();
        if (lista.isEmpty()) {
            throw new RuntimeException("No hay transacciones :(");
        } else {
            return lista;
        }
    }

    public List<Transaccion> obtenerTransaccionesPorCuenta(String numeroCuenta) {
        List<Transaccion> resultado = new ArrayList<>();

        resultado.addAll(extraccionRepo.findByCuentaOrigen_NumeroCuenta(numeroCuenta));
        resultado.addAll(depositoRepo.findByCuentaDestino_NumeroCuenta(numeroCuenta));
        resultado.addAll(transferenciaRepository.findByCuentaOrigen_NumeroCuentaOrCuentaDestino_NumeroCuenta(numeroCuenta, numeroCuenta));

        return resultado;
    }

    @Transactional
    public Transferencia realizarTransferencia(String nroCuentaOrigen, String nroCuentaDestino, BigDecimal monto) {

        Cuenta cuentaOrigen = cuentaRepository.findByNumeroCuenta(nroCuentaOrigen)
                .orElseThrow(() -> new RuntimeException("Cuenta origen no existe"));

        Cuenta cuentaDestino = cuentaRepository.findByNumeroCuenta(nroCuentaDestino)
                .orElseThrow(() -> new RuntimeException("Cuenta destino no existe"));

        if (cuentaOrigen.equals(cuentaDestino)) {
            throw new RuntimeException("No se puede transferir a la misma cuenta.");
        }

        if (cuentaOrigen.getMonto().compareTo(monto) < 0) {
            throw new RuntimeException("Saldo insuficiente en cuenta origen");
        }

        // Para restar y sumar:
        cuentaOrigen.setMonto(cuentaOrigen.getMonto().subtract(monto));
        cuentaDestino.setMonto(cuentaDestino.getMonto().add(monto));

        // Guardar cambios en cuentas
        cuentaRepository.save(cuentaOrigen);
        cuentaRepository.save(cuentaDestino);

        // Crear transferencia
        Transferencia transferencia = new Transferencia();
        transferencia.setCuentaOrigen(cuentaOrigen);
        transferencia.setCuentaDestino(cuentaDestino);
        transferencia.setMonto(monto);
        transferencia.setDescripcion("Transferencia de " + nroCuentaOrigen + " a " + nroCuentaDestino);
        transferencia.setFecha(LocalDateTime.now());
        transferencia.setEstado(EstadoTransaccion.Completada);

        return transferenciaRepository.save(transferencia);
    }
}
