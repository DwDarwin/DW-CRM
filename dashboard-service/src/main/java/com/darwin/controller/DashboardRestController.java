package com.darwin.controller;


import com.darwin.cliente.ContactoClient;
import com.darwin.cliente.EmpresaClient;
import com.darwin.cliente.InteraccionClient;
import com.darwin.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardRestController {

    private final EmpresaClient empresaClient;
    private final ContactoClient contactoClient;
    private final InteraccionClient interaccionClient;

    public DashboardRestController(EmpresaClient empresaClient,
                                   ContactoClient contactoClient,
                                   InteraccionClient interaccionClient) {
        this.empresaClient = empresaClient;
        this.contactoClient = contactoClient;
        this.interaccionClient = interaccionClient;
    }

    // Obtener todo el dashboard
    @GetMapping
    public ResponseEntity<?> obtenerDashboard(@RequestParam(required = false) Long empresaId) {
        try {
            List<EmpresaDTO> empresas = empresaClient.listarEmpresas(0L);
            List<DashboardEmpresaDTO> dashboard = new ArrayList<>();

            for (EmpresaDTO empresa : empresas) {
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

            // Calcular totales
            int totalEmpresas = dashboard.size();
            int totalContactos = 0;
            int totalInteracciones = 0;

            for (DashboardEmpresaDTO emp : dashboard) {
                totalContactos += emp.getContactos().size();
                for (DashboardContactoDTO con : emp.getContactos()) {
                    totalInteracciones += con.getInteracciones().size();
                }
            }

            return ResponseEntity.ok(new DashboardResponse(
                    totalEmpresas,
                    totalContactos,
                    totalInteracciones,
                    dashboard
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener dashboard: " + e.getMessage());
        }
    }

    // Obtener solo empresas
    @GetMapping("/empresas")
    public ResponseEntity<List<EmpresaDTO>> obtenerEmpresas() {
        try {
            return ResponseEntity.ok(empresaClient.listarEmpresas(0L));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Crear empresa
    @PostMapping("/empresas")
    public ResponseEntity<?> crearEmpresa(@RequestBody EmpresaDTO empresaDTO) {
        try {
            empresaClient.crearEmpresa(empresaDTO);
            return ResponseEntity.ok("Empresa creada");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    // Crear contacto
    @PostMapping("/contactos")
    public ResponseEntity<?> crearContacto(@RequestBody ContactoDTO contactoDTO) {
        try {
            contactoClient.crearContacto(contactoDTO);
            return ResponseEntity.ok("Contacto creado");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    // Crear interacción
    @PostMapping("/interacciones")
    public ResponseEntity<?> crearInteraccion(@RequestBody InteraccionDTO interaccionDTO) {
        try {
            interaccionClient.crearInteraccion(interaccionDTO);
            return ResponseEntity.ok("Interacción creada");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    // Eliminar empresa
    @DeleteMapping("/empresas/{id}")
    public ResponseEntity<?> eliminarEmpresa(@PathVariable Long id) {
        try {
            empresaClient.eliminarEmpresa(id);
            return ResponseEntity.ok("Empresa eliminada");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    // Eliminar contacto
    @DeleteMapping("/contactos/{id}")
    public ResponseEntity<?> eliminarContacto(@PathVariable Long id) {
        try {
            contactoClient.eliminarContacto(id);
            return ResponseEntity.ok("Contacto eliminado");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    // Eliminar interacción
    @DeleteMapping("/interacciones/{id}")
    public ResponseEntity<?> eliminarInteraccion(@PathVariable Long id) {
        try {
            interaccionClient.eliminarInteracciones(id);
            return ResponseEntity.ok("Interacción eliminada");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    // Clase interna para la respuesta del dashboard
    public static class DashboardResponse {
        private int totalEmpresas;
        private int totalContactos;
        private int totalInteracciones;
        private List<DashboardEmpresaDTO> dashboard;

        public DashboardResponse(int totalEmpresas, int totalContactos,
                                 int totalInteracciones, List<DashboardEmpresaDTO> dashboard) {
            this.totalEmpresas = totalEmpresas;
            this.totalContactos = totalContactos;
            this.totalInteracciones = totalInteracciones;
            this.dashboard = dashboard;
        }

        // Getters
        public int getTotalEmpresas() { return totalEmpresas; }
        public int getTotalContactos() { return totalContactos; }
        public int getTotalInteracciones() { return totalInteracciones; }
        public List<DashboardEmpresaDTO> getDashboard() { return dashboard; }
    }
}
