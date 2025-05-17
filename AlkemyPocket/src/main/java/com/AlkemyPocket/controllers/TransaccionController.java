package com.AlkemyPocket.controllers;

import com.AlkemyPocket.dto.TransaccionDTO;
import com.AlkemyPocket.model.*;
import com.AlkemyPocket.repository.CuentaRepository;
import com.AlkemyPocket.repository.TransaccionRepository;
import com.AlkemyPocket.services.TransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Transacciones", description = "Operaciones de transacciones")
@RestController
@RequestMapping("/AlkemyPocket/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    @Operation(summary = "Obtener todas las transacciones")
    @GetMapping
    public ResponseEntity<List<Transaccion>> listarTransacciones() {
        List<Transaccion> transacciones = transaccionService.listarTransacciones();
        return ResponseEntity.ok(transacciones);
    }

    // http://localhost:8080/AlkemyPocket/transacciones/traer-por?numeroCuenta=001-123456/1 -> Trea transacciones asociadas a ese num-cuenta

    @Operation(summary = "Obtener transacciones por cuenta")
    @GetMapping("/traer-por")
    public List<TransaccionDTO> getTransaccionesPorCuenta(@RequestParam  String numeroCuenta) {
        List<Transaccion> transacciones = transaccionService.obtenerTransaccionesPorCuenta(numeroCuenta);

        return transacciones.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private TransaccionDTO convertirADTO(Transaccion t) {
        TransaccionDTO dto = new TransaccionDTO();
        dto.setId_transaccion(t.getId_transaccion());
        dto.setMonto(t.getMonto());
        dto.setDescripcion(t.getDescripcion());
        dto.setFecha(t.getFecha());
        dto.setEstado(t.getEstado());
        return dto;
    }

    @Operation(summary = "Transferir dinero a una cuenta")
    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestParam String origen,
                                        @RequestParam String destino,
                                        @RequestParam BigDecimal monto) {
        try {
            Transferencia transferencia = transaccionService.realizarTransferencia(origen, destino, monto);
            return ResponseEntity.ok(convertirADTO(transferencia));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Depositar dinero en una cuenta")
    @PostMapping("/depositar")
    public ResponseEntity<?> depositar(@RequestParam String destino,
                                       @RequestParam BigDecimal monto){
        try{
            Deposito deposito = transaccionService.realizarDeposito(destino, monto);
            return ResponseEntity.ok(convertirADTO(deposito));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @Operation(summary = "Extraer dinero de una cuenta")
    @PostMapping("/extraer")
    public ResponseEntity<?> extraer(@RequestParam String origen,
                                     @RequestParam BigDecimal monto){
        try{
            Extraccion extraccion = transaccionService.realizarExtraccion(origen, monto);
            return ResponseEntity.ok(convertirADTO(extraccion));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}