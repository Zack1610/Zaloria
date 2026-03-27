/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ilerna.zaloria.repository;
import com.ilerna.zaloria.model.Equipos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 *
 * @author Zack
 */

@Repository
public interface EquiposRepository extends JpaRepository<Equipos, Integer> {
    // Aquí ya tienes funciones como save(), findAll(), deleteById()
}