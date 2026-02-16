package com.darwin.service;

import com.darwin.entity.Contacto;
import com.darwin.repository.ContactoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor    //crea el constructor para la injection de dependencia
public class ContactoService {

    private final ContactoRepository repository;

    public Contacto guardar(Contacto contacto){

        return repository.save(contacto);
    }

    public List<Contacto> listarPorEmpresa(Long empresaId){

        return repository.findByEmpresaId(empresaId);
    }
}
