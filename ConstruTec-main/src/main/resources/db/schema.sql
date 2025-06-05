-- Remover tabelas se existirem
DROP TABLE IF EXISTS usuarios_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS usuarios;

-- Criar tabela de roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_role VARCHAR(255) NOT NULL
);

-- Criar tabela de usuários
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(255) NOT NULL UNIQUE,
    nome_completo VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL
);

-- Criar tabela de relacionamento entre usuários e roles
CREATE TABLE IF NOT EXISTS usuarios_roles (
    usuario_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, role_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
); 