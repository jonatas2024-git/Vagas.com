import express from 'express';
import cors from 'cors';
import vagasRouter from './routes/vagas.js';

const app = express();
app.use(cors());
app.use(express.json());

app.use('/vagas', vagasRouter);

const PORT = process.env.PORT || 3001;
app.listen(PORT, () => console.log(`✅ Backend rodando na porta ${PORT}`));
