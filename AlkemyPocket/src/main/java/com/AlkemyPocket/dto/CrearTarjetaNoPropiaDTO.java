package com.AlkemyPocket.dto;

import com.AlkemyPocket.helpers.LocalDateMultiFormatDeserializer;
import lombok.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearTarjetaNoPropiaDTO {
    private String numeroTarjeta;
    private String codigoSeguridad;

    @JsonDeserialize(using = LocalDateMultiFormatDeserializer.class)
    private LocalDate fechaVencimiento;

    private String compania;
    private String tipo;

    @JsonDeserialize(using = LocalDateMultiFormatDeserializer.class)
    private LocalDate fechaEmision;

    private String particular;
}
