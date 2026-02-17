package com.darwin.controller;

import com.darwin.cliente.ContactoClient;
import com.darwin.cliente.EmpresaClient;
import com.darwin.cliente.InteraccionClient;
import com.darwin.dto.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

        return "dashboard";
    }
}
