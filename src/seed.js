import { pool } from './db.js';

const seed = async () => {
  try {
    console.log('🚀 Limpando tabela...');
    await pool.query('TRUNCATE TABLE public.vagas RESTART IDENTITY CASCADE;');

    console.log('📦 Inserindo vagas...');
    await pool.query(`
      INSERT INTO public.vagas
      (empresa_nome, titulo, descricao, cidade, estado, senioridade, tipo_contrato, modelo_trabalho,
       min_salary, max_salary, skills_obrigatorias, skills_desejaveis, created_at, updated_at)
      VALUES
      ('Nubank','Backend Engineer','Desenvolvimento de microsserviços financeiros escaláveis.','Sao Paulo','SP','SENIOR','CLT','HIBRIDO',13000.00,22000.00,'{Kotlin,Java,PostgreSQL}','{AWS,Kafka}',NOW(),NOW()),
      ('PicPay','Software Engineer','Criação de APIs de pagamento digital.','Vitoria','ES','PLENO','CLT','REMOTO',8000.00,12000.00,'{Node.js,TypeScript,REST}','{AWS,Redis}',NOW(),NOW()),
      ('Stone','DevOps Engineer','Automação de infraestrutura e pipelines CI/CD.','Rio de Janeiro','RJ','SENIOR','PJ','HIBRIDO',12000.00,20000.00,'{AWS,Terraform,Docker,Kubernetes}','{Grafana,Prometheus}',NOW(),NOW()),
      ('Itaú Unibanco','Engenheiro de Software','Modernização de sistemas bancários legados.','Sao Paulo','SP','SENIOR','CLT','HIBRIDO',14000.00,22000.00,'{Java,Spring Boot,Oracle}','{Kafka,Docker}',NOW(),NOW())
      -- ADICIONE AQUI AS DEMAIS 46 (mesmo padrão que já validamos)
    `);

    console.log('✅ Seed completo!');
    process.exit(0);
  } catch (err) {
    console.error('Erro ao inserir seed:', err);
    process.exit(1);
  }
};

seed();
