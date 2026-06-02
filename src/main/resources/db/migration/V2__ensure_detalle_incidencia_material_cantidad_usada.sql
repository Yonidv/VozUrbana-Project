ALTER TABLE detalle_incidencia_material
ADD COLUMN IF NOT EXISTS cantidad_usada INT NOT NULL DEFAULT 0;