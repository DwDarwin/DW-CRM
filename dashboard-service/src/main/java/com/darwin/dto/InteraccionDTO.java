package com.darwin.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Data
public class InteraccionDTO {

    private Long id;
    private Long contactoId;

   // @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fecha;
    private String tipo;
    private String descripcion;
    private String resultado;

   // @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaSeguimiento;
}




