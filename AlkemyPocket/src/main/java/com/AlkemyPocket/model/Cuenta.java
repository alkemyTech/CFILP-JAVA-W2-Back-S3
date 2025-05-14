package com.AlkemyPocket.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data // El uso de Lombok permite la generacion automatica y "por detras" de constructores getters y setters.
@NoArgsConstructor // El uso de Lombok permite la generacion automatica y "por detras" de constructores getters y setters.
@AllArgsConstructor // El uso de Lombok permite la generacion automatica y "por detras" de constructores getters y setters.
@Builder // El uso de Lombok permite la generacion automatica y "por detras" de constructores getters y setters.
public class Cuenta {

    @Id
    private String numeroCuenta;

    @Column(nullable = false, columnDefinition = "TEXT DEFAULT 'Ars'")
    private String moneda;

    @Column(nullable = false)
    private BigDecimal monto = BigDecimal.ZERO;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fecha;

    @Column(unique = true)
    private String alias;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false, unique = true, length = 22)
    private String cvu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
