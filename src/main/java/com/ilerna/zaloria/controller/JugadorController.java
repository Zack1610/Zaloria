/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ilerna.zaloria.controller;
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
        model.addAttribute("listaJugadores", jugadorRepo.findAll());
        return "jugadores-lista";
    }

    // Formulario para nuevo jugador (Con selector de skins)
    @GetMapping("/jugadores/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("jugador", new Jugador());
        model.addAttribute("listaEquipos", equiposRepo.findAll());
        
        // Leer archivos de la carpeta skins
        File carpetaSkins = new File("src/main/resources/static/images/skins/");
        model.addAttribute("listaSkins", carpetaSkins.list());
        
        return "jugadores-form";
    }

    @PostMapping("/jugadores/guardar")
public String guardarJugador(Jugador jugador, @RequestParam(value="solicitudId", required=false) Integer solicitudId) {
    // 1. Guardamos el nuevo jugador oficial
    jugadorRepo.save(jugador);
    
    // 2. Si venía de una solicitud, cambiamos su estado a ACEPTADA
    if (solicitudId != null) {
        solicitudRepo.findById(solicitudId).ifPresent(s -> {
            s.setEstado("ACEPTADA"); // Aquí es donde cambia en la base de datos
            solicitudRepo.save(s);
        });
    }
    
    // Redirigimos a la lista de jugadores para ver el nuevo fichaje
    return "redirect:/jugadores";
}

    // EDITAR JUGADOR (También necesita cargar la lista de skins)
    @GetMapping("/jugadores/editar/{id}")
    public String editarJugador(@PathVariable("id") Integer id, Model model) {
        Jugador j = jugadorRepo.findById(id).orElse(null);
        model.addAttribute("jugador", j);
        model.addAttribute("listaEquipos", equiposRepo.findAll());
        
        // Cargar skins también aquí para poder cambiarlas al editar
        File carpetaSkins = new File("src/main/resources/static/images/skins/");
        model.addAttribute("listaSkins", carpetaSkins.list());
        
        return "jugadores-form"; 
    }

    // BORRAR JUGADOR
    @GetMapping("/jugadores/borrar/{id}")
    public String borrarJugador(@PathVariable("id") Integer id) {
        jugadorRepo.deleteById(id);
        return "redirect:/jugadores";
    }
}

