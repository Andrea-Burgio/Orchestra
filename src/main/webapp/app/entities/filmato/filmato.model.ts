import { IConcerto } from 'app/entities/concerto/concerto.model';

export interface IFilmato {
  id: number;
  blob?: string | null;
  blobContentType?: string | null;
  nome_file?: string | null;
  concerto?: IConcerto | null;
}

export type NewFilmato = Omit<IFilmato, 'id'> & { id: null };
