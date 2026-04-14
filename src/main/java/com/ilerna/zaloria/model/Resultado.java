/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ilerna.zaloria.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 *
 * @author Zack
 */
@Entity
@Table(name = "resultado")
public class Resultado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "id_torneo", unique = true)
    private Torneo torneo;

    @ManyToOne
    @JoinColumn(name = "equipo_ganador_id")
    private Equipos equipoGanador;

    @ManyToOne
    @JoinColumn(name = "id_mvp")
    private Jugador mvp;

    private Integer eliminaciones;
    private Integer puntosPosicion;
    private Integer puntosTotales;
    private String puntuacion;

    public Resultado() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Equipos getEquipoGanador() {
        return equipoGanador;
    }

    public void setEquipoGanador(Equipos equipoGanador) {
        this.equipoGanador = equipoGanador;
    }

    public Jugador getMvp() {
        return mvp;
    }

    public void setMvp(Jugador mvp) {
        this.mvp = mvp;
    }

    public Integer getEliminaciones() {
        return eliminaciones;
    }

    public void setEliminaciones(Integer eliminaciones) {
        this.eliminaciones = eliminaciones;
    }

    public Integer getPuntosPosicion() {
        return puntosPosicion;
    }

    public void setPuntosPosicion(Integer puntosPosicion) {
        this.puntosPosicion = puntosPosicion;
    }

    public Integer getPuntosTotales() {
        return puntosTotales;
    }

    public void setPuntosTotales(Integer puntosTotales) {
        this.puntosTotales = puntosTotales;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }
    
}
