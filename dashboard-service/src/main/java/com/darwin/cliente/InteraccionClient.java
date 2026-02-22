package com.darwin.cliente;

import com.darwin.dto.EmpresaDTO;
import com.darwin.dto.InteraccionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "interaccion-service", url = "http://localhost:8083")
public interface InteraccionClient {


    @GetMapping("/interacciones")
    List<InteraccionDTO> listarInteracciones( @RequestParam("contactoId") Long contactoId);

    @PostMapping("/interacciones")
    InteraccionDTO crearInteraccion(@RequestBody InteraccionDTO interaccion);

    @DeleteMapping("/interacciones/{id}")
    void eliminarInteracciones(@PathVariable Long id);

    @PutMapping("/interacciones/{id}")
    InteraccionDTO editarInteraccion(@PathVariable Long id,@RequestBody InteraccionDTO interaccion);



}
