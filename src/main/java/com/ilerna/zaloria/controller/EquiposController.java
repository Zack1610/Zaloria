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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.File;

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

    // 2. Mostrar el formulario para añadir un equipo (Con selector de logos)
    @GetMapping("/equipos/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("equipo", new Equipos());
        
        // Leer archivos de la carpeta logos
        File carpetaLogos = new File("src/main/resources/static/images/logos/");
        String[] listaLogos = carpetaLogos.list();
        model.addAttribute("listaLogos", listaLogos);
        
        return "equipos-form"; 
    }

    // 3. Guardar el equipo enviado desde el formulario
    @PostMapping("/equipos/guardar")
    public String guardarEquipo(Equipos equipo) {
        equiposRepo.save(equipo);
        return "redirect:/equipos"; 
    }

// 1. MÉTODO PARA EDITAR: Busca el equipo y abre el formulario
@GetMapping("/equipos/editar/{id}")
public String mostrarFormularioEditar(@PathVariable("id") Integer id, Model model) { // CAMBIADO A Integer
    Equipos equipos = equiposRepo.findById(id).orElse(null); 
    
    if (equipos != null) {
        model.addAttribute("equipo", equipos);
        File carpetaLogos = new File("src/main/resources/static/images/logos/");
        model.addAttribute("listaLogos", carpetaLogos.list());
        return "equipos-form"; 
    }
    return "redirect:/equipos";
}

// 2. MÉTODO PARA ELIMINAR
@GetMapping("/equipos/eliminar/{id}")
public String eliminarEquipo(@PathVariable("id") Integer id) { 
    try {
        equiposRepo.deleteById(id); 
    } catch (Exception e) {
        System.out.println("Error al eliminar: El equipo tiene jugadores vinculados."); 
        return "redirect:/equipos?error=true";
    }
    return "redirect:/equipos";
    }
}
