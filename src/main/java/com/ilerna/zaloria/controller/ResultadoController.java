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
public String simularResultado(@RequestParam("torneoId") Integer torneoId, RedirectAttributes redirectAttributes) {
    // 1. Buscamos el torneo
    Torneo torneo = torneoRepo.findById(torneoId).orElse(null);
    Random random = new Random();

    if (torneo != null) {
        // 2. SEGURIDAD: Verificar si hay al menos 2 equipos inscritos
        if (torneo.getEquipos() == null || torneo.getEquipos().size() < 2) {
            redirectAttributes.addFlashAttribute("error", "⚠️ No se puede simular: El torneo necesita al menos 2 equipos inscritos.");
            return "redirect:/admin/resultados/nuevo";
        }

        // 3. Crear el objeto Resultado
        Resultado res = new Resultado();
        res.setTorneo(torneo);
        
        // 4. Elegir Equipo Ganador de la lista de inscritos (¡No de todos los equipos de la DB!)
        List<Equipos> participantes = torneo.getEquipos();
        res.setEquipoGanador(participantes.get(random.nextInt(participantes.size())));

        // 5. Elegir MVP aleatorio (de la base de datos general)
        List<Jugador> todosLosJugadores = jugadorRepo.findAll();
        if (!todosLosJugadores.isEmpty()) {
            res.setMvp(todosLosJugadores.get(random.nextInt(todosLosJugadores.size())));
        }

        // 6. Generar estadísticas aleatorias
        res.setEliminaciones(random.nextInt(30) + 10); 
        res.setPuntosPosicion(random.nextInt(50) + 20); 
        res.setPuntosTotales(res.getEliminaciones() + res.getPuntosPosicion());
        res.setPuntuacion(res.getPuntosTotales() + " PTS");

        // 7. Guardar resultado
        resultadoRepo.save(res);

        // 8. Actualizar el estado del Torneo a FINALIZADO y ponerle su ganador
        torneo.setEstado("FINALIZADO");
        torneo.setGanador(res.getEquipoGanador());
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
