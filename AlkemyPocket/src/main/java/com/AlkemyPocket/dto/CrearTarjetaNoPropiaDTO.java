package com.AlkemyPocket.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearTarjetaNoPropiaDTO {
    private String numeroTarjeta;
    private String codigoSeguridad;
    private LocalDate fechaVencimiento;
    private String compania;
    private String tipo; // DEBITO O CREDITO
    private LocalDate fechaEmision;
    private String particular;
}
