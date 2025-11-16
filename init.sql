-- Criação da tabela de vagas
CREATE TABLE IF NOT EXISTS public.vagas (
    id SERIAL PRIMARY KEY,
    empresa_nome VARCHAR(255) NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    salario NUMERIC(12,2),
    senioridade VARCHAR(50),
    modelo_trabalho VARCHAR(50),
    estado VARCHAR(50),
    habilidades_obrigatorias TEXT[],
    habilidades_desejaveis TEXT[],
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Exemplo de 3 registros iniciais
INSERT INTO public.vagas 
(empresa_nome, titulo, descricao, salario, senioridade, modelo_trabalho, estado, habilidades_obrigatorias, habilidades_desejaveis)
VALUES
('Google', 'Software Engineer Senior', 'Desenvolvimento de sistemas escaláveis em cloud.', 18000.00, 'Senior', 'Remoto', 'SP', ARRAY['Node.js', 'React', 'AWS'], ARRAY['Docker', 'Kubernetes']),
('Microsoft', 'DevOps Engineer Pleno', 'Automação de pipelines CI/CD.', 15000.00, 'Pleno', 'Híbrido', 'RJ', ARRAY['GitHub Actions', 'Azure'], ARRAY['Terraform', 'Python']),
('Amazon', 'Backend Engineer Senior', 'Criação de APIs de alta performance.', 19000.00, 'Senior', 'Remoto', 'PR', ARRAY['Java', 'Spring Boot', 'PostgreSQL'], ARRAY['Redis', 'Kafka']);
