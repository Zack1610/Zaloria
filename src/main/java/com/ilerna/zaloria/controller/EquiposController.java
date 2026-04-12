/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ilerna.zaloria.controller;
import com.ilerna.zaloria.model.Equipos;
import com.ilerna.zaloria.repository.EquiposRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
/**
 *
 * @author Zack
 */
@Controller
public class EquiposController {
    @Autowired
    private EquiposRepository equiposRepo;

    // 1. Ver la lista de equipos
    @GetMapping("/equipos")
    public String listarEquipos(Model model) {
        List<Equipos> lista = equiposRepo.findAll();
        model.addAttribute("listaEquipos", lista);
        return "equipos-lista"; 
    }

    // 2. Mostrar el formulario para añadir un equipo
    @GetMapping("/equipos/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("equipo", new Equipos());
        return "equipos-form"; 
    }

    // 3. Guardar el equipo enviado desde el formulario
    @PostMapping("/equipos/guardar")
    public String guardarEquipo(Equipos equipo) {
        equiposRepo.save(equipo);
        return "redirect:/equipos"; 
    }
}

