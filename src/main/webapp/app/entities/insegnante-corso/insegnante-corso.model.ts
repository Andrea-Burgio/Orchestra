import { IInsegnante } from 'app/entities/insegnante/insegnante.model';
import { ICorso } from 'app/entities/corso/corso.model';

export interface IInsegnanteCorso {
  id: number;
  mese?: number | null;
  insegnante?: IInsegnante | null;
  corso?: ICorso | null;
}

export type NewInsegnanteCorso = Omit<IInsegnanteCorso, 'id'> & { id: null };
