-- Transcrição de redação manuscrita (issues #16 e #17)

-- Origem do texto da redação: digitado no editor ou transcrito de uma foto (#17).
-- Permite comparar depois as notas de redações manuscritas e digitadas (calibração).
-- As redações que já existem foram todas digitadas, então o DEFAULT está correto para elas.
ALTER TABLE redacoes
    ADD COLUMN origem VARCHAR(20) NOT NULL DEFAULT 'DIGITADO';

ALTER TABLE redacoes
    ADD CONSTRAINT ck_redacoes_origem CHECK (origem IN ('DIGITADO', 'MANUSCRITO'));

-- Contador diário de transcrições, separado do de correções (#12, #16).
-- Os tokens da transcrição entram nas mesmas colunas tokens_entrada/tokens_saida do dia.
ALTER TABLE uso_ia_diario
    ADD COLUMN qtd_transcricoes INT NOT NULL DEFAULT 0;

ALTER TABLE uso_ia_diario
    ADD CONSTRAINT ck_uso_ia_diario_qtd_transcricoes CHECK (qtd_transcricoes >= 0);