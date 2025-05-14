package com.AlkemyPocket.dto;

import com.AlkemyPocket.model.Usuario;
import lombok.AllArgsConstructor; // El uso de Lombok permite la generacion automatica y "por detras" de constructores getters y setters.
import lombok.Data; // El uso de Lombok permite la generacion automatica y "por detras" de constructores getters y setters.
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor; // El uso de Lombok permite la generacion automatica y "por detras" de constructores getters y setters.


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraerCuentaDTO {
    private String numeroCuenta;
    private String moneda;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private String alias;
    private String tipo;
    private String cvu;
    private UsuarioParaCuentaDTO usuario;
}
