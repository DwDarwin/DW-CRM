package com.darwin.controller;

import com.darwin.entity.Contacto;
import com.darwin.service.ContactoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contactos")
@RequiredArgsConstructor
public class ContactoController {

    private final ContactoService service;

    @PostMapping
    public Contacto crear(@RequestBody Contacto contacto){

        return service.guardar(contacto);
    }

    @GetMapping
    public List<Contacto> listar(@RequestParam Long empresaId){

        return service.listarPorEmpresa(empresaId);
    }

    @PutMapping("/contactos{id}")
    public Contacto actualizar(@PathVariable Long id,
                              @RequestBody Contacto contacto){
        return service.actualizar(id, contacto );
    }

    @DeleteMapping("/contactos{id}")
    public void eliminar(@PathVariable Long id){
        service.eliminar(id);
    }


}
