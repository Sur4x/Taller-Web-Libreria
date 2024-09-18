-- Tabla de usuarios
CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       contraseña VARCHAR(255) NOT NULL,

);

-- Tabla de clubes de lectura
CREATE TABLE clubs (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       nombre VARCHAR(100) NOT NULL,
                       genero VARCHAR(50),
                       descripcion TEXT,
);

-- Tabla intermedia para relacionar usuarios y clubes
CREATE TABLE user_clubs (
                            user_id INT,
                            club_id INT,
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (club_id) REFERENCES clubs(id),
                            PRIMARY KEY (user_id, club_id)
);