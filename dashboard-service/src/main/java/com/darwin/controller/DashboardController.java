package com.darwin.controller;



import com.darwin.cliente.ContactoClient;
import com.darwin.cliente.InteraccionClient;
import com.darwin.dto.ContactoDTO;
import com.darwin.dto.InteraccionDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final ContactoClient contactoClient;
    private final InteraccionClient interaccionClient;

    public DashboardController(ContactoClient contactoClient,
                               InteraccionClient interaccionClient) {
        this.contactoClient = contactoClient;
        this.interaccionClient = interaccionClient;
    }

    @GetMapping("/resumen")
    public Map<String, Object> obtenerResumen(@RequestParam Long empresaId) {

        List<ContactoDTO> contactos = contactoClient.listarContactos(empresaId);
        List<InteraccionDTO> todasInteracciones = new ArrayList<>();
        for (ContactoDTO contacto : contactos) {
            List<InteraccionDTO> interacciones =
                    interaccionClient.listarInteracciones(contacto.getId());
            todasInteracciones.addAll(interacciones);
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("contactos", contactos);
        respuesta.put("interacciones", todasInteracciones);

        return respuesta;
    }
}
