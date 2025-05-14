package com.AlkemyPocket.services;

import com.AlkemyPocket.dto.TraerCuentaDTO;
import com.AlkemyPocket.dto.UsuarioParaCuentaDTO;
import com.AlkemyPocket.model.Cuenta;
import com.AlkemyPocket.model.Usuario;
import com.AlkemyPocket.repository.CuentaRepository;
import com.AlkemyPocket.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;



    // === CREACION DE CUENTA ===
    // El unico problema que se podria presentar seria que el usuario pueda tener mas de dos cuentas, aunque no estaría totalmente mal.
    public Cuenta crearCuenta(Integer usuarioId, String tipo, String moneda) {
        Usuario usuario = usuarioRepository.findById(usuarioId) // Claramente cuando la cuenta se crea junto con el usuario el ID siempre se va a encontrar pero puede ocurrir que se cree una cuenta el Dolares luego para el usuario.
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String numeroCuenta = generarNumeroCuentaUnico();
        String cvu = generarCvuUnico();
        String alias = generarAliasUnico();

        Cuenta cuenta = Cuenta.builder()
                .numeroCuenta(numeroCuenta)
                .moneda(moneda != null && (moneda.equals("Ars") || moneda.equals("Dolar") || moneda.equals("Euro") || moneda.equals("Real"))  ? moneda : "Ars")
                .monto(BigDecimal.ZERO)
                .fecha(LocalDateTime.now())
                .alias(alias)
                .tipo(tipo != null && (tipo.equals("CA") || tipo.equals("CC")) ? tipo : "CA")
                .cvu(cvu)
                .usuario(usuario)
                .build();

        return cuentaRepository.save(cuenta);
    }



    // === TRAER TODAS LAS CUENTAS ===
    public List<TraerCuentaDTO> obtenerCuentas() {
        List<TraerCuentaDTO> cuentas = cuentaRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
        if (cuentas.isEmpty()){
            throw new RuntimeException("No hay cuentas para ningun usuario registrado en Alkemy Pocket.");
        } else {
            return cuentas;
        }
    };
    // Dto para traer las cuentas.
    private TraerCuentaDTO mapToDTO(Cuenta cuenta) {

        Usuario usuario = cuenta.getUsuario();

        UsuarioParaCuentaDTO usuarioParaCuenta = new UsuarioParaCuentaDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getRol()
        );

        return new TraerCuentaDTO(
                cuenta.getNumeroCuenta(),
                cuenta.getMoneda(),
                cuenta.getMonto(),
                cuenta.getFecha(),
                cuenta.getAlias(),
                cuenta.getTipo(),
                cuenta.getCvu(),
                usuarioParaCuenta
        );
    };


    // === TRAER CUENTA POR PK ===
    public TraerCuentaDTO obtenerCuentaDTOporNumero(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con el numero " + numeroCuenta));

        return mapToDTO(cuenta);
    }


    // === ELIMINAR CUENTA POR PK ===
    public void eliminarCuenta(String numeroCuenta) {
        if (!cuentaRepository.existsById(numeroCuenta)) {
            throw new EntityNotFoundException("La cuenta con el numero " + numeroCuenta + " no existe");
        }
        try {
            cuentaRepository.deleteById(numeroCuenta); //
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la cuenta: " + e.getMessage());
        }
    }

    // ==== Generadores ====

    private String generarNumeroCuentaUnico() {
        String numero;
        do {
            numero = generarNumeroAleatorio(10);
        } while (cuentaRepository.existsById(numero));
        return numero;
    }

    private String generarCvuUnico() {
        String numero;
        do {
            numero = generarNumeroAleatorio(22);
        } while (cuentaRepository.findByCvu(numero).isPresent());
        return numero;
    }

    private String generarAliasUnico() {
        String alias;
        do {
            alias = generarAliasAleatorio();
        } while (cuentaRepository.findByAlias(alias).isPresent());
        return alias;
    }

    private String generarNumeroAleatorio(int longitud) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String generarAliasAleatorio() {
        String[] palabras = {"sol", "luna", "mar", "roca", "nube", "fuego"};
        Random random = new Random();
        return palabras[random.nextInt(palabras.length)] +
                "." +
                palabras[random.nextInt(palabras.length)] +
                "." +
                (random.nextInt(900) + 100);
    }
}
