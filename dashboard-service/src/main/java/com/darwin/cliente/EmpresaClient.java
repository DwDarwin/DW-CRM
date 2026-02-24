package com.darwin.cliente;

import com.darwin.dto.ContactoDTO;
import com.darwin.dto.EmpresaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "empresa-service")
public interface EmpresaClient {

    @GetMapping("/empresas")
    List<EmpresaDTO> listarEmpresas(@RequestParam Long empresaId);

    @PostMapping("/empresas")
    EmpresaDTO crearEmpresa(@RequestBody EmpresaDTO empresa);

    @DeleteMapping("/empresas/{id}")
    void eliminarEmpresa(@PathVariable Long id);

    @PutMapping("/empresas/{id}")
    EmpresaDTO editarEmpresa(@PathVariable Long id,@RequestBody EmpresaDTO empresa);

}
