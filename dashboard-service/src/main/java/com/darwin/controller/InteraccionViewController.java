//package com.darwin.controller;
//
//import com.darwin.cliente.InteraccionClient;
//import com.darwin.dto.InteraccionDTO;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/contactos/{contactoId}/interacciones")
//public class InteraccionViewController {
//
//    private final InteraccionClient interaccionClient;
//
//    public InteraccionViewController(InteraccionClient interaccionClient) {
//        this.interaccionClient = interaccionClient;
//    }
//
//    @GetMapping
//    public String listarInteracciones(@PathVariable Long contactoId, Model model) {
//        List<InteraccionDTO> interacciones = interaccionClient.listarInteracciones(contactoId);
//        model.addAttribute("interacciones", interacciones);
//        model.addAttribute("contactoId", contactoId);
//        model.addAttribute("interaccionForm", new InteraccionDTO());
//        return "interacciones";
//    }
//
//    @PostMapping
//    public String crearInteraccion(@PathVariable Long contactoId,
//                                   @ModelAttribute InteraccionDTO interaccionDTO) {
//        interaccionDTO.setContactoId(contactoId);
//        interaccionClient.crearInteraccion(interaccionDTO);
//        return "redirect:/contactos/" + contactoId + "/interacciones";
//    }
//}
