package com.darwin.dto;

import lombok.Data;

@Data
public class EmpresaDTO {
    private Long id;   // Se puede omitir el envío de ID y dirección para el JSON
    private String nombre;
    private String nit;
    private String direccion;
    private String telefono;
    private String correoGeneral;
}

