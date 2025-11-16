TRUNCATE TABLE public.vagas RESTART IDENTITY CASCADE;
INSERT INTO public.vagas (empresa_nome, titulo, descricao, cidade, estado, senioridade, tipo_contrato, modelo_trabalho, min_salary, max_salary, skills_obrigatorias, skills_desejaveis, created_at, updated_at)
VALUES
('Nubank', 'Backend Engineer', 'Desenvolvimento de microsserviços financeiros escaláveis.', 'Sao Paulo', 'SP', 'SENIOR', 'CLT', 'HIBRIDO', 13000.00, 22000.00, '{Kotlin,Java,PostgreSQL}', '{AWS,Kafka}', NOW(), NOW()),
('PicPay', 'Software Engineer', 'Criação de APIs de pagamento digital.', 'Vitoria', 'ES', 'PLENO', 'CLT', 'REMOTO', 8000.00, 12000.00, '{Node.js,TypeScript,REST}', '{AWS,Redis}', NOW(), NOW()),
-- ... até as 50
;
