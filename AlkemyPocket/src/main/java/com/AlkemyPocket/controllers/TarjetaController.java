package com.AlkemyPocket.controllers;

import com.AlkemyPocket.dto.CrearTarjetaNoPropiaDTO;
import com.AlkemyPocket.model.Tarjeta;
import com.AlkemyPocket.services.TarjetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public void eliminarTarjeta(@PathVariable String numeroTarjeta) {
        tarjetaService.eliminarTarjeta(numeroTarjeta);
    }
}
