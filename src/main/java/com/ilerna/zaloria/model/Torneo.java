package com.ilerna.zaloria.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
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
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") // Esto es el traductor para el HTML
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private Double premio;
    
    @Column(name = "max_equipos")
    private Integer maxEquipos;

    private String estado; // 'ABIERTO', 'EN CURSO', 'FINALIZADO'

    public Torneo() {
    }
@ManyToMany
    @JoinTable(
      name = "inscripciones", 
      joinColumns = @JoinColumn(name = "torneo_id"), 
      inverseJoinColumns = @JoinColumn(name = "equipo_id"))
    private List<Equipos> equipos;

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
    
    public List<Equipos> getEquipos() { return equipos; }
    public void setEquipos(List<Equipos> equipos) { this.equipos = equipos; }
}