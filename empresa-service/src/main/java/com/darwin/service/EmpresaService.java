package com.darwin.service;

import com.darwin.entity.Empresa;
import com.darwin.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {


    private final EmpresaRepository repository;



    public Empresa guardar(Empresa empresa){
        return repository.save(empresa);
    }

    public List<Empresa> listar(){
        return repository.findAll();
    }
}
