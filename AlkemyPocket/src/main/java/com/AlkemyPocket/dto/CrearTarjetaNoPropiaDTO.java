package com.AlkemyPocket.dto;

import com.AlkemyPocket.helpers.LocalDateMultiFormatDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para registrar una tarjeta que no es propia de la WALLET.")
public class CrearTarjetaNoPropiaDTO {

    @Schema(description = "Número de la tarjeta", example = "4111111111111111", required = true)
    private String numeroTarjeta;

    @Schema(description = "Código de seguridad de la tarjeta (CVV)", example = "123", required = true)
    private String codigoSeguridad;

    @JsonDeserialize(using = LocalDateMultiFormatDeserializer.class)
    @Schema(description = "Fecha de vencimiento de la tarjeta (formato yyyy-MM-dd)", example = "2026-12-31", required = true)
    private LocalDate fechaVencimiento;

    @Schema(description = "Compañía emisora de la tarjeta", example = "Visa", required = true)
    private String compania;

    @Schema(description = "Tipo de tarjeta (CREDITO o DEBITO)", example = "CREDITO", required = true)
    private String tipo;

    @JsonDeserialize(using = LocalDateMultiFormatDeserializer.class)
    @Schema(description = "Fecha de emisión de la tarjeta", example = "2022-01-01", required = true)
    private LocalDate fechaEmision;

    @Schema(description = "Nombre del titular de la tarjeta", example = "Juan Pérez", required = true)
    private String particular;
}
