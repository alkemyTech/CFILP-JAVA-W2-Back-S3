package com.AlkemyPocket.dto;

import com.AlkemyPocket.model.EstadoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransaccionDTO {
    private Integer id_transaccion;
    private BigDecimal monto;
    private String descripcion;
    private LocalDateTime fecha;
    private EstadoTransaccion estado;
    private String tipoTransaccion;
    private String cuentaOrigen;
    private String cuentaDestino;

}
