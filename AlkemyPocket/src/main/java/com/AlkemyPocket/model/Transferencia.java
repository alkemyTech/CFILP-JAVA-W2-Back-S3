package com.AlkemyPocket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Transferencia extends Transaccion {

    @ManyToOne
    @JoinColumn(name = "cuenta_origen", referencedColumnName = "numero_cuenta", nullable = false)
    private Cuenta cuentaOrigen;

    @ManyToOne
    @JoinColumn(name = "cuenta_destino", referencedColumnName = "numero_cuenta", nullable = false)
    private Cuenta cuentaDestino;

    public Cuenta getCuentaOrigen() { return cuentaOrigen; }
    public void setCuentaOrigen(Cuenta cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }

    public Cuenta getCuentaDestino() { return cuentaDestino; }
    public void setCuentaDestino(Cuenta cuentaDestino) { this.cuentaDestino = cuentaDestino; }

}
