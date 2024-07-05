import { IFoto, NewFoto } from './foto.model';

export const sampleWithRequiredData: IFoto = {
  id: 11297,
};

export const sampleWithPartialData: IFoto = {
  id: 11417,
};

export const sampleWithFullData: IFoto = {
  id: 19464,
  blob: '../fake-data/blob/hipster.png',
  blobContentType: 'unknown',
  nome_file: 'controvert indeed penalise',
};

export const sampleWithNewData: NewFoto = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
