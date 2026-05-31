IF COL_LENGTH('detalle_incidencia_material', 'cantidad_usada') IS NULL
BEGIN
    ALTER TABLE detalle_incidencia_material
    ADD cantidad_usada INT NOT NULL CONSTRAINT df_detalle_incidencia_material_cantidad_usada DEFAULT 0;
END;
