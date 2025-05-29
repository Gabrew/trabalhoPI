ALTER TABLE obras ADD COLUMN nome VARCHAR(255) NOT NULL DEFAULT 'Obra sem nome';
UPDATE obras SET nome = CONCAT('Obra ', id) WHERE nome = 'Obra sem nome'; 