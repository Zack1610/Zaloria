use zaloria;

INSERT INTO Equipo (ID, nombre, capitan, entrenadores, managers, logo_url) VALUES 
(1, 'Zelderos', 'Link', 'Daruk', 'cocacola', 'imagen'),
(2, 'PyroVex', 'Juanito', 'Zoro', 'cris', 'imagen'),
(3, 'Clan Kaedehara', 'Justinciero', 'Maestro roshi', 'radeon', 'imagen'),
(4, 'Clan Stromshroud', 'Luffy', 'Mickey', 'juanrtx', 'imagen'),
(5, 'Toon Trouble', 'Cuphead', 'Mugman', 'snorlax', 'imagen');


INSERT INTO Jugador (nickname, email, ID_equipo, skin_url) VALUES 
('Link', 'zaloria@gmail.com', 1, 'miku.skin.jpg'),          
('Juanito', 'juanitokawaii@gmail.com', 2, 'chenlee.skin.jpg'),   
('Justinciero', 'esaac1234@gmail.com', 3, 'toga.skin.jpg'),  
('machamp', 'luis@example.com', 2, 'bakugo.skin.jpg'),        
('Zacarias', 'zacarias123@gmail.com', 1, 'rubius.skin.jpg'),  
('1Arganda', 'aaron@example.com', 4, 'rex.skin.jpg'),       
('Cuphead', 'nodeals@gmail.com', 5, 'maiusculo.skin.jpg'),      
('Luffy', 'merry@hotmaail.com', 4, 'deku.skin.jpg');         


INSERT INTO Torneo (nombre, fecha, premio, max_equipos, estado, banner_url) VALUES 
('Daiku', '2026-04-12 22:19:00', 1000.0, 6, 'abierto', 'copa-daiku.png'),
('Impact Cop', '2026-04-20 22:35:00', 1500.0, 6, 'abierto', 'impact-cop.png');
