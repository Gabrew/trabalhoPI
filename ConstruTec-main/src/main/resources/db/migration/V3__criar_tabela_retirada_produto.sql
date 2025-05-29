CREATE TABLE retirada_produto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    obra_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    data_retirada DATE NOT NULL,
    FOREIGN KEY (obra_id) REFERENCES obras(id),
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
); 