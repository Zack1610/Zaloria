package com.ilerna.zaloria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import com.ilerna.zaloria.repository.EquiposRepository;
import com.ilerna.zaloria.repository.JugadorRepository;
import com.ilerna.zaloria.model.Equipos;
import com.ilerna.zaloria.model.Solicitud;
import com.ilerna.zaloria.repository.SolicitudRepository;
import java.io.File;
import java.util.List;
import org.springframework.ui.Model;



@Controller
public class SolicitudController {
    @Autowired
    private SolicitudRepository solicitudRepo;
    
    @Autowired
    private EquiposRepository equiposRepo;

    @Autowired
    private JugadorRepository jugadorRepo;

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
        model.addAttribute("solicitudes", solicitudRepo.findAll());
        
        // FILTRADO DE EQUIPOS CON CUPO
        List<Equipos> equiposConCupo = equiposRepo.findAll().stream()
                .filter(e -> {
                    // Contamos cuántos jugadores tiene este equipo actualmente
                    long actuales = jugadorRepo.countByEquipo(e);
                    // Obtenemos el máximo de la DB (si es null por algún error, usamos 5 por defecto)
                    int maximo = (e.getMaxJugadores() != null) ? e.getMaxJugadores() : 5;
                    // Solo dejamos pasar al equipo si aún tiene sitio
                    return actuales < maximo;
                })
                .toList();

        // Enviamos la lista FILTRADA al modelo
        model.addAttribute("listaEquipos", equiposConCupo);

        // Añadimos las skins de la carpeta static
        File carpetaSkins = new File("src/main/resources/static/images/skins/");
        model.addAttribute("listaSkins", carpetaSkins.list());
        
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
