package com.ilerna.zaloria.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
   @Autowired
    private ResultadoRepository resultadoRepo;

    @Autowired
    private TorneoRepository torneoRepo;

    @Autowired
    private EquiposRepository equiposRepo;

    @Autowired
    private JugadorRepository jugadorRepo;

    @GetMapping("/admin/resultados/nuevo")
    public String mostrarSimulador(Model model) {
        // Solo mostramos torneos que no estén FINALIZADOS para simular
        model.addAttribute("torneos", torneoRepo.findAll());
        return "admin-simulador";
    }

    @PostMapping("/admin/resultados/simular")
    public String simularResultado(@RequestParam("torneoId") Integer torneoId) {
        Torneo torneo = torneoRepo.findById(torneoId).orElse(null);
        Random random = new Random();
        
        if (torneo != null) {
            Resultado res = new Resultado();
            res.setTorneo(torneo);
            
            // Elegir Equipo Ganador de la base de datos
            List<Equipos> todosLosEquipos = equiposRepo.findAll();
            if (!todosLosEquipos.isEmpty()) {
                res.setEquipoGanador(todosLosEquipos.get(random.nextInt(todosLosEquipos.size())));
            }

            // Elegir MVP aleatorio
            List<Jugador> todosLosJugadores = jugadorRepo.findAll();
            if (!todosLosJugadores.isEmpty()) {
                res.setMvp(todosLosJugadores.get(random.nextInt(todosLosJugadores.size())));
            }

            // Generar estadísticas aleatorias según tu Entidad Resultado
            res.setEliminaciones(random.nextInt(30) + 10); 
            res.setPuntosPosicion(random.nextInt(50) + 20); 
            res.setPuntosTotales(res.getEliminaciones() + res.getPuntosPosicion());
            res.setPuntuacion(res.getPuntosTotales() + " PTS");

            // Guardar resultado
            resultadoRepo.save(res);

            // Actualizar el estado del Torneo a FINALIZADO y asignar el ganador al torneo también
            torneo.setEstado("FINALIZADO");
            torneo.setGanador(res.getEquipoGanador()); // Para que aparezca en la tabla de torneos
            torneoRepo.save(torneo);
        }
        
        return "redirect:/admin/resultados";
    }
    
    @GetMapping("/admin/resultados")
public String listarResultados(Model model) {
    // Buscamos todos los resultados guardados en la base de datos
    model.addAttribute("listaResultados", resultadoRepo.findAll());
    return "admin-resultados-lista"; // Este será el nombre del nuevo HTML
}
}
