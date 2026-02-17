package com.darwin.dto;


import lombok.Data;
import java.util.List;

@Data
public class DashboardContactoDTO {

    private ContactoDTO contacto;
    private List<InteraccionDTO> interacciones;

}