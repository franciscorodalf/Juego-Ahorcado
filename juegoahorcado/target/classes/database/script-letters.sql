DROP TABLE IF EXISTS usuarios;
DROP INDEX IF EXISTS idx_email;
DROP TABLE IF EXISTS niveles;
DROP TABLE IF EXISTS palabras;

CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    puntos INTEGER DEFAULT 0,
    id_nivel INTEGER NOT NULL,  
    FOREIGN KEY (id_nivel) REFERENCES niveles(id)  
);


CREATE INDEX idx_email ON usuarios (email);

INSERT INTO usuarios (user, email, password, id_nivel) VALUES 
    ('Usuario-1', 'email1@example.com', 'contraseña123', 1),  
    ('Usuario-2', 'email2@example.com', 'abc456', 1), 
    ('Usuario-3', 'email3@example.com', 'xyz789', 1);  

CREATE TABLE niveles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nivel TEXT NOT NULL
);

INSERT INTO niveles (nivel) VALUES
('facil'),
('medio'),
('dificil');

CREATE TABLE palabras (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    palabra TEXT NOT NULL,
    id_nivel INTEGER NOT NULL,
    FOREIGN KEY (id_nivel) REFERENCES niveles(id)
);

-- Insertar palabras de nivel 'facil'
INSERT INTO palabras (palabra, id_nivel) VALUES
('guerra', 1),
('circuito', 1),
('sombra', 1),
('relieve', 1),
('pescado', 1),
('pintura', 1),
('planta', 1),
('viento', 1),
('raton', 1),
('flore', 1);

-- Insertar palabras de nivel 'medio'
INSERT INTO palabras (palabra, id_nivel) VALUES
('murcielago', 2),
('ciudad', 2),
('camino', 2),
('repollo', 2),
('corredor', 2),
('banco', 2),
('granja', 2),
('tierra', 2),
('pelota', 2),
('parque', 2);

-- Insertar palabras de nivel 'dificil'
INSERT INTO palabras (palabra, id_nivel) VALUES
('anticonstitucionalmente', 3),
('hipopotomonstrosesquipedaliofobia', 3),
('supercalifragilisticexpialidocious', 3),
('pneumoultramicroscopicsilicovolcanoconiosis', 3),
('quimioterapia', 3),
('desoxirribonucleico', 3),
('transatlantico', 3),
('electromagnetico', 3),
('inconstitucionalidad', 3),
('historiografia', 3);