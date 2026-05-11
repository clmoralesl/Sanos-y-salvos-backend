-- 1. Insertando Catálogo de Especies
INSERT INTO especie (id_especie, nombre_especie) VALUES (1, 'Perro');
INSERT INTO especie (id_especie, nombre_especie) VALUES (2, 'Gato');
INSERT INTO especie (id_especie, nombre_especie) VALUES (3, 'Conejo');

-- 2. Insertando Catálogo de Razas
-- Perros
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (1, 'Mestizo', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (2, 'Labrador', 1);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (3, 'Golden Retriever', 1);
-- Gatos
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (4, 'Mestizo', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (5, 'Siames', 2);
INSERT INTO raza (id_raza, nombre_raza, id_especie) VALUES (6, 'Angora', 2);

-- 3. Insertando Catálogo de Tamaños
INSERT INTO tamanio (id_tamanio, descripcion_tamanio) VALUES (1, 'Pequeno');
INSERT INTO tamanio (id_tamanio, descripcion_tamanio) VALUES (2, 'Mediano');
INSERT INTO tamanio (id_tamanio, descripcion_tamanio) VALUES (3, 'Grande');
INSERT INTO tamanio (id_tamanio, descripcion_tamanio) VALUES (4, 'Gigante');

-- 4. Insertando Catálogo de Características
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (1, 'Jugueton');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (2, 'Timido');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (3, 'Amigable con ninos');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (4, 'Requiere medicacion');
INSERT INTO caracteristica (id_caracteristica, descripcion) VALUES (5, 'Ojos de distinto color');

-- 5. Insertando Catálogo de Tipos de Cuenta
INSERT INTO tipo_cuenta (id_tipo_cuenta, descripcion) VALUES (1, 'Usuario Estandar');
INSERT INTO tipo_cuenta (id_tipo_cuenta, descripcion) VALUES (2, 'Refugio Rescatista');

-- 6. Insertando Organizaciones (Al menos 2)
INSERT INTO organizacion (id_organizacion, nombre_organizacion, direccion) VALUES (1, 'Refugio Patitas', 'Av Siempre Viva 123');
INSERT INTO organizacion (id_organizacion, nombre_organizacion, direccion) VALUES (2, 'Salvando Colitas', 'Calle Principal 456');

-- 7. Insertando Usuarios (Data Inicial dummy, Al menos 2)
INSERT INTO usuario (id_usuario, auth0_id, nombre, email, telefono, id_organizacion, id_tipo_cuenta) VALUES (1, 'auth0|local_dummy_001', 'Juan Perez', 'juan@ejemplo.com', '123456789', NULL, 1);
INSERT INTO usuario (id_usuario, auth0_id, nombre, email, telefono, id_organizacion, id_tipo_cuenta) VALUES (2, 'auth0|local_dummy_002', 'Refugio Admin', 'admin@refugio.com', '987654321', 1, 2);

-- 8. Insertando Catálogos para Reportes
INSERT INTO tipo_reporte (id_tipo_reporte, descripcion) VALUES (1, 'Mascota Perdida');
INSERT INTO tipo_reporte (id_tipo_reporte, descripcion) VALUES (2, 'Mascota Encontrada / Avistamiento');

INSERT INTO estado_reporte (id_estado_reporte, descripcion) VALUES (1, 'Activo');
INSERT INTO estado_reporte (id_estado_reporte, descripcion) VALUES (2, 'Cerrado/Resuelto');

-- 9. Insertando Mascotas (Al menos 2)
INSERT INTO mascota (id_mascota, nombre_mascota, descripcion, id_raza, id_tamanio) VALUES (1, 'Max', 'Perrito con collar azul', 2, 2);
INSERT INTO mascota (id_mascota, nombre_mascota, descripcion, id_raza, id_tamanio) VALUES (2, 'Luna', 'Gata blanca muy mansa', 6, 1);

-- 10. Insertando Fotografías (Al menos 2 por mascota)
INSERT INTO fotografia (id_fotografia, url_fotografia, id_mascota) VALUES (1, 'https://ejemplo.com/max1.jpg', 1);
INSERT INTO fotografia (id_fotografia, url_fotografia, id_mascota) VALUES (2, 'https://ejemplo.com/max2.jpg', 1);
INSERT INTO fotografia (id_fotografia, url_fotografia, id_mascota) VALUES (3, 'https://ejemplo.com/luna1.jpg', 2);
INSERT INTO fotografia (id_fotografia, url_fotografia, id_mascota) VALUES (4, 'https://ejemplo.com/luna2.jpg', 2);

-- 11. Relacionando Mascotas con Características (N a N)
INSERT INTO mascota_caracteristica (id_mascota, id_caracteristica) VALUES (1, 1); -- Max es Juguetón
INSERT INTO mascota_caracteristica (id_mascota, id_caracteristica) VALUES (1, 3); -- Max es Amigable con niños
INSERT INTO mascota_caracteristica (id_mascota, id_caracteristica) VALUES (2, 2); -- Luna es Tímida
INSERT INTO mascota_caracteristica (id_mascota, id_caracteristica) VALUES (2, 5); -- Luna tiene Ojos de distinto color

-- 12. Insertando Reportes (Al menos 2)
INSERT INTO reporte (id_reporte, fecha_reporte, id_ubicacion_reporte, id_tipo_reporte, id_estado_reporte, id_usuario, id_mascota) VALUES (1, '2024-05-19 10:20:30', NULL, 1, 1, 1, 1); -- Max fue perdido por Juan
INSERT INTO reporte (id_reporte, fecha_reporte, id_ubicacion_reporte, id_tipo_reporte, id_estado_reporte, id_usuario, id_mascota) VALUES (2, '2024-05-20 15:45:00', NULL, 2, 2, 2, 2); -- Luna fue encontrada por el Refugio (Resuelto)
