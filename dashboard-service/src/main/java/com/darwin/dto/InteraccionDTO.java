package com.darwin.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InteraccionDTO {

    private Long id;
    private Long contactoId;
    private LocalDateTime fecha;
    private String tipo;
    private String descripcion;
    private String resultado;
    private LocalDateTime fechaSeguimiento;
}
