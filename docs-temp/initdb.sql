CREATE DATABASE IF NOT EXISTS zaloria;
USE zaloria;

/* 1. Tabla de Torneo */
CREATE TABLE torneo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    fecha DATETIME,
    premio VARCHAR(100),
    max_equipos INT,
    banner_url VARCHAR(255),
    estado ENUM('ABIERTO', 'CERRADO') DEFAULT 'ABIERTO'
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
    nickname VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    skin_url VARCHAR(255),
    id_equipo INT,
    CONSTRAINT fk_jugador_equipo FOREIGN KEY (id_equipo) REFERENCES equipo(id) ON DELETE SET NULL
);

/* 4. Tabla de Inscripción (Intermedia) */
CREATE TABLE inscripcion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_torneo INT,
    id_equipo INT,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_torneo) REFERENCES torneo(id),
    FOREIGN KEY (id_equipo) REFERENCES equipo(id)
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
    puntuacion VARCHAR(50),
    FOREIGN KEY (id_torneo) REFERENCES torneo(id),
    FOREIGN KEY (equipo_ganador_id) REFERENCES equipo(id),
    FOREIGN KEY (id_mvp) REFERENCES jugador(id)
);

/* 6. Tabla de Solicitudes */
CREATE TABLE solicitudes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    motivo TEXT,
    estado ENUM('PENDIENTE', 'ACEPTADA', 'RECHAZADA') DEFAULT 'PENDIENTE'
);