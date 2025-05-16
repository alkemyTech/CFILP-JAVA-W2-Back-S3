package com.AlkemyPocket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Deposito")
public class Deposito extends Transaccion {

    @ManyToOne
    @JoinColumn(name = "cuenta_destino", referencedColumnName = "numero_cuenta", nullable = false)
    private Cuenta cuentaDestino;

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }
}
