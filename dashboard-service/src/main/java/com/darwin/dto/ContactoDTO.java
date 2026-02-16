package com.darwin.dto;

import lombok.Data;

@Data
public class ContactoDTO {

    private Long id;
    private Long empresaId;
    private String nombre;
    private String cargo;
    private String celular;
    private String telefono;
    private String correo;
}
