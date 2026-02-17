package com.darwin.controller;

import com.darwin.cliente.ContactoClient;
import com.darwin.cliente.EmpresaClient;
import com.darwin.cliente.InteraccionClient;
import com.darwin.dto.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {

    private final EmpresaClient empresaClient;
    private final ContactoClient contactoClient;
    private final InteraccionClient interaccionClient;

    public DashboardController(EmpresaClient empresaClient,
                               ContactoClient contactoClient,
                               InteraccionClient interaccionClient) {
        this.empresaClient = empresaClient;
        this.contactoClient = contactoClient;
        this.interaccionClient = interaccionClient;
    }

    @GetMapping("/dashboard")
    public String verDashboard(Model model) {

        List<EmpresaDTO> empresas = empresaClient.listarEmpresas(0L);
        List<DashboardEmpresaDTO> dashboard = new ArrayList<>();

        for (EmpresaDTO empresa : empresas) {

            DashboardEmpresaDTO dashEmpresa = new DashboardEmpresaDTO();
            dashEmpresa.setEmpresa(empresa);

            List<ContactoDTO> contactos = contactoClient.listarContactos(empresa.getId());
            List<DashboardContactoDTO> dashContactos = new ArrayList<>();

            for (ContactoDTO contacto : contactos) {

                DashboardContactoDTO dashContacto = new DashboardContactoDTO();
                dashContacto.setContacto(contacto);

                List<InteraccionDTO> interacciones =
                        interaccionClient.listarInteracciones(contacto.getId());

                dashContacto.setInteracciones(interacciones);
                dashContactos.add(dashContacto);
            }

            dashEmpresa.setContactos(dashContactos);
            dashboard.add(dashEmpresa);
        }

        model.addAttribute("dashboard", dashboard);

        // Formularios vacíos
        model.addAttribute("empresaForm", new EmpresaDTO());
        model.addAttribute("contactoForm", new ContactoDTO());
        model.addAttribute("interaccionForm", new InteraccionDTO());

        return "dashboard";
    }

    @PostMapping("/dashboard/empresa")
    public String crearEmpresa(@ModelAttribute EmpresaDTO empresaDTO) {
        empresaClient.crearEmpresa(empresaDTO);
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/contacto")
    public String crearContacto(@ModelAttribute ContactoDTO contactoDTO) {
        contactoClient.crearContacto(contactoDTO);
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/interaccion")
    public String crearInteraccion(@ModelAttribute InteraccionDTO interaccionDTO) {
        interaccionClient.crearInteraccion(interaccionDTO);
        return "redirect:/dashboard";
    }

}
