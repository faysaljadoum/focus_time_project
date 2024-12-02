CREATE DATABASE focustime_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);


CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE applications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    exe_path VARCHAR(255) NOT NULL,
    icon VARCHAR(255),
    is_blocked BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

CREATE TABLE SuiviTemps (
    id INT AUTO_INCREMENT PRIMARY KEY,
    application_id INT NOT NULL,
    heureDebut DATETIME NOT NULL,
    heureFin DATETIME NOT NULL,
    duree TIME,
    FOREIGN KEY (application_id) REFERENCES Application(id)
);

CREATE TABLE ObjectifProductivite (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    description TEXT,
    dateDebut DATETIME NOT NULL,
    dateFin DATETIME NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE Tache (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    description TEXT,
    statut BOOLEAN NOT NULL DEFAULT FALSE,
    objectif_id INT NOT NULL,
    FOREIGN KEY (objectif_id) REFERENCES ObjectifProductivite(id)
);


ALTER TABLE Tache
ADD FOREIGN KEY (objectif_id)
REFERENCES ObjectifProductivite(id)
ON DELETE CASCADE;
