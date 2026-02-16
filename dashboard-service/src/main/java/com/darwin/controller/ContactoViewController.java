package com.darwin.controller;


import com.darwin.cliente.ContactoClient;
import com.darwin.dto.ContactoDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/contactos")
public class ContactoViewController {

    private final ContactoClient contactoClient;

    public ContactoViewController(ContactoClient contactoClient) {
        this.contactoClient = contactoClient;
    }

    @GetMapping
    public String listarContactos(@RequestParam Long empresaId, Model model) {
        List<ContactoDTO> contactos = contactoClient.listarContactos(empresaId);
        model.addAttribute("contactos", contactos);
        model.addAttribute("empresaId", empresaId);
        model.addAttribute("contactoForm", new ContactoDTO());
        return "contactos";  // plantilla Thymeleaf
    }

    @PostMapping
    public String crearContacto(@ModelAttribute ContactoDTO contactoDTO, RedirectAttributes ra) {
        contactoClient.crearContacto(contactoDTO);
        ra.addFlashAttribute("mensaje", "Contacto creado correctamente");
        return "redirect:/contactos?empresaId=" + contactoDTO.getEmpresaId();
    }
}
