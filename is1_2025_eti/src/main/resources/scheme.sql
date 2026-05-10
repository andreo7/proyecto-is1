-- Crea la tabla 'users' con los campos originales, adaptados para SQLite
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT, -- Clave primaria autoincremental para SQLite
    name TEXT NOT NULL UNIQUE,          -- Nombre de usuario (TEXT es el tipo de cadena recomendado para SQLite), con restricción UNIQUE
    password TEXT NOT NULL           -- Contraseña hasheada (TEXT es el tipo de cadena recomendado para SQLite)
);

DROP TABLE IF EXISTS persona;
CREATE TABLE persona (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    dni TEXT NOT NULL UNIQUE,
    nombre TEXT NOT NULL,     
    apellido TEXT NOT NULL,
    direccion TEXT NOT NULL,
    contacto TEXT NOT NULL UNIQUE      
);

DROP TABLE IF EXISTS docente;
CREATE TABLE docente(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_person INTEGER NOT NULL UNIQUE,
    matricula INTEGER NOT NULL UNIQUE,
    CONSTRAINT fk_docente1 FOREIGN KEY (id_person) REFERENCES persona(id)
);

DROP TABLE IF EXISTS estudiante;
CREATE TABLE estudiante(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_person INTEGER NOT NULL UNIQUE,
    nro_alumno INTEGER NOT NULL UNIQUE,
    estado_carrera VARCHAR NOT NULL,
    CONSTRAINT fk_estudiante FOREIGN KEY (id_person) REFERENCES persona(id),
    CHECK (estado_carrera IN ('Ingresante', 'Avanzado'))
);