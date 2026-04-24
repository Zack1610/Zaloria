package com.ilerna.zaloria.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ilerna.zaloria.model.Equipos;
import com.ilerna.zaloria.model.Jugador;
import com.ilerna.zaloria.model.Resultado;
import com.ilerna.zaloria.model.Torneo;
import com.ilerna.zaloria.repository.EquiposRepository;
import com.ilerna.zaloria.repository.JugadorRepository;
import com.ilerna.zaloria.repository.ResultadoRepository;
import com.ilerna.zaloria.repository.TorneoRepository;
import org.springframework.ui.Model;
import java.io.File;
import java.util.List;


@Controller
public class ResultadoController {
   @Autowired private ResultadoRepository resultadoRepo; // Para guardar los datos finales
    @Autowired private TorneoRepository torneoRepo; // Para buscar y actualizar torneos
    @Autowired private EquiposRepository equiposRepo; // Acceso a la tabla equipos
    @Autowired private JugadorRepository jugadorRepo; // Acceso a la tabla jugadores

    @GetMapping("/admin/resultados/nuevo")
    public String mostrarSimulador(Model model) {
        // Carga la lista de torneos disponibles para ser simulados
        model.addAttribute("torneos", torneoRepo.findAll());
        return "admin-simulador";
    }

    @PostMapping("/admin/resultados/simular")
    public String simularResultado(@RequestParam("torneoId") Integer torneoId, RedirectAttributes redirectAttributes) {
        Torneo torneo = torneoRepo.findById(torneoId).orElse(null); // Busca el torneo por ID
        Random random = new Random(); // Clase para generar valores aleatorios

        if (torneo != null) {
            // ESCUDO DE SEGURIDAD: Valida que existan al menos 2 equipos para competir
            if (torneo.getEquipos() == null || torneo.getEquipos().size() < 2) {
                redirectAttributes.addFlashAttribute("error", "⚠️ No se puede simular: Faltan equipos.");
                return "redirect:/admin/resultados/nuevo"; // Detiene la ejecución si hay error
            }

            Resultado res = new Resultado(); // Crea el registro de estadísticas
            res.setTorneo(torneo);
            
            // ELIGE GANADOR: Solo de la lista de equipos inscritos en ese torneo
            List<Equipos> participantes = torneo.getEquipos();
            res.setEquipoGanador(participantes.get(random.nextInt(participantes.size())));

            // ELIGE MVP: Selecciona un jugador aleatorio de toda la base de datos
            List<Jugador> todosLosJugadores = jugadorRepo.findAll();
            if (!todosLosJugadores.isEmpty()) {
                res.setMvp(todosLosJugadores.get(random.nextInt(todosLosJugadores.size())));
            }

            // GENERACIÓN DE STATS: Calcula bajas, puntos y suma el total
            res.setEliminaciones(random.nextInt(30) + 10); 
            res.setPuntosPosicion(random.nextInt(50) + 20); 
            res.setPuntosTotales(res.getEliminaciones() + res.getPuntosPosicion());
            res.setPuntuacion(res.getPuntosTotales() + " PTS");

            resultadoRepo.save(res); // Guarda las estadísticas en la base de datos

            // ACTUALIZA TORNEO: Cambia el estado a FINALIZADO y asigna al campeón
            torneo.setEstado("FINALIZADO");
            torneo.setGanador(res.getEquipoGanador());
            torneoRepo.save(torneo); // Registra el fin del torneo en la DB
        }
        return "redirect:/admin/resultados"; // Envía a la lista de resultados históricos
    }
    
    @GetMapping("/admin/resultados")
    public String listarResultados(Model model) {
        // Recupera todo el historial para mostrarlo en el panel de administrador
        model.addAttribute("listaResultados", resultadoRepo.findAll());
        return "admin-resultados-lista";
    }
}