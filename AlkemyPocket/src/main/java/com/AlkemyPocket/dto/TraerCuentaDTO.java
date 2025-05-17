package com.AlkemyPocket.dto;

import com.AlkemyPocket.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor; // El uso de Lombok permite la generacion automatica y "por detras" de constructores getters y setters.
import lombok.Data; // El uso de Lombok permite la generacion automatica y "por detras" de constructores getters y setters.
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor; // El uso de Lombok permite la generacion automatica y "por detras" de constructores getters y setters.


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraerCuentaDTO {

    @Schema(example = "1234567890")
    private String numeroCuenta;

    @Schema(example = "ARS")
    private String moneda;

    @Schema(example = "15000.00")
    private BigDecimal monto;

    @Schema(example = "2024-05-17T14:35:00")
    private LocalDateTime fecha;

    @Schema(example = "mi.alias.cuenta")
    private String alias;

    @Schema(example = "CAJA_AHORRO")
    private String tipo;

    @Schema(example = "0001234500001234567890")
    private String cvu;

    private UsuarioParaCuentaDTO usuario;
}
