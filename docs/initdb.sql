CREATE DATABASE IF NOT EXISTS zaloria;
USE zaloria;

/* 1. Tabla de Torneo */
CREATE TABLE torneo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    fecha DATETIME,
    premio DOUBLE,
    max_equipos INT,
    estado VARCHAR(50), -- ABIERTO, EN CURSO, FINALIZADO
    banner_url VARCHAR(255),
    ganador_id INT,
    FOREIGN KEY (ganador_id) REFERENCES equipo(id)
);

/* 2. Tabla de Equipo */
CREATE TABLE equipo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    capitan VARCHAR(100),
    entrenador VARCHAR(100),
    managers VARCHAR(100),
    logo_url VARCHAR(255)
);

/* 3. Tabla de Jugador */
CREATE TABLE jugador (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    skin_url VARCHAR(255),
    id_equipo INT,
    FOREIGN KEY (id_equipo) REFERENCES equipo(id)
);

/* 4. Tabla de Inscripción (Intermedia) */
CREATE TABLE inscripcion (
    torneo_id INT NOT NULL,
    equipo_id INT NOT NULL,
    PRIMARY KEY (torneo_id, equipo_id),
    FOREIGN KEY (torneo_id) REFERENCES torneo(id),
    FOREIGN KEY (equipo_id) REFERENCES equipo(id)
);

/* 5. Tabla de Resultado (Relación 1:1 con Torneo) */
CREATE TABLE resultado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_torneo INT UNIQUE,
    equipo_ganador_id INT,
    id_mvp INT,
    eliminaciones INT DEFAULT 0,
    puntos_posicion INT DEFAULT 0,
    puntos_totales INT DEFAULT 0,
    puntuacion VARCHAR(50), -- Ejemplo: "95 PTS"
    FOREIGN KEY (id_torneo) REFERENCES torneo(id),
    FOREIGN KEY (equipo_ganador_id) REFERENCES equipo(id),
    FOREIGN KEY (id_mvp) REFERENCES jugador(id)
);

/* 6. Tabla de Solicitudes */
CREATE TABLE solicitudes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    nickname VARCHAR(100),
    email VARCHAR(100),
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    motivo TEXT,
    estado VARCHAR(50) DEFAULT 'PENDIENTE' -- PENDIENTE, ACEPTADA, RECHAZADA
);