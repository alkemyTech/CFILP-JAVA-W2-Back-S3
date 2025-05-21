package com.AlkemyPocket.controllers;

import com.AlkemyPocket.dto.TransaccionDTO;
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
@RequestMapping("/AlkemyPocket/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;


    // GET DE TODAS LAS TRANSACCIONES QUE HAY EN LA APP.

    @Operation(summary = "Obtener todas las transacciones")
    @GetMapping
    public ResponseEntity<List<TransaccionDTO>> listarTransacciones() {
        List<Transaccion> transacciones = transaccionService.listarTransacciones();

        List<TransaccionDTO> dtoList = transacciones.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }


    // TRAE TODAS LAS TRANSACCIONES DE UNA CUENTA PARTICULAR.

    @Operation(summary = "Obtener transacciones por cuenta")
    @GetMapping("/porCuenta")
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


    // TRANSFERENCIA

    @Operation(
            summary = "Transferir dinero a una cuenta",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transferencia exitosa",
                            content = @Content(schema = @Schema(implementation = TransaccionDTO.class))
                    )
            }
    )
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

    @Operation(
            summary = "Depositar dinero en una cuenta",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Depósito exitoso",
                            content = @Content(schema = @Schema(implementation = TransaccionDTO.class))
                    )
            }
    )
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

    @Operation(
            summary = "Extraer dinero de una cuenta",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Extracción exitosa",
                            content = @Content(schema = @Schema(implementation = TransaccionDTO.class))
                    )
            }
    )
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