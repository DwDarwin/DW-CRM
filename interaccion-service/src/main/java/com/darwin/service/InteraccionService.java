package com.darwin.service;


import com.darwin.entity.Interaccion;
import com.darwin.repository.InteraccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class InteraccionService {

    private final InteraccionRepository repository;

    public Interaccion guardar(Interaccion interaccion){
        return repository.save(interaccion);
    }

    public List<Interaccion> listarPorContacto(Long contactoId){
        return repository.findByContactoId(contactoId);
    }

    public List<Interaccion> pendientes(){
        return repository.findByFechaSeguimientoBefore(LocalDateTime.now());
    }
}
