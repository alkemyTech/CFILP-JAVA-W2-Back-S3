package com.AlkemyPocket.services;

import com.AlkemyPocket.dto.CrearTarjetaNoPropiaDTO;
import com.AlkemyPocket.model.Cuenta;
import com.AlkemyPocket.model.PropiaTarjeta;
import com.AlkemyPocket.model.Tarjeta;
import com.AlkemyPocket.model.TipoTarjeta;
import com.AlkemyPocket.repository.CuentaRepository;
import com.AlkemyPocket.repository.TarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TarjetaService {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    public Tarjeta crearTarjetaNoPropia(CrearTarjetaNoPropiaDTO dto, String numeroCuenta) {

        validarDatosTarjeta(dto);

        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(dto.getNumeroTarjeta())
                .codigoSeguridad(dto.getCodigoSeguridad())
                .fechaVencimiento(dto.getFechaVencimiento())
                .compania(dto.getCompania())
                .tipo(TipoTarjeta.valueOf(dto.getTipo()))
                .fechaEmision(dto.getFechaEmision())
                .particular(dto.getParticular())
                .build();

        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        Tarjeta nueva = tarjetaRepository.save(tarjeta);

        tarjeta.setCuentas(new HashSet<>());

        tarjeta.getCuentas().add(cuenta);
        cuenta.getTarjetas().add(tarjeta);

        cuentaRepository.save(cuenta); // se guarda la relación también

        return nueva;
    }

    public Tarjeta crearTarjetaPropia(String nombreTitular, String numeroCuenta) {
        String numeroTarjeta = generarNumeroTarjeta();
        String codigoSeguridad = generarCodigoSeguridad();
        LocalDate fechaEmision = LocalDate.now();
        LocalDate fechaVencimiento = fechaEmision.plusYears(3);
        String compania = "AlkemyPocket";
        TipoTarjeta tipo = TipoTarjeta.CREDITO;
        PropiaTarjeta propia = PropiaTarjeta.PROPIA;
        String particular = nombreTitular;

        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        // Asocia la cuenta a la tarjeta antes de guardar
        Set<Cuenta> cuentas = new HashSet<>();
        cuentas.add(cuenta);

        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .codigoSeguridad(codigoSeguridad)
                .fechaVencimiento(fechaVencimiento)
                .compania(compania)
                .tipo(tipo)
                .fechaEmision(fechaEmision)
                .particular(particular)
                .propia(propia)
                .cuentas(cuentas) // 👈 importante: se setea antes de guardar
                .build();

        tarjetaRepository.save(tarjeta); // guarda con la cuenta ya asociada

        // También agregamos la tarjeta a la cuenta, por el lado inverso
        cuenta.getTarjetas().add(tarjeta);
        cuentaRepository.save(cuenta); // guarda la cuenta actualizada

        return tarjeta;
    }

    public List<Tarjeta> obtenerTarjetasPorCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return new ArrayList<>(cuenta.getTarjetas());
    }

    public void eliminarTarjeta(String numeroTarjeta) {
        Tarjeta tarjeta = tarjetaRepository.findById(numeroTarjeta)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        // Eliminar relaciones con cuentas
        tarjeta.getCuentas().forEach(cuenta -> cuenta.getTarjetas().remove(tarjeta));

        tarjetaRepository.delete(tarjeta);
    }

    private void validarDatosTarjeta(CrearTarjetaNoPropiaDTO dto) {
        if (!dto.getNumeroTarjeta().matches("\\d{16}")) {
            throw new IllegalArgumentException("El número de tarjeta debe tener exactamente 16 dígitos numéricos.");
        }

        if (!dto.getCodigoSeguridad().matches("\\d{3}")) {
            throw new IllegalArgumentException("El código de seguridad debe tener exactamente 3 dígitos numéricos.");
        }

        if(dto.getCompania().equals("AlkemyPocket")){
            throw new IllegalArgumentException("La compañia no debe ser AlkemyPocket, para ello solicita la tarjeta de CREDITO de nuestra Wallet");
        }

        if (!dto.getFechaVencimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de vencimiento debe ser posterior a hoy.");
        }

        if (dto.getFechaEmision().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de emisión no puede ser futura.");
        }
    }

    private String generarNumeroTarjeta() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10)); // números del 0 al 9
        }
        return sb.toString();
    }

    private String generarCodigoSeguridad() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }


}
