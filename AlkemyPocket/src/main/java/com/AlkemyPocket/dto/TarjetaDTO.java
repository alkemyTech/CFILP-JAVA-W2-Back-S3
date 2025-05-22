package com.AlkemyPocket.dto;

import com.AlkemyPocket.model.TipoTarjeta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TarjetaDTO {

    private String numeroTarjeta;
    private LocalDate fechaVencimiento;
    private String compania;
    private TipoTarjeta tipo;
    private LocalDate fechaEmision;
    private String particular;
}

