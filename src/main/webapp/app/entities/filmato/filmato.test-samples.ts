import { IFilmato, NewFilmato } from './filmato.model';

export const sampleWithRequiredData: IFilmato = {
  id: 11363,
};

export const sampleWithPartialData: IFilmato = {
  id: 21003,
  nome_file: 'pro whoa',
};

export const sampleWithFullData: IFilmato = {
  id: 20433,
  blob: '../fake-data/blob/hipster.png',
  blobContentType: 'unknown',
  nome_file: 'dazzling sweetly muddy',
};

export const sampleWithNewData: NewFilmato = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
