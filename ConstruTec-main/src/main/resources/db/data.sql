-- Inserir roles
INSERT IGNORE INTO roles (id, nome_role) VALUES 
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER');

-- Inserir usuário admin (senha: admin)
INSERT IGNORE INTO usuarios (id, nome_completo, login, senha) VALUES 
(1, 'Administrador', 'admin', '$2a$10$GQT8b4wjzRwjad.4G6S8.OHXpxNNt.WcQRfK8CDDzKHw5NUhKqhcW');

-- Associar role admin ao usuário admin
INSERT IGNORE INTO usuarios_roles (usuario_id, role_id) VALUES 
(1, 1);

-- Inserir usuário 'g' com senha '1' (hash BCrypt)
INSERT INTO usuarios (login, nome_completo, senha) 
VALUES ('g', 'Administrador', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a');

-- Associar role admin ao usuário 'g'
INSERT IGNORE INTO usuarios_roles (usuario_id, role_id) 
SELECT u.id, r.id 
FROM usuarios u, roles r 
WHERE u.login = 'g' AND r.nome_role = 'ROLE_ADMIN';

-- Inserir usuário admin com senha '123'
INSERT INTO usuarios (login, nome_completo, senha) 
VALUES ('admin', 'Administrador', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a'); 