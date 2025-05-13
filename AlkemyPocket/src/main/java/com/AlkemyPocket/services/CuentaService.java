package com.AlkemyPocket.services;

import com.AlkemyPocket.dto.TraerCuentaDTO;
import com.AlkemyPocket.dto.UsuarioParaCuentaDTO;
import com.AlkemyPocket.model.Cuenta;
import com.AlkemyPocket.model.Usuario;
import com.AlkemyPocket.repository.CuentaRepository;
import com.AlkemyPocket.repository.UsuarioRepository;
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

    public Cuenta crearCuenta(Integer usuarioId, String tipo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String numeroCuenta = generarNumeroCuentaUnico();
        String cvu = generarCvuUnico();
        String alias = generarAliasUnico();

        Cuenta cuenta = Cuenta.builder()
                .numeroCuenta(numeroCuenta)
                .moneda("Ars")
                .monto(BigDecimal.ZERO)
                .fecha(LocalDateTime.now())
                .alias(alias)
                .tipo(tipo != null && (tipo.equals("CA") || tipo.equals("CC")) ? tipo : "CA")
                .cvu(cvu)
                .usuario(usuario)
                .build();

        return cuentaRepository.save(cuenta);
    }

    public List<TraerCuentaDTO> obtenerCuentas() {
        return cuentaRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    };

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

    public Optional<Cuenta> obtenerCuentaPorNumero(String numeroCuenta) {
        return cuentaRepository.findById(numeroCuenta);
    }

    public void eliminarCuenta(String numeroCuenta) {
        cuentaRepository.deleteById(numeroCuenta);
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
