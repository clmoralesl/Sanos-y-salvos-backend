INSERT INTO region (id_region, nombre_region) VALUES (1, 'Región de Tarapacá') ON DUPLICATE KEY UPDATE nombre_region = nombre_region;
INSERT INTO region (id_region, nombre_region) VALUES (2, 'Región de Valparaíso') ON DUPLICATE KEY UPDATE nombre_region = nombre_region;
INSERT INTO region (id_region, nombre_region) VALUES (3, 'Región Metropolitana de Santiago') ON DUPLICATE KEY UPDATE nombre_region = nombre_region;

INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (1, 'Santiago', 3) ON DUPLICATE KEY UPDATE nombre_comuna = nombre_comuna;
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (2, 'Providencia', 3) ON DUPLICATE KEY UPDATE nombre_comuna = nombre_comuna;
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (3, 'Las Condes', 3) ON DUPLICATE KEY UPDATE nombre_comuna = nombre_comuna;
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (4, 'Maipú', 3) ON DUPLICATE KEY UPDATE nombre_comuna = nombre_comuna;
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (5, 'Ñuñoa', 3) ON DUPLICATE KEY UPDATE nombre_comuna = nombre_comuna;

INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (6, 'Valparaíso', 2) ON DUPLICATE KEY UPDATE nombre_comuna = nombre_comuna;
INSERT INTO comuna (id_comuna, nombre_comuna, region_id_region) VALUES (7, 'Viña del Mar', 2) ON DUPLICATE KEY UPDATE nombre_comuna = nombre_comuna;
