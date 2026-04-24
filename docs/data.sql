-- ==========================================================
-- SCRIPT MAESTRO DE GESTIÓN ZALORIA (MODO JDBC/SQL PURO)
-- ==========================================================
CREATE DATABASE IF NOT EXISTS zaloria;
USE zaloria;

-- Limpieza total para ejecución limpia
-- 1. Desactivamos las restricciones
SET FOREIGN_KEY_CHECKS = 0;

-- 2. Usamos DELETE en lugar de TRUNCATE para evitar el error #1701
DELETE FROM resultado;
DELETE FROM inscripcion;
DELETE FROM jugador;
DELETE FROM solicitudes;
DELETE FROM torneo;
DELETE FROM equipo;

-- 3. IMPORTANTE: Reiniciamos los contadores (IDs) para que empiecen de 1
ALTER TABLE resultado AUTO_INCREMENT = 1;
ALTER TABLE inscripcion AUTO_INCREMENT = 1;
ALTER TABLE jugador AUTO_INCREMENT = 1;
ALTER TABLE solicitudes AUTO_INCREMENT = 1;
ALTER TABLE torneo AUTO_INCREMENT = 1;
ALTER TABLE equipo AUTO_INCREMENT = 1;

-- 4. Reactivamos las restricciones
SET FOREIGN_KEY_CHECKS = 1;

-- ==========================================================
-- 1. EQUIPOS (Máximo 4 jugadores por diseño de base de datos)
-- ==========================================================
INSERT INTO equipo (id, nombre, capitan, entrenador, logo_url, managers, max_jugadores) VALUES
(1, 'Zelderos', 'Link', 'Daruk', 'zelderos.png', 'cocacola', 4),
(2, 'PyroVex', 'Juanito', 'Zoro', 'equipo-pyrovex.png', 'cris', 4),
(3, 'Clan Kaedehara', 'Justinciero', 'Maestro roshi', 'equipo-kaedehara.png', 'radeon', 4),
(4, 'Clan Stromshroud', 'Luffy', 'Mickey', 'equipo-Clan-stromshroud.png', 'juanrtx', 4),
(5, 'Toon Trouble', 'Cuphead', 'Mugman', 'equipo-toon_trouble.png', 'snorlax', 4),
(6, 'FrosFang', 'Kasuta1610', 'Joyboy', 'equipo-FrosFang.png', 'Frozono', 4),
(7, 'Obsidian Legends', 'Hu tao', 'navia', 'equipo-obsidian-legends.png', 'zhongli', 4),
(8, 'Aqua Legends', 'machamp', 'Rojo', 'equipo-aqua-legends.png', 'brook', 4);



-- ==========================================================
-- 2. JUGADORES (Completando tope de 4 por equipo con tus imágenes)
-- ==========================================================
INSERT INTO jugador (nickname, email, skin_url, id_equipo) VALUES
-- Equipo 1: Zelderos (4/4)
('Link', 'zaloria@gmail.com', 'miku.skin.jpg', 1),
('Zacarias', 'zacarias123@gmail.com', 'bakugo.skin.jpg', 1),
('Ganyu_Fan', 'ganyu@example.com', 'bananin.skin.jpg', 1),
('Venti_Pro', 'venti@example.com', 'billie.skin.jpg', 1),

-- Equipo 2: PyroVex (4/4)
('Juanito', 'juanitokawaii@gmail.com', 'chunlee.skin.jpg', 2),
('machamp', 'luis@example.com', 'cuervo.skin.jpg', 2),
('Pyro_Master', 'pyro@example.com', 'deku.skin.jpg', 2),
('Diluc_Main', 'diluc@example.com', 'deriva.skin.jpg', 2),

-- Equipo 3: Clan Kaedehara (4/4)
('Justinciero', 'esaac1234@gmail.com', 'toga.skin.jpg', 3),
('Kazuha_God', 'kazu@example.com', 'eren.skin.jpg', 3),
('Inazuma_Ace', 'inazuma@example.com', 'gojo.skin.jpg', 3),
('Sword_King', 'sword@example.com', 'goku.skin.jpg', 3),

-- Equipo 4: Clan Stromshroud (4/4)
('Luffy', 'merry@hotmaail.com', 'hamburguesa.skin.jpg', 4),
('Zoro_Lost', 'zoro@example.com', 'joker.skin.jpg', 4),
('Nami_Navigator', 'nami@example.com', 'kugisaki.skin.jpg', 4),
('Sanji_Cook', 'sanji@example.com', 'leon.skin.jpg', 4),

-- Equipo 5: Toon Trouble (4/4)
('Cuphead', 'nodeals@gmail.com', 'maiusculo.skin.jpg', 5),
('Mugman_Pro', 'mug@example.com', 'ruby.skin.jpg', 5),
('Inkwell_Ace', 'ink@example.com', 'midas.skin.jpg', 5),
('Dice_Roller', 'dice@example.com', 'naruto.skin.jpg', 5),

('Kasuta1610', 'kasuta2020@gmail.com', 'ikonik.skin.jpg', 6),
('anemokazuha', 'mug@example.com', 'miku2.skin.jpg', 6),
('Ninja', 'ninja2026@example.com', 'ninja.skin.jpg', 6),
('Sabrina', 'carpenter@example.com', 'sabrina.skin.jpg', 6),

('Hu tao', 'fantasmita@gmail.com', 'lexa.skin.jpg', 7),
('scooby', 'galletas@example.com', 'scooby.skin.jpg', 7),
('rubius', 'wilson@example.com', 'rubius.skin.jpg', 7),
('sakura', 'sakura@example.com', 'sakura.skin.jpg', 7),

('machamp', 'machampp12@example.com', 'sasuke.skin.jpg', 8);

-- ==========================================================
-- 3. TORNEOS Y RESULTADOS (Historial)
-- ==========================================================
INSERT INTO torneo (id, nombre, fecha, premio, max_equipos, estado, banner_url, ganador_id) VALUES
(1, 'Impact Cop', '2026-04-15 02:40:00', 15000, 5, 'FINALIZADO', 'impact-cop.png', 1),
(2, 'Copa Daiku', '2026-04-16 02:43:00', 500, 4, 'FINALIZADO', 'copa-daiku.png', 4),
(3, 'Terraclash Cup', '2026-04-18 15:53:00', 500, 4, 'FINALIZADO', 'copa-terraclash.png', 4);

-- Resultados (Simulando estadísticas)
INSERT INTO resultado (id_torneo, equipo_ganador_id, id_mvp, eliminaciones, puntos_posicion, puntos_totales, puntuacion) VALUES
(1, 1, 1, 37, 30, 67, '67 PTS'),
(2, 4, 13, 15, 49, 64, '64 PTS'),
(3, 4, 14, 36, 59, 95, '95 PTS');

-- ==========================================================
-- 4. SOLICITUDES (Usando las imágenes sobrantes para nuevos candidatos)
-- ==========================================================
INSERT INTO solicitudes (nombre, nickname, email, fecha_creacion, motivo, estado) VALUES
('Dani', 'machamp', 'luis@example.com', '2026-04-14 19:47:25', 'El premio suena bien', 'ACEPTADA'),
('Daruk', 'darukkk', 'daruk@gmail.com', '2026-04-14 19:50:14', 'Porque si', 'RECHAZADA'),
('Juancarlos', 'campero', 'juancarlos@example.com', '2026-04-15 18:34:43', 'Me interesa el premio', 'PENDIENTE');
-- Candidatos con tus imágenes sobrantes
INSERT INTO solicitudes (nombre, nickname, email, fecha_creacion, motivo, estado) VALUES
('Esdeath_Fan', 'IceQueen', 'esdeath@example.com', NOW(), 'Quiero probar mi nivel en Zaloria', 'PENDIENTE'), 
('Raiden_Shogun', 'ElectroArchon', 'raiden@example.com', NOW(), 'Busco un equipo competitivo', 'PENDIENTE'); 

-- actualizar estado de una solicitud (ejemplo)
UPDATE solicitudes 
SET estado = 'ACEPTADA' 
WHERE nickname = 'IceQueen';

INSERT INTO jugador (nickname, email, skin_url, id_equipo) 
VALUES ('IceQueen', 'esdeath@example.com', 'pez.skin.jpg', 8);

-- ==========================================================
-- 5. CONSULTAS DE GESTIÓN (SIMULACIÓN DE REPORTES JDBC)
-- ==========================================================

-- Ver Ranking de Equipos por Copas
SELECT e.nombre, COUNT(r.id) as copas_ganadas 
FROM equipo e 
JOIN resultado r ON e.id = r.equipo_ganador_id 
GROUP BY e.id 
ORDER BY copas_ganadas DESC;

-- Verificar que todos los equipos estén al tope (4/4)
SELECT e.nombre, COUNT(j.id) as total_jugadores 
FROM equipo e 
LEFT JOIN jugador j ON e.id = j.id_equipo 
GROUP BY e.id;

-- Listar solicitudes activas para el Admin
SELECT * FROM solicitudes WHERE estado = 'ACEPTADA';
SELECT * FROM solicitudes WHERE estado = 'PENDIENTE';
SELECT * FROM solicitudes WHERE estado = 'RECHAZADA';

DEMOSTRACIÓN DE FUNCIONAMIENTO
-- ==========================================================

-- A. Mostrar todos los jugadores y sus equipos (JOIN)
SELECT j.nickname as 'Jugador', e.nombre as 'Equipo', j.skin_url as 'Skin' 
FROM jugador j 
INNER JOIN equipo e ON j.id_equipo = e.id;

-- B. Mostrar el Hall de la Fama (Ranking)
SELECT e.nombre as 'Equipo', COUNT(r.id) as 'Copas Ganadas' 
FROM equipo e 
JOIN resultado r ON e.id = r.equipo_ganador_id 
GROUP BY e.id 
ORDER BY 2 DESC;

-- C. Comprobar que los equipos están llenos (Gestión de Cupos)
SELECT e.nombre as 'Equipo', COUNT(j.id) as 'Total Jugadores' 
FROM equipo e 
LEFT JOIN jugador j ON e.id = j.id_equipo 
GROUP BY e.id;

-- D. Ver el estado de las solicitudes
SELECT nickname, email, estado FROM solicitudes;

-- ver las tablas por si se me olvida alguna
SHOW TABLES;
DESCRIBE nombre_de_la_tabla;
SELECT * FROM nombre_de_la_tabla; -- Ejemplo: SELECT * FROM jugador;
SELECT columna1, columna2 FROM nombre_de_la_tabla; -- Ejemplo: SELECT nickname, email FROM solicitudes;
SELECT COUNT(*) FROM jugador; -- Contar total de jugadores
SELECT * FROM nombre_de_la_tabla ORDER BY id DESC LIMIT 5; -- Ver los últimos 5 registros de una tabla
SELECT * FROM equipo\G -- Ver detalles de equipos en formato vertical