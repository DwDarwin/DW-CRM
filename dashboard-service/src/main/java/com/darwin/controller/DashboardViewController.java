package com.darwin.controller;

import com.darwin.cliente.ContactoClient;
import com.darwin.cliente.EmpresaClient;
import com.darwin.cliente.InteraccionClient;
import com.darwin.dto.ContactoDTO;
import com.darwin.dto.EmpresaDTO;
import com.darwin.dto.InteraccionDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dashboard/view")
public class DashboardViewController {


    private final ContactoClient contactoClient;
    private final InteraccionClient interaccionClient;
    private final EmpresaClient empresaClient;


    public DashboardViewController(ContactoClient contactoClient,
                                   InteraccionClient interaccionClient, EmpresaClient empresaClient, EmpresaClient empresaClient1) {
        this.contactoClient = contactoClient;
        this.interaccionClient = interaccionClient;
        this.empresaClient = empresaClient1;
    }

    // Tus clientes Feign
    @GetMapping("/resumen")
    public String verResumen(@RequestParam Long empresaId, Model model) {

        // Obtener contactos
        var contactos = contactoClient.listarContactos(empresaId);

        // Obtener todas las interacciones de esos contactos
        List<InteraccionDTO> todasInteracciones = new ArrayList<>();

        for (var contacto : contactos) {
            todasInteracciones.addAll(
                    interaccionClient.listarInteracciones(contacto.getId())
            );
        }

          // **Obtener la empresa usando FeignClient**
        List<EmpresaDTO> empresas = empresaClient.listarEmpresas(empresaId);
        EmpresaDTO empresa = null;
        if (!empresas.isEmpty()) {
            empresa = empresas.get(0); // Solo la primera, que es la correcta
        }


        model.addAttribute("contactos", contactos);
        model.addAttribute("interacciones", todasInteracciones);
          // Para mostrar la empresa
        model.addAttribute("empresas", empresa);

        return "resumen";   // Thymeleaf buscará resumen.html
   }
}
