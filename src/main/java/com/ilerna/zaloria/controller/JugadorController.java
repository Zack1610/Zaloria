/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ilerna.zaloria.controller;
import com.ilerna.zaloria.model.Jugador;
import com.ilerna.zaloria.repository.EquiposRepository;
import com.ilerna.zaloria.repository.JugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
/**
 *
 * @author Zack
 */
@Controller
public class JugadorController {
    @Autowired
    private JugadorRepository jugadorRepo;

    @Autowired
    private EquiposRepository equiposRepo;

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
        // Pasamos la lista de equipos para el desplegable (Select)
        model.addAttribute("listaEquipos", equiposRepo.findAll());
        return "jugadores-form";
    }

    @PostMapping("/jugadores/guardar")
    public String guardarJugador(Jugador jugador) {
        jugadorRepo.save(jugador);
        return "redirect:/jugadores";
    }
   // 1. EDITAR JUGADOR (Carga el formulario con los datos actuales)
@GetMapping("/jugadores/editar/{id}")
public String editarJugador(@PathVariable("id") Integer id, Model model) {
    Jugador j = jugadorRepo.findById(id).orElse(null);
    model.addAttribute("jugador", j);
    model.addAttribute("listaEquipos", equiposRepo.findAll());
    return "jugadores-form"; // Usamos el mismo formulario que para crear
}

// 2. BORRAR JUGADOR
@GetMapping("/jugadores/borrar/{id}")
public String borrarJugador(@PathVariable("id") Integer id) {
    jugadorRepo.deleteById(id);
    return "redirect:/jugadores";
}
}

