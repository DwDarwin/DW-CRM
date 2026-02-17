package com.darwin.controller;

import com.darwin.entity.Interaccion;
import com.darwin.service.InteraccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interacciones")
@RequiredArgsConstructor
public class InteraccionController {

    private final InteraccionService service;

    @PostMapping
    public Interaccion crear(@RequestBody Interaccion interaccion){
        return service.guardar(interaccion);
    }

    @GetMapping
    public List<Interaccion> listar(@RequestParam Long contactoId){

        return service.listarPorContacto(contactoId);
    }

    @GetMapping("/pendientes")
    public List<Interaccion> pendientes(){

        return service.pendientes();
    }

    @PutMapping("/{id}")
    public Interaccion actualizar(@PathVariable Long id,
                               @RequestBody Interaccion interaccion){
        return service.actualizar(id, interaccion );
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id){
        service.eliminar(id);
    }
}
