package com.AlkemyPocket.controllers;

import com.AlkemyPocket.dto.ContactosFrecuentesDTO;
import com.AlkemyPocket.dto.CuentaDTO;
import com.AlkemyPocket.dto.TraerCuentaDTO;
import com.AlkemyPocket.model.Cuenta;
import com.AlkemyPocket.services.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Cuentas")
@RestController
@RequestMapping("/AlkemyPocket/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;




    // Creacion de una cuenta AUTOMATICA y NO AUTOMATICA.

    @Operation(summary = "Crear una cuenta referida a un usuario",
            description = "La creación puede ser automatica con la creacion del mismo usuario o puede ser aparte una vez que el usuario ya creado la solicita.")
    @PostMapping
    public ResponseEntity<?> crearCuenta(@RequestParam Integer usuarioId, @RequestParam(required = false) String tipo, @RequestParam (required = false) String moneda) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.crearCuenta(usuarioId, tipo, moneda));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }


    // Traer TODAS las cuentas CON INFORMACIÓN SENSIBLE con manejo de errores que pueden venir del servicio (el manejo estas mas abajo).

    @Operation(summary = "Trae todas las cuentas de la WALLET",
            description = "Trae las cuentas CON INFORMACION SENSIBLE, ideal para el uso por parte del Admin.")
    @GetMapping("/completo")
    public List<Cuenta> listarTodasLasCuentas() {
        return cuentaService.obtenerTodasLasCuentas();
    }


    // Traer TODAS las cuentas SIN INFORMACIÓN SENSIBLE con manejo de errores que pueden venir del servicio.

    @Operation(summary = "Trae todas las cuentas de la WALLET",
            description = "Trae las cuentas SIN INFORMACION SENSIBLE.")
    @GetMapping
    public ResponseEntity<List<TraerCuentaDTO>> listarCuentas() {
        List <TraerCuentaDTO> cuentas = cuentaService.obtenerCuentas();
        return ResponseEntity.ok(cuentas);
    }


    // Traer una sola cuenta SIN INFORMACION SENSIBLE con manejo de excepción directamente en el controlador.

    @Operation(summary = "Trae información de una sola cuenta",
            description = "De acuerdo al numero de cuenta se trae TODA la informacion de esa cuenta SIN DATOS SENSIBLES. Ideal para el frontend.")
    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<TraerCuentaDTO> obtenerCuenta(@PathVariable String numeroCuenta) {
        TraerCuentaDTO cuenta = cuentaService.obtenerCuentaDTOporNumero(numeroCuenta);
        return ResponseEntity.ok(cuenta);
    }

    // Traer varias cuentas SIN INFORMACION SENSIBLE de acuerdo al numero de ID.
    @Operation(summary = "Trae todas las cuentas de UN SOLO USUARIO",
            description = "Con base en el ID se obtienen los datos de las cuentas del usuario consultado.")
    @GetMapping("/porUsuario/{idUsuario}")
    public ResponseEntity<List<CuentaDTO>> obtenerCuentasPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(cuentaService.obtenerCuentasPorUsuario(idUsuario));
    }


    // Eliminacion de cuentas con errores que pueden venir desde el servicio.

    @Operation(summary = "Eliminar cuenta")
    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<String> eliminarCuenta(@PathVariable String numeroCuenta) {
        cuentaService.eliminarCuenta(numeroCuenta);
        return ResponseEntity.ok("Cuenta eliminada correctamente");
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Bad Request");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }



    // === VER CONTACTOS FRECUENTES ===

    @Operation(summary = "Obtener contactos frequentes por ID",
    description = "Se obtienen los contactos con los que sabe interactuar un usuario.")
    @GetMapping("/contactos-frecuentes/{idUsuario}")
    public ResponseEntity<List<ContactosFrecuentesDTO>> obtenerContactosFrecuentes(@PathVariable Integer idUsuario) {
        List<ContactosFrecuentesDTO> contactos = cuentaService.obtenerContactosFrecuentes(idUsuario);
        return ResponseEntity.ok(contactos);
    }
}
