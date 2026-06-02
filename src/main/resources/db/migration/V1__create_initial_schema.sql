CREATE TABLE municipalidad (
    municipalidad_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_municipio VARCHAR(255)
);

CREATE TABLE usuario (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    rol VARCHAR(255),
    municipalidad_id BIGINT,
    CONSTRAINT fk_usuario_municipalidad
        FOREIGN KEY (municipalidad_id)
        REFERENCES municipalidad (municipalidad_id)
);

CREATE TABLE material (
    material_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_item VARCHAR(255),
    stock INT NOT NULL,
    municipalidad_id BIGINT,
    CONSTRAINT fk_material_municipalidad
        FOREIGN KEY (municipalidad_id)
        REFERENCES municipalidad (municipalidad_id)
);

CREATE TABLE incidencia (
    incidencia_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    descripcion VARCHAR(255),
    url_foto VARCHAR(255),
    estado VARCHAR(255) NOT NULL,
    municipalidad_id BIGINT,
    CONSTRAINT fk_incidencia_municipalidad
        FOREIGN KEY (municipalidad_id)
        REFERENCES municipalidad (municipalidad_id)
);

CREATE TABLE detalle_incidencia_material (
    detalle_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cantidad_usada INT NOT NULL,
    incidencia_id BIGINT,
    material_id BIGINT,
    municipalidad_id BIGINT,
    CONSTRAINT fk_detalle_incidencia_material_incidencia
        FOREIGN KEY (incidencia_id)
        REFERENCES incidencia (incidencia_id),

    CONSTRAINT fk_detalle_incidencia_material_material
        FOREIGN KEY (material_id)
        REFERENCES material (material_id),

    CONSTRAINT fk_detalle_incidencia_material_municipalidad
        FOREIGN KEY (municipalidad_id)
        REFERENCES municipalidad (municipalidad_id)
);