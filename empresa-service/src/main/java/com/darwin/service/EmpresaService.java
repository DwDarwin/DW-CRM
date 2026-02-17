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

    public Empresa actualizar(Long id, Empresa datos) {

        Empresa empresa = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada con id: " + id));

        empresa.setNombre(datos.getNombre());
        empresa.setNit(datos.getNit());
        empresa.setDireccion(datos.getDireccion());
        empresa.setTelefono(datos.getTelefono());
        empresa.setCorreoGeneral(datos.getCorreoGeneral());

        return repository.save(empresa);
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Empresa no encontrada con id: " + id);
        }
        repository.deleteById(id);
    }

}
