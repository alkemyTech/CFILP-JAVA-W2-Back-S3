package com.AlkemyPocket.controllers;

import com.AlkemyPocket.model.Cuenta;
import com.AlkemyPocket.services.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public Cuenta crearCuenta(@RequestParam Integer usuarioId, @RequestParam(required = false) String tipo) {
        return cuentaService.crearCuenta(usuarioId, tipo);
    }

    @GetMapping
    public List<Cuenta> listarCuentas() {
        return cuentaService.obtenerCuentas();
    }

    @GetMapping("/{numeroCuenta}")
    public Cuenta obtenerCuenta(@PathVariable String numeroCuenta) {
        return cuentaService.obtenerCuentaPorNumero(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    @DeleteMapping("/{numeroCuenta}")
    public void eliminarCuenta(@PathVariable String numeroCuenta) {
        cuentaService.eliminarCuenta(numeroCuenta);
    }
}
