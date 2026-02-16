package com.darwin.cliente;

import com.darwin.dto.ContactoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "contacto-service", url = "http://localhost:8082")
public interface ContactoClient {

    @GetMapping("/contactos")
    List<ContactoDTO> listarContactos(@RequestParam Long empresaId);

    @PostMapping("/contactos")
    ContactoDTO crearContacto(@RequestBody ContactoDTO contacto);
}
