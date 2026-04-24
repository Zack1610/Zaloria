package com.ilerna.zaloria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import com.ilerna.zaloria.repository.ResultadoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class PublicController {

    @Autowired
    private ResultadoRepository resultadoRepo;

    // Ruta principal para que los fans vean los resultados
    @GetMapping("/resultados-publicos")
    public String verResultadosPublicos(Model model) {
       // 1. Enviamos todos los torneos finalizados para las tarjetas de historial
        model.addAttribute("listaResultados", resultadoRepo.findAll());
        
        // 2. Enviamos el Ranking de Equipos basado en copas ganadas (Usa la consulta JPQL personalizada)
        model.addAttribute("ranking", resultadoRepo.obtenerRankingGlobal());
        
        // 3. Enviamos el Ranking de MVPs basado en eliminaciones acumuladas
        model.addAttribute("rankingMvp", resultadoRepo.obtenerRankingMvp());
        
        return "resultados-publicos";
    }
}