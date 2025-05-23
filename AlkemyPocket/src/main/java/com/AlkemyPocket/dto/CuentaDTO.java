package com.AlkemyPocket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO que representa datos necesarios de una cuenta bancaria")
public class CuentaDTO {

    @Schema(description = "Número único de la cuenta", example = "001234567890", required = true)
    private String numeroCuenta;

    @Schema(description = "Tipo de moneda de la cuenta", example = "ARS", required = true)
    private String moneda;

    @Schema(description = "Monto actual disponible en la cuenta", example = "12500.50", required = true)
    private BigDecimal monto;

    @Schema(description = "Alias personalizado de la cuenta", example = "juan.mi.cuenta", required = true)
    private String alias;

    @Schema(description = "Tipo de cuenta", example = "CAJA_DE_AHORRO", required = true)
    private String tipo;

    @Schema(description = "Clave Virtual Uniforme (CVU) de la cuenta", example = "0000003100000123456789", required = true)
    private String cvu;
}