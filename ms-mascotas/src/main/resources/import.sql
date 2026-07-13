INSERT INTO especie (id_especie, nombre_especie) VALUES (1, 'Perro');
INSERT INTO especie (id_especie, nombre_especie) VALUES (2, 'Gato');
INSERT INTO especie (id_especie, nombre_especie) VALUES (3, 'Ave');
INSERT INTO especie (id_especie, nombre_especie) VALUES (4, 'Hámster');
INSERT INTO especie (id_especie, nombre_especie) VALUES (5, 'Tortuga');
INSERT INTO especie (id_especie, nombre_especie) VALUES (6, 'Conejo');
INSERT INTO especie (id_especie, nombre_especie) VALUES (7, 'Hurón');
INSERT INTO especie (id_especie, nombre_especie) VALUES (8, 'Erizo');
INSERT INTO especie (id_especie, nombre_especie) VALUES (9, 'Reptil');
INSERT INTO especie (id_especie, nombre_especie) VALUES (10, 'Pez');
INSERT INTO especie (id_especie, nombre_especie) VALUES (11, 'Otro');

INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (1, 'Mestizo', 1);
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
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (16, 'Otra', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (17, 'Mestizo / Común Europeo', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (18, 'Siamés', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (19, 'Persa', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (20, 'Maine Coon', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (21, 'Ragdoll', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (22, 'Bengala', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (23, 'Angora Turco', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (24, 'Sphynx (Esfinge)', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (25, 'Otra', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (26, 'Canario', 3);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (27, 'Cacatúa', 3);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (28, 'Otra', 3);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (29, 'Sirio', 4);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (30, 'Ruso', 4);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (31, 'Otra', 4);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (32, 'Tortuga de agua', 5);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (33, 'Tortuga terrestre', 5);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (34, 'Otra', 5);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (35, 'Enano', 6);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (36, 'Cabeza de león', 6);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (37, 'Otra', 6);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (38, 'Estándar', 7);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (39, 'Otra', 7);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (40, 'Pigmio Africano', 8);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (41, 'Otra', 8);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (42, 'Iguana', 9);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (43, 'Gecko', 9);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (44, 'Otra', 9);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (45, 'Pez Dorado', 10);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (46, 'Beta', 10);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (47, 'Otra', 10);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (48, 'Especificar en la descripción', 11);

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
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (9, 'Cola corta');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (10, 'Sin cola');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (11, 'Orejas caídas');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (12, 'Orejas erguidas');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (13, 'Mancha blanca en el pecho');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (14, 'Usa arnés');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (15, 'Cicatriz visible');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (16, 'Patas blancas');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (17, 'Mancha en el ojo');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (18, 'Cariñoso');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (19, 'Muy activo');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (20, 'Lleva ropa / capa');

INSERT INTO tipo_cuenta (id_tipo_cuenta, descripcion) VALUES (1, 'Usuario Estándar');
INSERT INTO tipo_cuenta (id_tipo_cuenta, descripcion) VALUES (2, 'Organización / Refugio');
INSERT INTO tipo_cuenta (id_tipo_cuenta, descripcion) VALUES (3, 'Administrador');

-- 6. Tipos de Reporte
INSERT INTO tipo_reporte (id_tipo_reporte, descripcion) VALUES (1, 'Mascota Perdida');
INSERT INTO tipo_reporte (id_tipo_reporte, descripcion) VALUES (2, 'Mascota Encontrada / Avistamiento');

-- 7. Estados de Reporte
INSERT INTO estado_reporte (id_estado_reporte, descripcion) VALUES (1, 'Activo');
INSERT INTO estado_reporte (id_estado_reporte, descripcion) VALUES (2, 'Cerrado/Resuelto');

INSERT INTO organizacion (id_organizacion, nombre_organizacion, direccion, telefono) VALUES (1, 'Refugio Patitas Felices', 'Av. Providencia 1234, Santiago', '+56912345678');
INSERT INTO usuario (id_usuario, auth0_id, nombre, email, telefono, id_organizacion, id_tipo_cuenta) VALUES (1, 'auth0|local_dummy_001', 'Juan Pérez', 'juan@sanosysalvos.cl', '+56988887777', NULL, 1);
INSERT INTO usuario (id_usuario, auth0_id, nombre, email, telefono, id_organizacion, id_tipo_cuenta) VALUES (2, 'auth0|local_dummy_002', 'Valeska Guardia', 'valeska@sanosysalvos.cl', '+56999999999', NULL, 1);
INSERT INTO usuario (id_usuario, auth0_id, nombre, email, telefono, id_organizacion, id_tipo_cuenta) VALUES (3, 'auth0|local_dummy_003', 'Claudio Morales', 'claudio@sanosysalvos.cl', '+56977777777', NULL, 1);
INSERT INTO usuario (id_usuario, auth0_id, nombre, email, telefono, id_organizacion, id_tipo_cuenta) VALUES (4, 'google-oauth2|106589703694374122699', 'Claudio Morales Linares', 'claudiomoraleseiv@gmail.com', '', NULL, 3);
INSERT INTO mascota (id_mascota, nombre_mascota, descripcion, color_primario, color_secundario, id_raza, id_tamanio, id_usuario, edad_aproximada) VALUES (1, 'Max', 'Labrador chocolate con mancha blanca en el pecho', 'Marrón', 'Blanco', 2, 3, 1, '1-3 años');
