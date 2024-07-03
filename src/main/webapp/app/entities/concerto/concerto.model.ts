import dayjs from 'dayjs/esm';

export interface IConcerto {
  id: number;
  data?: dayjs.Dayjs | null;
  nome?: string | null;
}

export type NewConcerto = Omit<IConcerto, 'id'> & { id: null };
