package com.AlkemyPocket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data // El uso de Lombok permite la generacion automatica y "por detras" de constructores getters y setters.
@NoArgsConstructor // El uso de Lombok permite la generacion automatica y "por detras" de constructores getters y setters.
@AllArgsConstructor // El uso de Lombok permite la generacion automatica y "por detras" de constructores getters y setters.
@Builder // El uso de Lombok permite la generacion automatica y "por detras" de constructores getters y setters.
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cuenta {

    @Id
    @Column(name = "numero_cuenta")
    private String numeroCuenta;

    @Column(nullable = false, columnDefinition = "TEXT DEFAULT 'Ars'")
    private String moneda;

    @Setter
    @Getter
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
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
            name = "Cuenta_Tarjeta",
            joinColumns = @JoinColumn(name = "numero_cuenta"),
            inverseJoinColumns = @JoinColumn(name = "numero_tarjeta")
    )
    @JsonManagedReference // 👈 esta es la clave
    private Set<Tarjeta> tarjetas = new HashSet<>();
}
