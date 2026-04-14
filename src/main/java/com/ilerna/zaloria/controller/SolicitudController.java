package com.ilerna.zaloria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import com.ilerna.zaloria.model.Solicitud;
import com.ilerna.zaloria.repository.SolicitudRepository;
import java.util.List;
import org.springframework.ui.Model;



@Controller
public class SolicitudController {
    @Autowired
    private SolicitudRepository solicitudRepo;

    @PostMapping("/solicitudes/guardar")
    public String guardarSolicitud(@ModelAttribute Solicitud solicitud) {
        // Guardamos la solicitud que viene del formulario
        solicitudRepo.save(solicitud);
        // Redirigimos a la comunidad con un mensaje de éxito (opcional)
        return "redirect:/?exito=true";
    }
    // VER las solicitudes (Panel Admin)
    @GetMapping("/admin/solicitudes")
    public String listarSolicitudes(Model model) {
        // Buscamos todas las solicitudes en la DB
       // Dentro del método que carga el index.html
        List<Solicitud> lista = solicitudRepo.findAll();
        model.addAttribute("solicitudes", lista);
        return "admin-solicitudes";
    }
    @PostMapping("/admin/solicitudes/actualizar")
    public String actualizarEstado(@RequestParam("id") Integer id, @RequestParam("estado") String estado) {
    // 1. Buscamos la solicitud por ID
    Solicitud solicitud = solicitudRepo.findById(id).orElse(null);
    
    if (solicitud != null) {
        // 2. Cambiamos el estado (ACEPTADA o RECHAZADA)
        solicitud.setEstado(estado);
        // 3. Guardamos los cambios
        solicitudRepo.save(solicitud);
    }
    
    // Redirigimos de vuelta a la lista para ver el cambio
    return "redirect:/admin/solicitudes";
}
}
