export interface IFoto {
  id: number;
  blob?: string | null;
  blobContentType?: string | null;
  nome_file?: string | null;
}

export type NewFoto = Omit<IFoto, 'id'> & { id: null };
