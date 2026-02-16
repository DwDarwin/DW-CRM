package com.darwin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Interaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long contactoId;
    private LocalDateTime fecha;
    private String tipo;
    private String descripcion;
    private String resultado;
    private LocalDateTime fechaSeguimiento;
}

