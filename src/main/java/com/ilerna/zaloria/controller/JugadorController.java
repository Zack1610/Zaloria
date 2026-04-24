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
import java.util.List;

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
        model.addAttribute("listaJugadores", jugadorRepo.findAll()); 
        return "jugadores-lista"; 
    }

    // Formulario para nuevo jugador
    @GetMapping("/jugadores/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("jugador", new Jugador());
        
        // FILTRO: Solo equipos con cupo disponible
        List<Equipos> equiposConCupo = equiposRepo.findAll().stream()
                .filter(e -> {
                    long actuales = jugadorRepo.countByEquipo(e);
                    int maximo = (e.getMaxJugadores() != null) ? e.getMaxJugadores() : 5;
                    return actuales < maximo;
                })
                .toList();
        
        model.addAttribute("listaEquipos", equiposConCupo); 
        
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
        Jugador j = jugadorRepo.findById(id).orElse(null);
        model.addAttribute("jugador", j); 

        // FILTRO: Equipos con cupo O el equipo actual del jugador que estamos editando
        List<Equipos> equiposDisponibles = equiposRepo.findAll().stream()
                .filter(e -> {
                    long actuales = jugadorRepo.countByEquipo(e);
                    int maximo = (e.getMaxJugadores() != null) ? e.getMaxJugadores() : 5;
                    // El equipo es válido si hay sitio O si es el equipo que ya tiene asignado el jugador
                    return actuales < maximo || (j != null && j.getEquipo() != null && e.getId().equals(j.getEquipo().getId()));
                })
                .toList();

        model.addAttribute("listaEquipos", equiposDisponibles); 
        
        File carpetaSkins = new File("src/main/resources/static/images/skins/");
        model.addAttribute("listaSkins", carpetaSkins.list()); 
        
        return "jugadores-form"; 
    }

    // Borrar jugador
    @GetMapping("/jugadores/borrar/{id}")
    public String borrarJugador(@PathVariable("id") Integer id) {
        jugadorRepo.deleteById(id);
        return "redirect:/jugadores";
    }
}