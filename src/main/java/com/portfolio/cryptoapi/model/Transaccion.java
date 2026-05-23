package com.portfolio.cryptoapi.model;

// IMPORTS
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo; // Ejemplo: "DEPOSITO", "COMPRA", "VENTA".

    @Column(nullable = false)
    private Double cantidad; // Cantidad de dinero o cripto

    @Column(nullable = false)
    private String moneda;

    @Column(name = "fecha_transaccion", updatable = false)
    private LocalDateTime fechaTransaccion;

    // --- RELACIÓN CON LA CARTERA ---
    // ManyToOne -> Muchas transacciones pertenecen a una sola cartera.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartera_id", nullable = false)
    @JsonIgnore // Para prevenir bucle infinito.
    private Cartera cartera;

    @PrePersist
    protected void onCreate() {
        this.fechaTransaccion = LocalDateTime.now();
    }

    // --- CONSTRUCTORES ---
    public Transaccion() {}

    public Transaccion(String tipo, Double cantidad, String moneda, Cartera cartera) {
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.moneda = moneda;
        this.cartera = cartera;
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Double getCantidad() { return cantidad; }
    public void setCantidad(Double cantidad) { this.cantidad = cantidad; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public LocalDateTime getFechaTransaccion() { return fechaTransaccion; }

    public Cartera getCartera() { return cartera; }
    public void setCartera(Cartera cartera) { this.cartera = cartera; }
}