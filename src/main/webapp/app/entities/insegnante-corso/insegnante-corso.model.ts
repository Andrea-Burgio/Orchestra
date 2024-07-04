export interface IInsegnanteCorso {
  id: number;
  mese?: number | null;
}

export type NewInsegnanteCorso = Omit<IInsegnanteCorso, 'id'> & { id: null };
