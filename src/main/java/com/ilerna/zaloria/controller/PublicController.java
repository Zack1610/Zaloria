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
        // Obtenemos todos los resultados para mostrarlos en el muro de la fama
        // Traemos todos los resultados para la página de gloria
        model.addAttribute("listaResultados", resultadoRepo.findAll());
        // 2. El ranking acumulado (lo nuevo)
        model.addAttribute("ranking", resultadoRepo.obtenerRankingGlobal());
        return "resultados-publicos";
    }
}
