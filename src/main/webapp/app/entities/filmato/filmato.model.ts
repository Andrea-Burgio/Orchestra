export interface IFilmato {
  id: number;
  blob?: string | null;
  blobContentType?: string | null;
  nome_file?: string | null;
}

export type NewFilmato = Omit<IFilmato, 'id'> & { id: null };
