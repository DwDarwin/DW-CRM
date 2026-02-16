package com.darwin.controller;

import com.darwin.entity.Empresa;
import com.darwin.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {


    private final EmpresaService service;


    @PostMapping
    public Empresa crear(@RequestBody Empresa empresa){
        return service.guardar(empresa);
    }

    @GetMapping
    public List<Empresa> listar(){
        return service.listar();
    }
}
