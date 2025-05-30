ALTER TABLE ESTOQUES
ADD COLUMN fornecedor_id BIGINT,
ADD COLUMN preco_custo DECIMAL(7,2);

ALTER TABLE ESTOQUES
ADD CONSTRAINT fk_estoque_fornecedor
FOREIGN KEY (fornecedor_id)
REFERENCES fornecedores(id);

-- Atualiza os registros existentes com dados das últimas movimentações
UPDATE ESTOQUES e
SET fornecedor_id = (
    SELECT m.fornecedor_id
    FROM MOVIMENTACOES_ESTOQUE m
    WHERE m.produto_id = e.produto_id
    AND (m.destino_id = e.obra_id OR (m.destino_id IS NULL AND e.obra_id IS NULL))
    ORDER BY m.data_movimentacao DESC
    LIMIT 1
),
preco_custo = (
    SELECT m.preco_custo
    FROM MOVIMENTACOES_ESTOQUE m
    WHERE m.produto_id = e.produto_id
    AND (m.destino_id = e.obra_id OR (m.destino_id IS NULL AND e.obra_id IS NULL))
    ORDER BY m.data_movimentacao DESC
    LIMIT 1
); 