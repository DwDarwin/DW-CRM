package com.darwin.controller;

import com.darwin.cliente.ContactoClient;
import com.darwin.cliente.EmpresaClient;
import com.darwin.cliente.InteraccionClient;
import com.darwin.dto.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String verDashboard(@RequestParam(required = false) Long empresaId,
                                Model model)  {

        List<EmpresaDTO> empresas = empresaClient.listarEmpresas(0L);
        List<DashboardEmpresaDTO> dashboard = new ArrayList<>();

        for (EmpresaDTO empresa : empresas) {

            // filtro empresa
            if (empresaId != null && !empresa.getId().equals(empresaId)) {
                continue;
            }

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
        int totalEmpresas = dashboard.size();
        int totalContactos = 0;
        int totalInteracciones = 0;

        for (DashboardEmpresaDTO emp : dashboard) {
            totalContactos += emp.getContactos().size();

            for (DashboardContactoDTO con : emp.getContactos()) {
                totalInteracciones += con.getInteracciones().size();
            }
        }

        model.addAttribute("totalEmpresas", totalEmpresas);
        model.addAttribute("totalContactos", totalContactos);
        model.addAttribute("totalInteracciones", totalInteracciones);


        model.addAttribute("empresas", empresas);
        model.addAttribute("empresaSeleccionada", empresaId);
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
    @PostMapping("/dashboard/contacto/eliminar/{id}")
    public String eliminarContacto(@PathVariable Long id) {
        contactoClient.eliminarContacto(id);
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/interaccion")
    public String crearInteraccion(@ModelAttribute InteraccionDTO interaccionDTO) {

        System.out.println("===== DEBUG =====");
        System.out.println("contactoId: " + interaccionDTO.getContactoId());
        System.out.println("fecha: " + interaccionDTO.getFecha());
        System.out.println("tipo: " + interaccionDTO.getTipo());
        System.out.println("resultado: " + interaccionDTO.getResultado());
        System.out.println("=================");

        interaccionClient.crearInteraccion(interaccionDTO);

        return "redirect:/dashboard";
    }

      @PostMapping("/dashboard/interaccion/eliminar/{id}")
      public String eliminarInteraccion(@PathVariable Long id) {
         interaccionClient.eliminarInteracciones(id);
        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/empresa/eliminar/{id}")
    public String eliminarEmpresa(@PathVariable Long id) {
        empresaClient.eliminarEmpresa(id);
        return "redirect:/dashboard";
    }




}
