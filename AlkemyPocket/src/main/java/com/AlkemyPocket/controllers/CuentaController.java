package com.AlkemyPocket.controllers;

import com.AlkemyPocket.dto.TraerCuentaDTO;
import com.AlkemyPocket.services.CuentaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    // Creacion de una cuenta AUTOMATICA y NO AUTOMATICA.

    @PostMapping
    public ResponseEntity<?> crearCuenta(@RequestParam Integer usuarioId, @RequestParam(required = false) String tipo, @RequestParam (required = false) String moneda) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.crearCuenta(usuarioId, tipo, moneda));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // Traer TODAS las cuentas con manejo de errores que pueden venir del servicio.

    @GetMapping
    public ResponseEntity<List<TraerCuentaDTO>> listarCuentas() {
        List <TraerCuentaDTO> cuentas = cuentaService.obtenerCuentas();
        return ResponseEntity.ok(cuentas);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> manejarRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Traer una sola cuenta con manejo de excepción directamente en el controlador.

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<TraerCuentaDTO> obtenerCuenta(@PathVariable String numeroCuenta) {
        TraerCuentaDTO cuenta = cuentaService.obtenerCuentaDTOporNumero(numeroCuenta);
        return ResponseEntity.ok(cuenta);
    }

    // Eliminacion de cuentas con errores que pueden venir desde el servicio.

    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<String> eliminarCuenta(@PathVariable String numeroCuenta) {
        cuentaService.eliminarCuenta(numeroCuenta);
        return ResponseEntity.ok("Cuenta eliminada correctamente");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> manejarNoEncontrado(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarErrores(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error: " + ex.getMessage());
    }
}
