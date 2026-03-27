package com.ilerna.zaloria.model;

import jakarta.persistence.*;
import java.util.Date;
/**
 *
 * @author Zack
 */
@Entity
@Table(name = "torneos")
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private Double premio;
    
    @Column(name = "max_equipos")
    private Integer maxEquipos;

    private String estado; // 'ABIERTO', 'EN CURSO', 'FINALIZADO'

    public Torneo() {
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Double getPremio() { return premio; }
    public void setPremio(Double premio) { this.premio = premio; }

    public Integer getMaxEquipos() { return maxEquipos; }
    public void setMaxEquipos(Integer maxEquipos) { this.maxEquipos = maxEquipos; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}