DROP TABLE IF EXISTS public.vagas CASCADE;

CREATE TABLE public.vagas (
    id SERIAL PRIMARY KEY,
    empresa_nome VARCHAR(255) NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    senioridade VARCHAR(50) NOT NULL,
    tipo_contrato VARCHAR(20) NOT NULL,
    modelo_trabalho VARCHAR(20) NOT NULL,
    min_salary NUMERIC(10,2),
    max_salary NUMERIC(10,2),
    skills_obrigatorias TEXT[],
    skills_desejaveis TEXT[],
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);
