/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ilerna.zaloria.controller;

import com.ilerna.zaloria.model.Equipos;
import org.springframework.stereotype.Controller;

import com.ilerna.zaloria.model.Torneo;
import com.ilerna.zaloria.repository.EquiposRepository;
import com.ilerna.zaloria.repository.TorneoRepository;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 *
 * @author Zack
 */
@Controller
public class TorneoController {
    
    @Autowired // Inyecta el repositorio para interactuar con la tabla 'torneo'
    private TorneoRepository torneoRepo;

    @Autowired // Inyecta el repositorio de equipos para las inscripciones y ganadores
    private EquiposRepository equipoRepo;
    
    // LISTAR TODOS LOS TORNEOS
    @GetMapping("/torneos")
    public String listarTorneos(Model model) {
        // Recupera todos los torneos y los envía a la vista
        model.addAttribute("listaTorneos", torneoRepo.findAll());
        return "torneos-lista";
    }

    // MOSTRAR FORMULARIO DE NUEVO TORNEO
  @GetMapping("/torneos/nuevo")
public String mostrarFormulario(Model model) {
    model.addAttribute("torneo", new Torneo());
    
    // 1. Ruta a la carpeta física
    File carpeta = new File("src/main/resources/static/images/banners/");
    
    // 2. Obtener lista de archivos
    String[] archivos = carpeta.list();
    
    // 3. Seguridad: Si la carpeta no existe o está vacía, enviamos lista vacía
    if (archivos == null) {
        archivos = new String[0];
    }
    
    // 4. EL NOMBRE CLAVE: debe ser "listaBanners"
    model.addAttribute("listaBanners", archivos);
    
    return "torneos-form";
}

    // GUARDAR TORNEO (Creación o Edición)
    @PostMapping("/torneos/guardar")
    public String guardarTorneo(@ModelAttribute("torneo") Torneo torneo) {
        // Guarda el objeto en la DB (Hibernate decide si es INSERT o UPDATE por el ID)
        torneoRepo.save(torneo);
        return "redirect:/torneos";
    }

    // gestion de inscripciones manytomany

    // MOSTRAR PANTALLA DE INSCRIPCIÓN
    @GetMapping("/torneos/inscribir/{id}")
    public String mostrarInscripcion(@PathVariable("id") Integer id, Model model) {
        Torneo t = torneoRepo.findById(id).orElse(null);
        model.addAttribute("torneo", t);
        // Enviamos todos los equipos para que el admin elija cuál inscribir
        model.addAttribute("listaEquipos", equipoRepo.findAll());
        return "torneos-inscripcion";
    }

    //PROCESAR INSCRIPCIÓN DE EQUIPO
    @PostMapping("/torneos/inscribir/guardar")
    public String guardarInscripcion(@RequestParam("torneoId") Integer torneoId, 
                                     @RequestParam("equipoId") Integer equipoId) {
        Torneo t = torneoRepo.findById(torneoId).orElse(null);
        Equipos e = equipoRepo.findById(equipoId).orElse(null);
        
        if (t != null && e != null) {
            // Evita duplicados: solo añade si el equipo no está ya en la lista
            if (!t.getEquipos().contains(e)) {
                t.getEquipos().add(e);
                torneoRepo.save(t); // Actualiza la tabla intermedia 'inscripcion'
            }
        }
        return "redirect:/torneos";
    }

    // VER EQUIPOS PARTICIPANTES
    @GetMapping("/torneos/ver/{id}")
    public String verParticipantes(@PathVariable("id") Integer id, Model model) {
        Torneo t = torneoRepo.findById(id).orElse(null);
        if (t != null) {
            model.addAttribute("torneo", t);
            return "torneos-participantes"; 
        }
        return "redirect:/torneos";
    }

    // DESVINCULAR EQUIPO DEL TORNEO
    @PostMapping("/torneos/desvincular")
    public String desvincularEquipo(@RequestParam("torneoId") Integer torneoId, 
                                     @RequestParam("equipoId") Integer equipoId) {
        Torneo t = torneoRepo.findById(torneoId).orElse(null);
        Equipos e = equipoRepo.findById(equipoId).orElse(null);
        
        if (t != null && e != null) {
            t.getEquipos().remove(e); // Elimina la relación en Java
            torneoRepo.save(t);       // Spring borra la fila en la DB
        }
        return "redirect:/torneos/ver/" + torneoId;
    }

    // --- CIERRE Y RESULTADOS ---

    // MOSTRAR PANTALLA PARA FINALIZAR TORNEO
    @GetMapping("/torneos/finalizar/{id}")
    public String irAFinalizar(@PathVariable("id") Integer id, Model model) {
        Torneo t = torneoRepo.findById(id).orElse(null);
        if (t != null) {
            model.addAttribute("torneo", t);
            // Solo pasamos los equipos que realmente están inscritos para elegir al ganador
            model.addAttribute("participantes", t.getEquipos());
            return "torneos-finalizar"; 
        }
        return "redirect:/torneos";
    }

    // GUARDAR GANADOR Y CERRAR TORNEO
    @PostMapping("/torneos/guardar-ganador")
    public String guardarGanador(@RequestParam("torneoId") Integer torneoId, 
                                 @RequestParam("equipoId") Integer equipoId) {
        Torneo t = torneoRepo.findById(torneoId).orElse(null);
        Equipos e = equipoRepo.findById(equipoId).orElse(null);
        
        if (t != null && e != null) {
            t.setGanador(e);          // Asigna el equipo ganador (FK)
            t.setEstado("FINALIZADO"); // Cambia el estado automáticamente
            torneoRepo.save(t);
        }
        return "redirect:/torneos";
    }

    // ELIMINAR TORNEO
    @GetMapping("/torneos/eliminar/{id}")
    public String eliminarTorneo(@PathVariable("id") Integer id) {
        Torneo t = torneoRepo.findById(id).orElse(null);
        if (t != null) {
            // Limpia las inscripciones antes de borrar para evitar errores de integridad
            t.getEquipos().clear(); 
            torneoRepo.delete(t);
        }
        return "redirect:/torneos";
    }
}