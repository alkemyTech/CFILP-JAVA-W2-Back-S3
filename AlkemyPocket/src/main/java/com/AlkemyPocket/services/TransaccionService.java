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
    private ExtraccionRepository extraccionRepository;

    @Autowired
    private DepositoRepository depositoRepository;

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

        resultado.addAll(extraccionRepository.findByCuentaOrigen_NumeroCuenta(numeroCuenta));
        resultado.addAll(depositoRepository.findByCuentaDestino_NumeroCuenta(numeroCuenta));
        resultado.addAll(transferenciaRepository.findByCuentaOrigen_NumeroCuentaOrCuentaDestino_NumeroCuenta(numeroCuenta, numeroCuenta));

        return resultado;
    }

    @Transactional
    public Transferencia realizarTransferencia(String nroCuentaOrigen, String nroCuentaDestino, BigDecimal monto, String descripcion) {

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

        String descripcionTransferencia = (descripcion == null || descripcion.isBlank())
            ? "Transferencia de " + nroCuentaOrigen + " a " + nroCuentaDestino
            : descripcion;

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
        transferencia.setDescripcion(descripcionTransferencia);
        transferencia.setFecha(LocalDateTime.now());
        transferencia.setEstado(EstadoTransaccion.Completada);

        return transferenciaRepository.save(transferencia);
    }

    @Transactional
    public Deposito realizarDeposito(String destino, BigDecimal monto, String descripcion){
        Cuenta CuentaDestino = cuentaRepository.findByNumeroCuenta(destino)
                .orElseThrow(() -> new RuntimeException("Cuenta destino no existe"));

        if(monto.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("No puedes ingresar un monto no positivo o nulo");
        }

        String descripcionDeposito = (descripcion == null || descripcion.isBlank())
            ? "Deposito de " + monto + "$ en " + destino
            : descripcion;


        // Guardamos el dinero en la cuenta
        CuentaDestino.setMonto(CuentaDestino.getMonto().add(monto));

        // Creamos el deposito
        Deposito deposito = new Deposito();

        deposito.setCuentaDestino(CuentaDestino);
        deposito.setMonto(monto);
        deposito.setDescripcion(descripcionDeposito);
        deposito.setFecha(LocalDateTime.now());
        deposito.setEstado(EstadoTransaccion.Completada);

        return depositoRepository.save(deposito);
    }

    @Transactional
    public Extraccion realizarExtraccion(String origen, BigDecimal monto, String descripcion){
        Cuenta cuentaOrigen = cuentaRepository.findByNumeroCuenta(origen)
                .orElseThrow(() -> new RuntimeException("Cuenta origen no existe"));

        if(monto.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("No puedes extraer un monto no positivo o nulo");
        }

        if(monto.compareTo(cuentaOrigen.getMonto()) > 0){
            throw new RuntimeException("No puedes extraer un monto que no tienes -.-");
        }

        String descripcionExtraccion = (descripcion == null || descripcion.isBlank())
            ? "Extraccion de " + monto + "$ en " + origen
            : descripcion;

        // Extraemos el dinero de la cuenta
        cuentaOrigen.setMonto(cuentaOrigen.getMonto().subtract(monto));

        // Creamos la extraccion
        Extraccion extraccion = new Extraccion();

        extraccion.setMonto(monto);
        extraccion.setCuentaOrigen(cuentaOrigen);
        extraccion.setDescripcion(descripcionExtraccion);
        extraccion.setFecha(LocalDateTime.now());
        extraccion.setEstado(EstadoTransaccion.Completada);

        return extraccionRepository.save(extraccion);
    }
}
