package com.ilerna.zaloria.repository;

import com.ilerna.zaloria.model.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Importante añadir este import
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Integer> {

    // La consulta va AQUÍ, dentro del repositorio
    @Query("SELECT r.equipoGanador.nombre AS nombreEquipo, r.equipoGanador.logoUrl AS logoUrl, COUNT(r) AS victorias " +
           "FROM Resultado r " +
           "GROUP BY r.equipoGanador.id, r.equipoGanador.nombre, r.equipoGanador.logoUrl " +
           "ORDER BY victorias DESC")
    List<RankingDTO> obtenerRankingGlobal();
}

/** * Esta interfaz es un "molde" para los datos del ranking.
 * Al no llevar la palabra 'public', puede vivir en este mismo archivo 
 * justo debajo del repositorio principal.
 */
interface RankingDTO {
    String getNombreEquipo();
    String getLogoUrl();
    Long getVictorias();
}