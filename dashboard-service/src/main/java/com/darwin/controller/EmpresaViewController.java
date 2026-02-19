//package com.darwin.controller;
//
//import com.darwin.cliente.EmpresaClient;
//import com.darwin.dto.EmpresaDTO;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.util.List;
//
//
//
//@Controller
//@RequestMapping("/empresas")
//public class EmpresaViewController {
//
//    private final EmpresaClient empresaClient;
//
//    public EmpresaViewController(EmpresaClient empresaClient) {
//        this.empresaClient = empresaClient;
//    }
//
//    @GetMapping
//    public String listar(Model model) {
//        List<EmpresaDTO> empresas = empresaClient.listarEmpresas(0L);
//        model.addAttribute("empresas", empresas);
//        model.addAttribute("empresaForm", new EmpresaDTO());
//        return "empresas";
//    }
//
//    @PostMapping
//    public String guardar(@ModelAttribute EmpresaDTO empresaDTO, RedirectAttributes ra) {
//        empresaClient.crearEmpresa(empresaDTO);
//        ra.addFlashAttribute("mensaje", "Empresa creada correctamente");
//        return "redirect:/empresas";
//
//    }
//}
