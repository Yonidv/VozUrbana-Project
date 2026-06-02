
ALTER TABLE material
ADD COLUMN IF NOT EXISTS codigo_material VARCHAR(20);


UPDATE material
SET codigo_material =
    'MAT-' || LPAD(material_id::TEXT, 3, '0')
WHERE codigo_material IS NULL;

ALTER TABLE material
ALTER COLUMN codigo_material SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_material_codigo_material
ON material (codigo_material);