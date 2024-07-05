export interface IClienteCorso {
  id: number;
  mese?: number | null;
  pagato?: boolean | null;
}

export type NewClienteCorso = Omit<IClienteCorso, 'id'> & { id: null };
