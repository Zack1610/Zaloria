package com.ilerna.zaloria.repository;

import com.ilerna.zaloria.model.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Importante añadir este import
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Integer> {

    // La consulta JPQL para obtener el ranking global de equipos ganadores
    @Query("SELECT r.equipoGanador.nombre AS nombreEquipo, r.equipoGanador.logoUrl AS logoUrl, COUNT(r) AS victorias " +
           "FROM Resultado r " +
           "GROUP BY r.equipoGanador.id, r.equipoGanador.nombre, r.equipoGanador.logoUrl " +
           "ORDER BY victorias DESC")
    List<RankingDTO> obtenerRankingGlobal();

    // Consulta para el Ranking de MVPs basado en Kills
@Query("SELECT r.mvp.nickname AS nickname, r.mvp.skinUrl AS skinUrl, SUM(r.eliminaciones) AS totalKills " +
       "FROM Resultado r " +
       "GROUP BY r.mvp.id, r.mvp.nickname, r.mvp.skinUrl " +
       "ORDER BY totalKills DESC")
       List<MvpRankingDTO> obtenerRankingMvp();
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

interface MvpRankingDTO {
    String getNickname();
    String getSkinUrl();
    Long getTotalKills(); // Cambiamos el nombre para que sea más claro
}