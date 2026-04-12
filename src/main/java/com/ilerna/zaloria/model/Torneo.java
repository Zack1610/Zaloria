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
@Table(name = "torneo")
public class Torneo {

    @Id //hibernaste necesita saber cual es la primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //este id generamela de forma auto- cada vez que recibas un nuevo registro
    private Integer id;

    private String nombre;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") 
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private Double premio;
    
    @Column(name = "max_equipos")
    private Integer maxEquipos;
    
    @ManyToOne//con esto le digo que un equipo puede ser ganador en muchos torneos
    private Equipos ganador;

    private String estado; // 'ABIERTO', 'EN CURSO', 'FINALIZADO'

    private String bannerUrl;

    public Torneo() {
    }
@ManyToMany
    @JoinTable(
      name = "inscripcion", 
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

    public Equipos getGanador() {
        return ganador;
    }

    public void setGanador(Equipos ganador) {
        this.ganador = ganador;
    }
    public String getBannerUrl() {
    return bannerUrl;
}

public void setBannerUrl(String bannerUrl) {
    this.bannerUrl = bannerUrl;
}
}