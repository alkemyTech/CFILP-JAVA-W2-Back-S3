package com.AlkemyPocket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Extraccion")
public class Extraccion extends Transaccion{

    @ManyToOne
    @JoinColumn(name = "cuenta_origen", referencedColumnName = "numero_cuenta", nullable = false)
    private Cuenta cuentaOrigen;

    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(Cuenta cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }
}
