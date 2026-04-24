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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    
    // Ruta a la carpeta física
    File carpeta = new File("src/main/resources/static/images/banners/");
    
    // Obtener lista de archivos
    String[] archivos = carpeta.list();
    
    // Seguridad: Si la carpeta no existe o está vacía, enviamos lista vacía
    if (archivos == null) {
        archivos = new String[0];
    }
    
    // EL NOMBRE CLAVE: debe ser "listaBanners"
    model.addAttribute("listaBanners", archivos);
    
    return "torneos-form";
}

    // GUARDAR TORNEO (Creación o Edición)
    @PostMapping("/torneos/guardar")
public String guardarTorneo(@ModelAttribute("torneo") Torneo torneo, RedirectAttributes ra) {
    
    // SEGURIDAD: Usamos getFecha() porque así se llama tu atributo en la clase Torneo
    if (torneo.getFecha() != null && torneo.getFecha().before(new java.util.Date())) {
        ra.addFlashAttribute("errorTorneo", "⚠️ No puedes programar un torneo en una fecha que ya pasó.");
        return "redirect:/torneos/nuevo"; 
    }

    // --- El resto de tu lógica de edición/guardado sigue igual ---
    if (torneo.getId() != null) {
        Torneo torneoExistente = torneoRepo.findById(torneo.getId()).orElse(null);
        if (torneoExistente != null) {
            if (torneo.getBannerUrl() == null || torneo.getBannerUrl().isEmpty()) {
                torneo.setBannerUrl(torneoExistente.getBannerUrl());
            }
            if (torneo.getEstado() == null) {
                torneo.setEstado(torneoExistente.getEstado());
            }
        }
    } else {
        torneo.setEstado("ABIERTO");
    }

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
  // MÉTODO PARA INICIAR EL TORNEO
@PostMapping("/admin/torneos/iniciar")
public String iniciarTorneo(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
    // Busca el torneo por su ID en la base de datos
    Torneo torneo = torneoRepo.findById(id).orElse(null);
    
    if (torneo != null) {
        // ESCUDO DE SEGURIDAD: Comprueba que la lista de equipos inscritos tenga al menos 2 elementos
        if (torneo.getEquipos() == null || torneo.getEquipos().size() < 2) {
            // Envía un mensaje de error que aparecerá solo una vez en la pantalla (FlashAttribute)
            redirectAttributes.addFlashAttribute("errorTorneo", "⚠️ No puedes iniciar '" + torneo.getNombre() + "' sin al menos 2 equipos inscritos.");
            return "redirect:/torneos"; // Detiene el proceso y recarga la lista
        }
        
        // Cambia el estado a 'EN CURSO' para que el simulador pueda detectarlo
        torneo.setEstado("EN CURSO");
        torneoRepo.save(torneo); // Guarda el cambio de estado en MySQL
    }
    return "redirect:/torneos";
}

// MÉTODO PARA EDITAR UN TORNEO EXISTENTE
@GetMapping("/torneos/editar/{id}")
public String mostrarFormularioEditar(@PathVariable("id") Integer id, Model model) {
    // @PathVariable: Captura el ID directamente desde la URL (ej: /editar/5)
    Torneo torneo = torneoRepo.findById(id).orElse(null);
    
    if (torneo != null) {
        model.addAttribute("torneo", torneo); // Envía los datos actuales del torneo al formulario
        model.addAttribute("titulo", "Editar Torneo: " + torneo.getNombre());
        
        // Lógica de Banners: Lee la carpeta física para que el usuario pueda cambiar la imagen
        File carpeta = new File("src/main/resources/static/images/banners/");
        String[] archivos = carpeta.list();
        if (archivos == null) { archivos = new String[0]; }
        model.addAttribute("listaBanners", archivos); // Carga el selector de imágenes

        return "torneos-form"; // Reutiliza el mismo HTML de creación para editar
    }
    return "redirect:/torneos";
 }
}