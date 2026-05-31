CREATE TABLE municipalidad (
    municipalidad_id BIGINT IDENTITY(1,1) NOT NULL,
    nombre_municipio NVARCHAR(255),
    CONSTRAINT pk_municipalidad PRIMARY KEY (municipalidad_id)
);

CREATE TABLE usuario (
    user_id BIGINT IDENTITY(1,1) NOT NULL,
    username NVARCHAR(255),
    password NVARCHAR(255),
    rol NVARCHAR(255),
    municipalidad_id BIGINT NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (user_id),
    CONSTRAINT fk_usuario_municipalidad FOREIGN KEY (municipalidad_id) REFERENCES municipalidad (municipalidad_id)
);

CREATE TABLE material (
    material_id BIGINT IDENTITY(1,1) NOT NULL,
    nombre_item NVARCHAR(255),
    stock INT NOT NULL,
    municipalidad_id BIGINT NULL,
    CONSTRAINT pk_material PRIMARY KEY (material_id),
    CONSTRAINT fk_material_municipalidad FOREIGN KEY (municipalidad_id) REFERENCES municipalidad (municipalidad_id)
);

CREATE TABLE incidencia (
    incidencia_id BIGINT IDENTITY(1,1) NOT NULL,
    descripcion NVARCHAR(255),
    url_foto NVARCHAR(255),
    estado NVARCHAR(255) NOT NULL,
    municipalidad_id BIGINT NULL,
    CONSTRAINT pk_incidencia PRIMARY KEY (incidencia_id),
    CONSTRAINT fk_incidencia_municipalidad FOREIGN KEY (municipalidad_id) REFERENCES municipalidad (municipalidad_id)
);

CREATE TABLE detalle_incidencia_material (
    detalle_id BIGINT IDENTITY(1,1) NOT NULL,
    cantidad_usada INT NOT NULL,
    incidencia_id BIGINT NULL,
    material_id BIGINT NULL,
    municipalidad_id BIGINT NULL,
    CONSTRAINT pk_detalle_incidencia_material PRIMARY KEY (detalle_id),
    CONSTRAINT fk_detalle_incidencia_material_incidencia FOREIGN KEY (incidencia_id) REFERENCES incidencia (incidencia_id),
    CONSTRAINT fk_detalle_incidencia_material_material FOREIGN KEY (material_id) REFERENCES material (material_id),
    CONSTRAINT fk_detalle_incidencia_material_municipalidad FOREIGN KEY (municipalidad_id) REFERENCES municipalidad (municipalidad_id)
);