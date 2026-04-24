/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ilerna.zaloria.controller;
import com.ilerna.zaloria.model.Equipos;
import com.ilerna.zaloria.model.Jugador;
import com.ilerna.zaloria.repository.EquiposRepository;
import com.ilerna.zaloria.repository.JugadorRepository;
import com.ilerna.zaloria.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Zack
 */
import java.io.File; // Importante para leer la carpeta

@Controller
public class JugadorController {
    @Autowired
    private JugadorRepository jugadorRepo;

    @Autowired
    private EquiposRepository equiposRepo;
    
    @Autowired
    private SolicitudRepository solicitudRepo; 

   // Listar todos los jugadores
    @GetMapping("/jugadores")
    public String listarJugadores(Model model) {
        model.addAttribute("listaJugadores", jugadorRepo.findAll()); // Trae todos los datos de la DB
        return "jugadores-lista"; // Carga la página de la lista
    }

    // Formulario para nuevo jugador
    @GetMapping("/jugadores/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("jugador", new Jugador()); // Crea objeto vacío para el formulario
        model.addAttribute("listaEquipos", equiposRepo.findAll()); // Lista de equipos para el selector
        
        // Lee la carpeta de imágenes para elegir la Skin
        File carpetaSkins = new File("src/main/resources/static/images/skins/");
        model.addAttribute("listaSkins", carpetaSkins.list()); 
        
        return "jugadores-form";
    }

   @PostMapping("/jugadores/guardar")
public String guardarJugador(Jugador jugador, @RequestParam(value="solicitudId", required=false) Integer solicitudId, RedirectAttributes ra) {
    
    Equipos equipo = equiposRepo.findById(jugador.getEquipo().getId()).orElse(null);
    
    if (equipo != null) {
        long actuales = jugadorRepo.countByEquipo(equipo);
        
        if (actuales >= equipo.getMaxJugadores()) {
            ra.addFlashAttribute("errorTorneo", "⚠️ El equipo " + equipo.getNombre() + " ya está lleno.");
            
            // Si venimos de una solicitud, volvemos allí. Si no, volvemos a la lista de jugadores.
            if (solicitudId != null) {
                return "redirect:/admin/solicitudes";
            } else {
                return "redirect:/jugadores";
            }
        }
    }

    jugadorRepo.save(jugador);
    
    if (solicitudId != null) {
        solicitudRepo.findById(solicitudId).ifPresent(s -> {
            s.setEstado("ACEPTADA");
            solicitudRepo.save(s);
        });
    }
    return "redirect:/jugadores";
}

    // Editar jugador existente
    @GetMapping("/jugadores/editar/{id}")
    public String editarJugador(@PathVariable("id") Integer id, Model model) {
        Jugador j = jugadorRepo.findById(id).orElse(null); // Busca al jugador por su ID
        model.addAttribute("jugador", j); 
        model.addAttribute("listaEquipos", equiposRepo.findAll()); // Carga equipos de nuevo
        
        File carpetaSkins = new File("src/main/resources/static/images/skins/");
        model.addAttribute("listaSkins", carpetaSkins.list()); // Carga skins para poder cambiarlas
        
        return "jugadores-form"; 
    }

    // Borrar jugador
    @GetMapping("/jugadores/borrar/{id}")
    public String borrarJugador(@PathVariable("id") Integer id) { //sirve para capturar el ID directamente de la URL (ej: /borrar/5).
        jugadorRepo.deleteById(id); // Elimina el registro por ID de la base de datos
        return "redirect:/jugadores";
    
    }
}