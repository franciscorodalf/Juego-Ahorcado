DROP TABLE IF EXISTS usuarios;

DROP INDEX IF EXISTS idx_email;

CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
);

CREATE INDEX idx_email ON usuarios (email);

INSERT INTO usuarios (user, email, password) VALUES 
    ('Usuario-1', 'email1@example.com', 'contraseña123'),
    ('Usuario-2', 'email2@example.com', 'abc456'),
    ('Usuario-3', 'email3@example.com', 'xyz789');