package com.portfolio.cryptoapi.model;

// IMPORTS
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "carteras")
public class Cartera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El símbolo de la moneda es obligatorio")
    @Column(nullable = false, length = 10)
    private String simboloMoneda; // Ejemplo: "EUR", "BTC", "ETH"

    @Column(nullable = false)
    private Double saldo = 0.0;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    // --- LA RELACIÓN SQL (CLAVE FORÁNEA) ---
    // @ManyToOne indica que MUCHAS carteras pueden pertenecer a UN solo usuario.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false) // Crea la columna 'usuario_id' en la Base de Datos.
    @JsonIgnore
    private Usuario usuario;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    // --- CONSTRUCTORES ---
    public Cartera() {}

    public Cartera(String simboloMoneda, Usuario usuario) {
        this.simboloMoneda = simboloMoneda;
        this.usuario = usuario;
        this.saldo = 0.0;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSimboloMoneda() { return simboloMoneda; }
    public void setSimboloMoneda(String simboloMoneda) { this.simboloMoneda = simboloMoneda; }
    
    public Double getSaldo() { return saldo; }
    public void SetSaldo(Double saldo) { this.saldo = saldo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public Usuario getUsuario() { return usuario; }
    public void SetUsuario(Usuario usuario) { this.usuario = usuario; }
}

