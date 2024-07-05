import { IInsegnante } from 'app/entities/insegnante/insegnante.model';

export interface IInsegnanteCorso {
  id: number;
  mese?: number | null;
  insegnante?: IInsegnante | null;
}

export type NewInsegnanteCorso = Omit<IInsegnanteCorso, 'id'> & { id: null };
