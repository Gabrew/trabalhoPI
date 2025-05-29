-- Adiciona a coluna status
ALTER TABLE obras ADD COLUMN IF NOT EXISTS status VARCHAR(20);

-- Atualiza registros existentes
UPDATE obras SET status = 'EM_ANDAMENTO' WHERE status IS NULL;

-- Define o valor padrão e NOT NULL constraint
ALTER TABLE obras ALTER COLUMN status SET NOT NULL;
ALTER TABLE obras ALTER COLUMN status SET DEFAULT 'EM_ANDAMENTO'; 