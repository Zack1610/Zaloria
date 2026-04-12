/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ilerna.zaloria.controller;

import com.ilerna.zaloria.model.Equipos;
import org.springframework.stereotype.Controller;

import com.ilerna.zaloria.model.Torneo;
import com.ilerna.zaloria.repository.TorneoRepository;
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
@Controller
public class TorneoController {
    
    @Autowired
    private TorneoRepository torneoRepo;

    // 1. LISTAR TODOS LOS TORNEOS
    @GetMapping("/torneos")
    public String listarTorneos(Model model) {
        model.addAttribute("listaTorneos", torneoRepo.findAll());
        return "torneos-lista";
    }

    // 2. MOSTRAR FORMULARIO DE NUEVO TORNEO
    @GetMapping("/torneos/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("torneo", new Torneo());
        return "torneos-form";
    }

    // 3. GUARDAR EL TORNEO EN LA BASE DE DATOS
    @PostMapping("/torneos/guardar")
    public String guardarTorneo(Torneo torneo) {
        torneoRepo.save(torneo);
        return "redirect:/torneos";
    }
    @Autowired
private com.ilerna.zaloria.repository.EquiposRepository equipoRepo;

// 1. Mostrar pantalla de inscripción
@GetMapping("/torneos/inscribir/{id}")
public String mostrarInscripcion(@PathVariable("id") Integer id, Model model) {
    Torneo t = torneoRepo.findById(id).orElse(null);
    model.addAttribute("torneo", t);
    model.addAttribute("listaEquipos", equipoRepo.findAll()); // Para elegir cuál inscribir
    return "torneos-inscripcion";
}

// 2. Guardar la inscripción
@PostMapping("/torneos/inscribir/guardar")
public String guardarInscripcion(@RequestParam("torneoId") Integer torneoId, 
                                 @RequestParam("equipoId") Integer equipoId) {
    
    Torneo t = torneoRepo.findById(torneoId).orElse(null);
    com.ilerna.zaloria.model.Equipos e = equipoRepo.findById(equipoId).orElse(null);
    
    if (t != null && e != null) {
        //  Comprobamos si el equipo NO está ya inscrito
        if (!t.getEquipos().contains(e)) {
            t.getEquipos().add(e); //  Lo añadimos a la lista
            torneoRepo.save(t);     // Guardamos los cambios en la DB
        }
    }
    return "redirect:/torneos";
}
@GetMapping("/torneos/ver/{id}")
public String verParticipantes(@PathVariable("id") Integer id, Model model) {
    //  Buscamos el torneo por su ID
    Torneo t = torneoRepo.findById(id).orElse(null);
    
    if (t != null) {
        //  Metemos el torneo en la "maleta" (Model) para que el HTML lo use
        model.addAttribute("torneo", t);
        //  Abrimos el archivo torneos-participantes.html
        return "torneos-participantes"; 
    }
    
    // Si el torneo no existe, volvemos a la lista general
    return "redirect:/torneos";
}
@PostMapping("/torneos/desvincular")
public String desvincularEquipo(@RequestParam("torneoId") Integer torneoId, 
                                 @RequestParam("equipoId") Integer equipoId) {
    
    Torneo t = torneoRepo.findById(torneoId).orElse(null);
    com.ilerna.zaloria.model.Equipos e = equipoRepo.findById(equipoId).orElse(null);
    
    if (t != null && e != null) {
        // Quitamos el equipo de la lista del torneo
        t.getEquipos().remove(e);
        torneoRepo.save(t); // Hibernate borrará la fila en la tabla 'inscripciones'
    }
    
    return "redirect:/torneos/ver/" + torneoId;
}
@GetMapping("/torneos/finalizar/{id}")
public String irAFinalizar(@PathVariable("id") Integer id, Model model) {
    //  Buscamos el torneo en la base de datos
    Torneo t = torneoRepo.findById(id).orElse(null);
    
    if (t != null) {
        // Pasamos el torneo a la vista
        model.addAttribute("torneo", t);
        // Pasamos SOLO los equipos que se inscribieron en este torneo
        model.addAttribute("participantes", t.getEquipos());
        
        return "torneos-finalizar"; 
    }
    return "redirect:/torneos";
}
@PostMapping("/torneos/guardar-ganador")
public String guardarGanador(@RequestParam("torneoId") Integer torneoId, 
                             @RequestParam("equipoId") Integer equipoId) {
    
    // Buscamos el torneo y el equipo por sus IDs
    Torneo t = torneoRepo.findById(torneoId).orElse(null);
    Equipos e = equipoRepo.findById(equipoId).orElse(null);
    
    if (t != null && e != null) {
        // Le asignamos el ganador al torneo
        t.setGanador(e);
        // Opcional: Cambiamos el estado a FINALIZADO
        t.setEstado("FINALIZADO");
        
        // Guardamos los cambios en la base de datos
        torneoRepo.save(t);
    }

    //  Volvemos a la lista principal para ver el resultado
    return "redirect:/torneos";
}
@GetMapping("/torneos/eliminar/{id}")
public String eliminarTorneo(@PathVariable("id") Integer id) {
    // Buscamos el torneo para verificar que existe
    Torneo t = torneoRepo.findById(id).orElse(null);
    
    if (t != null) {
        // Limpiamos las relaciones (opcional pero recomendado)
        
        // Esto evita errores si el torneo tiene equipos inscritos
        t.getEquipos().clear(); 
        
        // Borramos el torneo definitivamente
        torneoRepo.delete(t);
    }
    // Recargamos la lista para ver que ya no está
    return "redirect:/torneos";
}

}