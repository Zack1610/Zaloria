package com.ilerna.zaloria.model;

import jakarta.persistence.*;
/**
 *
 * @author Zack
 */
@Entity
@Table(name = "equipos") // Debe coincidir con el nombre en phpMyAdmin
public class Equipos{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String capitan;
    private String entrenadores;
    private String managers;

    // Constructor vacío (Obligatorio para que Spring funcione)
    public Equipos() {
    }

    // Getters y Setters (Los "grifos" para leer y escribir datos)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCapitan() {
        return capitan;
    }

    public void setCapitan(String capitan) {
        this.capitan = capitan;
    }

    public String getEntrenadores() {
        return entrenadores;
    }

    public void setEntrenadores(String entrenadores) {
        this.entrenadores = entrenadores;
    }

    public String getManagers() {
        return managers;
    }

    public void setManagers(String managers) {
        this.managers = managers;
    }
}