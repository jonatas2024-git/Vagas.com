import express from 'express';
import { pool } from '../db.js';
const router = express.Router();

// Listar todas as vagas
router.get('/', async (req, res) => {
  try {
    const result = await pool.query('SELECT * FROM public.vagas ORDER BY created_at DESC');
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Buscar por filtros
router.get('/search', async (req, res) => {
  const { estado, senioridade, modelo_trabalho } = req.query;
  let query = 'SELECT * FROM public.vagas WHERE 1=1';
  const params = [];

  if (estado) {
    params.push(estado);
    query += ` AND estado = $${params.length}`;
  }
  if (senioridade) {
    params.push(senioridade);
    query += ` AND senioridade = $${params.length}`;
  }
  if (modelo_trabalho) {
    params.push(modelo_trabalho);
    query += ` AND modelo_trabalho = $${params.length}`;
  }

  query += ' ORDER BY created_at DESC';

  try {
    const result = await pool.query(query, params);
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

export default router;
