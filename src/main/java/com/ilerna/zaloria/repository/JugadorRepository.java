/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ilerna.zaloria.repository;

import com.ilerna.zaloria.model.Equipos;
import com.ilerna.zaloria.model.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 *
 * @author Zack
 */

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Integer> {
    // Spring creará la consulta SQL automáticamente: SELECT COUNT(*) FROM jugador WHERE equipo_id = ?
    long countByEquipo(Equipos equipo);
}