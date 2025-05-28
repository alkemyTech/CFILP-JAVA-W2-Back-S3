package com.AlkemyPocket.controllers;

import com.AlkemyPocket.dto.TransaccionDTO;
import com.AlkemyPocket.dto.TransaccionDetalleDTO;
import com.AlkemyPocket.model.*;
import com.AlkemyPocket.services.TransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Transacciones", description = "Operaciones de transacciones")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/AlkemyPocket/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;


    // GET DE TODAS LAS TRANSACCIONES QUE HAY EN LA APP.

    @Operation(summary = "Obtener todas las transacciones que hay en la WALLET.")
    @GetMapping
    public ResponseEntity<List<TransaccionDTO>> listarTransacciones() {
        List<Transaccion> transacciones = transaccionService.listarTransacciones();

        List<TransaccionDTO> dtoList = transacciones.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }


    // TRAE TODAS LAS TRANSACCIONES DE UNA CUENTA PARTICULAR.

    @Operation(summary = "Obtener transacciones de una cuenta en particular.")
    @GetMapping("/porCuenta")
    public List<TransaccionDetalleDTO> getTransaccionesPorCuenta(@RequestParam  String numeroCuenta) {
        List<Transaccion> transacciones = transaccionService.obtenerTransaccionesPorCuenta(numeroCuenta);

        return transacciones.stream()
                .map(t -> convertirADetalleDTO(t, numeroCuenta))
                .collect(Collectors.toList());
    }




    // TRANSFERENCIA

    @Operation(summary = "Transferir dinero desde una cuenta registrada a otra.")
    @PostMapping("/transferirDinero")
    public ResponseEntity<?> transferir(@RequestParam String origen,
                                        @RequestParam String destino,
                                        @RequestParam BigDecimal monto,
                                        @RequestParam(required = false) String descripcion) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(convertirADTO(transaccionService.realizarTransferencia(origen, destino, monto, descripcion)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


 // DEPOSITO

    @Operation(summary = "Depositar dinero en una cuenta")
    @PostMapping("/depositarDinero")
    public ResponseEntity<?> depositar(@RequestParam String destino,
                                       @RequestParam BigDecimal monto,
                                       @RequestParam(required = false) String descripcion) {
        try {
            Deposito deposito = transaccionService.realizarDeposito(destino, monto, descripcion);
            return ResponseEntity.ok(convertirADTO(deposito));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // EXTRACCION

    @Operation(summary = "Extraer dinero de una cuenta")
    @PostMapping("/extraerDinero")
    public ResponseEntity<?> extraer(@RequestParam String origen,
                                     @RequestParam BigDecimal monto,
                                     @RequestParam(required = false) String descripcion) {
        try {
            Extraccion extraccion = transaccionService.realizarExtraccion(origen, monto, descripcion);
            return ResponseEntity.ok(convertirADTO(extraccion));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ======= METODOS DE CONVERSION INTERNOS DE LA CLASE =======


    // Metodo clase 1.
    private TransaccionDTO convertirADTO(Transaccion t) {
        TransaccionDTO dto = new TransaccionDTO();
        dto.setId_transaccion(t.getId_transaccion());
        dto.setMonto(t.getMonto());
        dto.setDescripcion(t.getDescripcion());
        dto.setFecha(t.getFecha());
        dto.setEstado(t.getEstado());

        if (t instanceof Deposito) {
            dto.setTipoTransaccion("DEPOSITO");
            Deposito d = (Deposito) t;
            dto.setCuentaDestino(d.getCuentaDestino().getNumeroCuenta());

        } else if (t instanceof Extraccion) {
            dto.setTipoTransaccion("EXTRACCION");
            Extraccion e = (Extraccion) t;
            dto.setCuentaOrigen(e.getCuentaOrigen().getNumeroCuenta());

        } else if (t instanceof Transferencia) {
            dto.setTipoTransaccion("TRANSFERENCIA");
            Transferencia tr = (Transferencia) t;
            dto.setCuentaOrigen(tr.getCuentaOrigen().getNumeroCuenta());
            dto.setCuentaDestino(tr.getCuentaDestino().getNumeroCuenta());

        } else {
            dto.setTipoTransaccion("DESCONOCIDA");
        }

        return dto;
    }
    // Metodo de clase especial.
    private TransaccionDetalleDTO convertirADetalleDTO(Transaccion t, String cuentaConsultada) {
        TransaccionDetalleDTO dto = new TransaccionDetalleDTO();
        dto.setId_transaccion(t.getId_transaccion());
        dto.setMonto(t.getMonto());
        dto.setDescripcion(t.getDescripcion());
        dto.setFecha(t.getFecha());
        dto.setEstado(t.getEstado());

        if (t instanceof Deposito) {
            dto.setTipoTransaccion("DEPOSITO");
            Deposito d = (Deposito) t;
            String cuentaDestino = d.getCuentaDestino().getNumeroCuenta();
            dto.setCuentaDestino(cuentaDestino);
            dto.setImpacto(cuentaDestino.equals(cuentaConsultada) ? "INGRESO" : "EGRESO");

        } else if (t instanceof Extraccion) {
            dto.setTipoTransaccion("EXTRACCION");
            Extraccion e = (Extraccion) t;
            String cuentaOrigen = e.getCuentaOrigen().getNumeroCuenta();
            dto.setCuentaOrigen(cuentaOrigen);
            dto.setImpacto(cuentaOrigen.equals(cuentaConsultada) ? "EGRESO" : "INGRESO");

        } else if (t instanceof Transferencia) {
            dto.setTipoTransaccion("TRANSFERENCIA");
            Transferencia tr = (Transferencia) t;
            String cuentaOrigen = tr.getCuentaOrigen().getNumeroCuenta();
            String cuentaDestino = tr.getCuentaDestino().getNumeroCuenta();
            dto.setCuentaOrigen(cuentaOrigen);
            dto.setCuentaDestino(cuentaDestino);
            dto.setImpacto(cuentaOrigen.equals(cuentaConsultada) ? "EGRESO" : "INGRESO");

        } else {
            dto.setTipoTransaccion("DESCONOCIDA");
            dto.setImpacto("DESCONOCIDO");
        }

        return dto;
    }


  // === MANEJO DE ERRORES ===

  // NOTA IMPORTANTE CON RESPECTO A EXCEPCIONES: EN MUCHOS METODOS ESTAMOS USANDO TRY CATCH LO CUAL DEVULEVE SOLO UN TEXTO PLANO (PODRIAMOS MODIFICARLO INTERNAMENTE
  // PERO ES UN LABURAZO Y NO HAY TIEMPO). EN OTROS METODOS NO HAY TRAY CATCH POR LO TANTO SE DEVUELVE UNA RESPUESTA MAS COMPLETA EN JSON.


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Bad Request");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }




}