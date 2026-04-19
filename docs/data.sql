use zaloria;
-- insertar equipos
INSERT INTO equipo (nombre, capitan, entrenador, logo_url, managers) VALUES
('Zelderos', 'Link', 'Daruk', 'zelderos.png', 'cocacola'),
('PyroVex', 'Juanito', 'Zoro', 'equipo-pyrovex.png', 'cris'),
('Clan Kaedehara', 'Justinciero', 'Maestro roshi', 'equipo-kaedehara.png', 'radeon'),
('Clan Stromshroud', 'Luffy', 'Mickey', 'equipo-Clan-stromshroud.png', 'juanrtx'),
('Toon Trouble', 'Cuphead', 'Mugman', 'equipo-toon_trouble.png', 'snorlax');

-- insertar jugadores
INSERT INTO jugador (nickname, email, skin_url, id_equipo) VALUES
('Link', 'zaloria@gmail.com', 'miku.skin.jpg', 1),
('Juanito', 'juanitokawaii@gmail.com', 'chunlee.skin.jpg', 2),
('Justinciero', 'esaac1234@gmail.com', 'toga.skin.jpg', 3),
('machamp', 'luis@example.com', 'bakugo.skin.jpg', 2),
('Zacarias', 'zacarias123@gmail.com', 'rubius.skin.jpg', 1),
('Cuphead', 'nodeals@gmail.com', 'maiusculo.skin.jpg', 5),
('Luffy', 'merry@hotmaail.com', 'deku.skin.jpg', 4);

-- insertar torneos
INSERT INTO torneo (nombre, fecha, premio, max_equipos, estado, banner_url, ganador_id) VALUES
('Impact Cop', '2026-04-15 02:40:00', 15000, 5, 'FINALIZADO', 'impact-cop.png', 1),
('Copa Daiku', '2026-04-16 02:43:00', 500, 4, 'FINALIZADO', 'copa-daiku.png', 4),
('Copa no deal', '2026-04-16 19:08:00', 500, 4, 'FINALIZADO', 'copa-no-deal.png', 2),
('Terraclash Cup', '2026-04-18 15:53:00', 500, 4, 'FINALIZADO', 'copa-terraclash.png', 4);

-- Insertar Inscripciones (Relación Muchos a Muchos)
INSERT INTO inscripcion (torneo_id, equipo_id) VALUES
(4, 1), (4, 2), (4, 3), (4, 4), (4, 5), -- Todos en Impact Cop
(5, 2), (5, 3), (5, 4), (5, 5);         -- Algunos en Copa Daiku

-- Insertar Resultados (Relación 1:1 con Torneo)
INSERT INTO resultado (id_torneo, equipo_ganador_id, id_mvp, eliminaciones, puntos_posicion, puntos_totales, puntuacion) VALUES
(4, 1, 8, 37, 30, 67, '67 PTS'),
(5, 4, 3, 15, 49, 64, '64 PTS'),
(6, 2, 2, 29, 52, 81, '81 PTS'),
(7, 4, 6, 36, 59, 95, '95 PTS');

-- Insertar Solicitudes
INSERT INTO solicitudes (nombre, nickname, email, fecha_creacion, motivo, estado) VALUES
('dani', 'machamp', 'luis@example.com', '2026-04-14 19:47:25', 'el premio suena bien', 'ACEPTADA'),
('daruk', 'darukkk', 'daruk@gmail.com', '2026-04-14 19:50:14', 'porque si', 'RECHAZADA'),
('juancarlos', 'campero', 'juancarlos@example.com', '2026-04-15 18:34:43', 'me interesa el premio', 'PENDIENTE');

--Ver el Ranking de Equipos (para el hall de la fama):
SELECT e.nombre, COUNT(r.id) as copas 
FROM equipo e 
JOIN resultado r ON e.id = r.equipo_ganador_id 
GROUP BY e.id 
ORDER BY copas DESC;

-- Saber qué equipos están inscritos en un torneo específico (ej: el ID 4):
SELECT e.nombre 
FROM equipo e 
JOIN inscripcion i ON e.id = i.equipo_id 
WHERE i.torneo_id = 4;

-- Ver solicitudes pendientes de revisar:
SELECT * FROM solicitudes WHERE estado = 'PENDIENTE';

-- Borramos las tablas con dependencias (las que tienen FK)
DELETE FROM resultado;
DELETE FROM inscripcion;
DELETE FROM jugador;
DELETE FROM solicitudes;

-- Borramos los torneos (ahora que no tienen resultados)
DELETE FROM torneo;

-- Borramos los equipos (ahora que no tienen jugadores ni inscripciones)
DELETE FROM equipo;

-- OPCIONAL: Si quieres que los IDs vuelvan a empezar desde 1
ALTER TABLE resultado AUTO_INCREMENT = 1;
ALTER TABLE jugador AUTO_INCREMENT = 1;
ALTER TABLE torneo AUTO_INCREMENT = 1;
ALTER TABLE equipo AUTO_INCREMENT = 1;
ALTER TABLE solicitudes AUTO_INCREMENT = 1;