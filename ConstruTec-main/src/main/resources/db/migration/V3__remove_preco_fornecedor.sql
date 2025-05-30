-- Verifica o nome da chave estrangeira
SELECT CONSTRAINT_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'produtos'
  AND COLUMN_NAME = 'fornecedor_id_fk'
  AND CONSTRAINT_SCHEMA = 'controle_estoque';

-- Remove as colunas preco_custo e fornecedor_id_fk da tabela produtos
SET FOREIGN_KEY_CHECKS=0;
ALTER TABLE produtos DROP COLUMN preco_custo;
ALTER TABLE produtos DROP COLUMN fornecedor_id_fk;
SET FOREIGN_KEY_CHECKS=1; 