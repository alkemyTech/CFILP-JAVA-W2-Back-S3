package com.AlkemyPocket.dto;

import com.AlkemyPocket.model.EstadoTransaccion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransaccionDTO {

    private Integer id_transaccion;
    private BigDecimal monto;
    private String descripcion;
    private LocalDateTime fecha;
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
