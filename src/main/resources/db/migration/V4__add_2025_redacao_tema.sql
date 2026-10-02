INSERT INTO temas (titulo, origem, ano)
SELECT 'Perspectivas acerca do envelhecimento na sociedade brasileira', 'ENEM_OFICIAL', 2025
WHERE NOT EXISTS (
    SELECT 1 FROM temas
    WHERE titulo = 'Perspectivas acerca do envelhecimento na sociedade brasileira'
);