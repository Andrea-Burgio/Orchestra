import { IFilmato, NewFilmato } from './filmato.model';

export const sampleWithRequiredData: IFilmato = {
  id: 24971,
};

export const sampleWithPartialData: IFilmato = {
  id: 5553,
};

export const sampleWithFullData: IFilmato = {
  id: 29800,
  blob: '../fake-data/blob/hipster.png',
  blobContentType: 'unknown',
  nome_file: 'truthful supposing ack',
};

export const sampleWithNewData: NewFilmato = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
