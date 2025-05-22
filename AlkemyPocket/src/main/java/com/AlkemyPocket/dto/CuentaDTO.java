package com.AlkemyPocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CuentaDTO {
    private String numeroCuenta;
    private String moneda;
    private BigDecimal monto;
    private String alias;
    private String tipo;
    private String cvu;
}
