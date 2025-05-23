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


    // TRAE TODAS LAS TRANSACCIONES DE LA APP

    public List<Transaccion> listarTransacciones() {
        List<Transaccion> lista = transaccionRepository.findAll();
        if (lista.isEmpty()) {
            throw new IllegalArgumentException("No hay transacciones realizas todavía en AlkemyPocket");
        } else {
            return lista;
        }
    }

    // TRAE TODAS LAS TRANSACCIONES DE UNA CUENTA PARTICULAR.

    public List<Transaccion> obtenerTransaccionesPorCuenta(String numeroCuenta) {

        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("La cuenta cuyos datos fueron solicitados no existe"));


        List<Transaccion> resultado = new ArrayList<>();

        resultado.addAll(extraccionRepository.findByCuentaOrigen_NumeroCuenta(numeroCuenta));
        resultado.addAll(depositoRepository.findByCuentaDestino_NumeroCuenta(numeroCuenta));
        resultado.addAll(transferenciaRepository.findByCuentaOrigen_NumeroCuentaOrCuentaDestino_NumeroCuenta(numeroCuenta, numeroCuenta));

        if (resultado.isEmpty()) {
            throw new IllegalArgumentException("No hay transacciones realizas todavía con la cuenta cuya información fué solicitada.");
        } else {
            return resultado;
        }

    }

    // Realiza una transferencia con posibles breaks.

    @Transactional
    public Transferencia realizarTransferencia(String nroCuentaOrigen, String nroCuentaDestino, BigDecimal monto, String descripcion) {

        Cuenta cuentaOrigen = cuentaRepository.findByNumeroCuenta(nroCuentaOrigen)
                .orElseThrow(() -> new RuntimeException("Cuenta origen no existe"));

        Cuenta cuentaDestino = cuentaRepository.findByNumeroCuenta(nroCuentaDestino)
                .orElseThrow(() -> new RuntimeException("Cuenta destino no existe"));

        if (!cuentaOrigen.getMoneda().equals(cuentaDestino.getMoneda())){
            throw new IllegalArgumentException("No puede transferir " + cuentaOrigen.getMoneda() + " a una cuenta cuya moneda es " + cuentaDestino.getMoneda());
        };

        if (cuentaOrigen.equals(cuentaDestino)) {
            throw new IllegalArgumentException("No se puede transferir a la misma cuenta.");
        }

        if (cuentaOrigen.getMonto().compareTo(monto) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente en cuenta origen");
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

    // DEPOSITO con posibles breacks.

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


        CuentaDestino.setMonto(CuentaDestino.getMonto().add(monto));

        Deposito deposito = new Deposito();

        deposito.setCuentaDestino(CuentaDestino);
        deposito.setMonto(monto);
        deposito.setDescripcion(descripcionDeposito);
        deposito.setFecha(LocalDateTime.now());
        deposito.setEstado(EstadoTransaccion.Completada);

        return depositoRepository.save(deposito);
    }

    // EXTRACCION OK.

    @Transactional
    public Extraccion realizarExtraccion(String origen, BigDecimal monto, String descripcion){
        Cuenta cuentaOrigen = cuentaRepository.findByNumeroCuenta(origen)
                .orElseThrow(() -> new RuntimeException("Cuenta origen no existe"));

        if(monto.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("No puedes extraer un monto no positivo o nulo");
        }

        if(monto.compareTo(cuentaOrigen.getMonto()) > 0){
            throw new IllegalArgumentException("El saldo de la cuenta es insuficiente");
        }

        String descripcionExtraccion = (descripcion == null || descripcion.isBlank())
            ? "Extraccion de " + monto + "$ en " + origen
            : descripcion;

        cuentaOrigen.setMonto(cuentaOrigen.getMonto().subtract(monto));

        Extraccion extraccion = new Extraccion();

        extraccion.setMonto(monto);
        extraccion.setCuentaOrigen(cuentaOrigen);
        extraccion.setDescripcion(descripcionExtraccion);
        extraccion.setFecha(LocalDateTime.now());
        extraccion.setEstado(EstadoTransaccion.Completada);

        return extraccionRepository.save(extraccion);
    }
}
