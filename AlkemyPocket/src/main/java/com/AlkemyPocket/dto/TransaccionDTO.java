package com.AlkemyPocket.dto;

import com.AlkemyPocket.model.EstadoTransaccion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransaccionDTO {

    @Schema(example = "1")
    private Integer id_transaccion;

    @Schema(example = "2500.75")
    private BigDecimal monto;

    @Schema(example = "Pago de alquiler")
    private String descripcion;

    @Schema(example = "2024-05-17T12:00:00")
    private LocalDateTime fecha;

    @Schema(description = "Estado de la transacción: PENDIENTE, EXITOSA o FALLIDA", example = "Completada")
    private EstadoTransaccion estado;

    public Integer getId_transaccion() { return id_transaccion; }
    public void setId_transaccion(Integer id_transaccion) { this.id_transaccion = id_transaccion; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public EstadoTransaccion getEstado() { return estado; }
    public void setEstado(EstadoTransaccion estado) { this.estado = estado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
