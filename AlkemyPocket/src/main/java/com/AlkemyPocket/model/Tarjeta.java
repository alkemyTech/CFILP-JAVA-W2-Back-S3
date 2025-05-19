package com.AlkemyPocket.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tarjeta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarjeta {

    @Id
    @Column(name = "numero_tarjeta")
    private String numeroTarjeta;

    @Column(name = "codigo_seguridad", nullable = false)
    private String codigoSeguridad;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "compania", nullable = false)
    private String compania;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoTarjeta tipo = TipoTarjeta.CREDITO;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "particular", nullable = false)
    private String particular;

    @Enumerated(EnumType.STRING)
    @Column(name = "propia", nullable = false)
    private PropiaTarjeta propia = PropiaTarjeta.TERCERO;

    @ManyToMany(mappedBy = "tarjetas")
    @JsonBackReference // 👈 esta evita que se serialice en bucle
    private Set<Cuenta> cuentas = new HashSet<>();
}