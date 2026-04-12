package com.ilerna.zaloria.model;

import jakarta.persistence.*;
/**
 *
 * @author Zack
 */
@Entity
@Table(name = "jugador")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nickname;
    private String email;

    @Column(name = "skin_url") // El nombre exacto de la columna SQL
    private String skinUrl;

    // RELACIÓN: Muchos jugadores pertenecen a UN equipo
    @ManyToOne
    @JoinColumn(name = "id_equipo") // Esta es la FK de tu tabla MySQL
    private Equipos equipo;

    public Jugador() {
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Equipos getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipos equipo) {
        this.equipo = equipo;
    }

    public String getSkinUrl() {
        return skinUrl;
    }

    public void setSkinUrl(String skinUrl) {
        this.skinUrl = skinUrl;
    }
}