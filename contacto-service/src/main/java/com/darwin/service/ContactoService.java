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

    public Contacto actualizar(Long id, Contacto datos){
        Contacto contacto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("\"Contacto no encontrado con id: \" + id"));

        contacto.setNombre(datos.getNombre());
        contacto.setCargo(datos.getCargo());
        contacto.setTelefono(datos.getTelefono());
        contacto.setCelular(datos.getCelular());
        contacto.setCorreo(datos.getCorreo());

        return repository.save(contacto);
    }

    public void eliminar(Long id){
        if(!repository.existsById(id)){
            throw new RuntimeException("Contacto no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }


}
