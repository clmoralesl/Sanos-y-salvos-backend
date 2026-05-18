-- 1. Catálogo de Especies
INSERT INTO especie (id_especie, nombre_especie) VALUES (1, 'Perro');
INSERT INTO especie (id_especie, nombre_especie) VALUES (2, 'Gato');
INSERT INTO especie (id_especie, nombre_especie) VALUES (3, 'Ave');
INSERT INTO especie (id_especie, nombre_especie) VALUES (4, 'Otro');

-- 2. Catálogo de Razas
-- Perros
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (1, 'Mestizo / Callejero', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (2, 'Labrador Retriever', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (3, 'Golden Retriever', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (4, 'Pastor Alemán', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (5, 'Bulldog Francés', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (6, 'Beagle', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (7, 'Poodle (Caniche)', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (8, 'Rottweiler', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (9, 'Yorkshire Terrier', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (10, 'Boxer', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (11, 'Chihuahua', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (12, 'Dachshund (Salchicha)', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (13, 'Siberian Husky', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (14, 'Border Collie', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (15, 'Pug', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (16, 'No lo sé / No estoy seguro', 1);
-- Gatos
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (17, 'Mestizo / Común Europeo', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (18, 'Siamés', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (19, 'Persa', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (20, 'Maine Coon', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (21, 'Ragdoll', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (22, 'Bengala', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (23, 'Angora Turco', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (24, 'Sphynx (Esfinge)', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (25, 'No lo sé', 2);

-- 3. Catálogo de Tamaños
INSERT INTO tamanio (id_tamanio, descripcion_tamanio) VALUES (1, 'Pequeño (0-10kg)');
INSERT INTO tamanio (id_tamanio, descripcion_tamanio) VALUES (2, 'Mediano (11-25kg)');
INSERT INTO tamanio (id_tamanio, descripcion_tamanio) VALUES (3, 'Grande (26-45kg)');
INSERT INTO tamanio (id_tamanio, descripcion_tamanio) VALUES (4, 'Gigante (45kg+)');

-- 4. Catálogo de Características
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (1, 'Juguetón');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (2, 'Tímido / Asustadizo');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (3, 'Amigable con niños');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (4, 'Requiere medicación');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (5, 'Ojos de distinto color');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (6, 'Cojera visible');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (7, 'Usa collar');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (8, 'Agresivo con otros animales');

-- 5. Tipos de Cuenta
INSERT INTO tipo_cuenta (id_tipo_cuenta, descripcion) VALUES (1, 'Usuario Estándar');
INSERT INTO tipo_cuenta (id_tipo_cuenta, descripcion) VALUES (2, 'Organización / Refugio');

-- 6. Tipos de Reporte
INSERT INTO tipo_reporte (id_tipo_reporte, descripcion) VALUES (1, 'Mascota Perdida');
INSERT INTO tipo_reporte (id_tipo_reporte, descripcion) VALUES (2, 'Mascota Encontrada / Avistamiento');

-- 7. Estados de Reporte
INSERT INTO estado_reporte (id_estado_reporte, descripcion) VALUES (1, 'Activo');
INSERT INTO estado_reporte (id_estado_reporte, descripcion) VALUES (2, 'Cerrado/Resuelto');

-- 8. Datos Iniciales
INSERT INTO organizacion (id_organizacion, nombre_organizacion, direccion, telefono) VALUES (1, 'Refugio Patitas Felices', 'Av. Providencia 1234, Santiago', '+56912345678');
INSERT INTO usuario (id_usuario, auth0_id, nombre, email, telefono, id_organizacion, id_tipo_cuenta) VALUES (1, 'auth0|local_dummy_001', 'Juan Pérez', 'juan@sanosysalvos.cl', '+56988887777', NULL, 1);
INSERT INTO mascota (id_mascota, nombre_mascota, descripcion, id_raza, id_tamanio, id_usuario) VALUES (1, 'Max', 'Labrador chocolate con mancha blanca en el pecho', 2, 3, 1);
