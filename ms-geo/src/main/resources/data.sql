-- 1. Regiones de Chile
INSERT INTO region (id_region, nombre_region) VALUES (1, 'Arica y Parinacota') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (2, 'Tarapacá') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (3, 'Antofagasta') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (4, 'Atacama') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (5, 'Coquimbo') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (6, 'Valparaíso') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (7, 'Metropolitana de Santiago') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (8, 'Libertador General Bernardo O''Higgins') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (9, 'Maule') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (10, 'Ñuble') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (11, 'Biobío') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (12, 'La Araucanía') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (13, 'Los Ríos') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (14, 'Los Lagos') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (15, 'Aysén del General Carlos Ibáñez del Campo') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);
INSERT INTO region (id_region, nombre_region) VALUES (16, 'Magallanes y de la Antártica Chilena') ON DUPLICATE KEY UPDATE nombre_region = VALUES(nombre_region);

-- 2. Comunas principales por Región (Muestra representativa, se recomienda expandir)
-- RM (7)
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (1, 'Santiago', 7) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (2, 'Providencia', 7) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (3, 'Las Condes', 7) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (4, 'Maipú', 7) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (5, 'Ñuñoa', 7) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (6, 'La Florida', 7) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (7, 'Pudahuel', 7) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (8, 'Quilicura', 7) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (9, 'Vitacura', 7) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (10, 'Lo Barnechea', 7) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);

-- Valparaíso (6)
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (11, 'Valparaíso', 6) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (12, 'Viña del Mar', 6) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (13, 'Quilpué', 6) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (14, 'Villa Alemana', 6) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (15, 'Concón', 6) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);

-- Biobío (11)
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (16, 'Concepción', 11) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (17, 'Talcahuano', 11) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (18, 'San Pedro de la Paz', 11) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (19, 'Chiguayante', 11) ON DUPLICATE KEY UPDATE nombre_comuna = VALUES(nombre_comuna);
