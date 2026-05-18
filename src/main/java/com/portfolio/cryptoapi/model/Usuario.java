package com.portfolio.cryptoapi.model;

// IMPORTS
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity // Para decirle a Spring que esta clase de Java será una tabla en PostgreSQL.
@Table(name = "usuarios") // Forzar a que la tabla se llame "usuarios" en minúscula.
public class Usuario {

    @Id // Este será la clave primaria (Primary Key).
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental.
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "El e-mail es obligatorio.")
    @Email(message = "Debe ser un formato de e-mail válido.")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Double saldoDisponible = 0.0; // Los usuarios empiezan con 0€.

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    // Se ejecuta automáticamente justo antes de guardar el usuario por primera vez en la BD.
    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }

    // --- CONSTRUCTORES ---
    public Usuario() {
    }

    public Usuario(String username, String email) {
        this.username = username;
        this.email = email;
        this.saldoDisponible = 0.0; // Aseguramos que el saldo inicial sea 0€.
    }

    // --- GETTERS & SETTERS ---
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Double getSaldoDisponible() {
        return saldoDisponible;
    }
    public void setSaldoDisponible(Double saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}
