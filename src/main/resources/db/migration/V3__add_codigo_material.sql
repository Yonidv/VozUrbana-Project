IF COL_LENGTH('material', 'codigo_material') IS NULL
BEGIN
    ALTER TABLE material
    ADD codigo_material NVARCHAR(20) NULL;
END;

EXEC sys.sp_executesql N'
    UPDATE material
    SET codigo_material = ''MAT-'' + RIGHT(''000'' + CAST(material_id AS NVARCHAR(10)), 3)
    WHERE codigo_material IS NULL;
';

IF EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'uq_material_codigo_material'
      AND object_id = OBJECT_ID('material')
)
BEGIN
    EXEC('DROP INDEX uq_material_codigo_material ON material');
END;

ALTER TABLE material
ALTER COLUMN codigo_material NVARCHAR(20) NOT NULL;

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'uq_material_codigo_material'
      AND object_id = OBJECT_ID('material')
)
BEGIN
    EXEC('CREATE UNIQUE INDEX uq_material_codigo_material ON material (codigo_material)');
END;