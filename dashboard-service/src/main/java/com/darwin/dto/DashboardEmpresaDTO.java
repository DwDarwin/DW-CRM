package com.darwin.dto;

import lombok.Data;
import java.util.List;

@Data
public class DashboardEmpresaDTO {

    private EmpresaDTO empresa;
    private List<DashboardContactoDTO> contactos;

}
