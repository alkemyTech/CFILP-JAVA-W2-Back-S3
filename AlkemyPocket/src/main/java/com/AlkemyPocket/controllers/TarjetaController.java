package com.AlkemyPocket.controllers;

import com.AlkemyPocket.dto.CrearTarjetaNoPropiaDTO;
import com.AlkemyPocket.dto.TarjetaDTO;
import com.AlkemyPocket.model.Tarjeta;
import com.AlkemyPocket.services.TarjetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/AlkemyPocket/tarjetas")
public class TarjetaController {

    @Autowired
    private TarjetaService tarjetaService;

    // Crear tarjeta NO PROPIA asociada a una cuenta
    @PostMapping("/noPropia/{numeroCuenta}")
    public Tarjeta crearTarjeta(@RequestBody CrearTarjetaNoPropiaDTO dto,
                                @PathVariable String numeroCuenta) {
        return tarjetaService.crearTarjetaNoPropia(dto, numeroCuenta);
    }

    // Crear tarjeta PROPIA asociada a una cuenta
    @PostMapping("/propia/{numeroCuenta}")
    public Tarjeta crearTarjeta(@PathVariable String numeroCuenta,
                                @RequestParam String nombreTitular) {
        return tarjetaService.crearTarjetaPropia(nombreTitular, numeroCuenta);
    }

    // Traer tarjetas por cuenta
    @GetMapping("/{numeroCuenta}")
    public List<Tarjeta> obtenerPorCuenta(@PathVariable String numeroCuenta) {
        return tarjetaService.obtenerTarjetasPorCuenta(numeroCuenta);
    }

    // Eliminar tarjeta por número
    @DeleteMapping("/{numeroTarjeta}")
    public ResponseEntity<String>  eliminarTarjeta(@PathVariable String numeroTarjeta) {
        tarjetaService.eliminarTarjeta(numeroTarjeta);
        return ResponseEntity.ok("Tarjeta eliminada correctamente");
    }

    // Traer todas las tarjetas de un usuario.
    @GetMapping("/porUsuario/{idUsuario}")
    public ResponseEntity<List<TarjetaDTO>> obtenerPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(tarjetaService.obtenerTarjetasPorUsuario(idUsuario));
    }

    // === MANEJO DE ERRORES EN TERMINOS GENERALES ===


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Bad Request");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }






}






