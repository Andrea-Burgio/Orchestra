import { IFilmato, NewFilmato } from './filmato.model';

export const sampleWithRequiredData: IFilmato = {
  id: 30956,
};

export const sampleWithPartialData: IFilmato = {
  id: 31689,
  blob: '../fake-data/blob/hipster.png',
  blobContentType: 'unknown',
};

export const sampleWithFullData: IFilmato = {
  id: 7744,
  blob: '../fake-data/blob/hipster.png',
  blobContentType: 'unknown',
  nome_file: 'foolishly',
};

export const sampleWithNewData: NewFilmato = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
