package com.ilerna.zaloria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
// 1. Ahora la raíz "/" muestra la web de noticias/pública
    @GetMapping("/")
    public String inicioPublico() {
        return "inicio-publico"; 
    }

    // 2. Creamos una nueva ruta para el panel de gestión
    // (A esta es a la que entrarás con el botón de "Acceso Administrador")
    @GetMapping("/admin")
    public String panelAdmin() {
        return "index"; // Tu archivo actual con los botones de gestión
    }
}