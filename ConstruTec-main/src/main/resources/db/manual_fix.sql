-- Adiciona um valor default temporário para a coluna quantidade
ALTER TABLE produtos MODIFY quantidade INT DEFAULT 0;

-- Remove o campo quantidade
ALTER TABLE produtos DROP COLUMN quantidade; 