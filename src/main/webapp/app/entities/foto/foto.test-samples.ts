import { IFoto, NewFoto } from './foto.model';

export const sampleWithRequiredData: IFoto = {
  id: 18591,
};

export const sampleWithPartialData: IFoto = {
  id: 18673,
  nome_file: 'flounder',
};

export const sampleWithFullData: IFoto = {
  id: 27219,
  blob: '../fake-data/blob/hipster.png',
  blobContentType: 'unknown',
  nome_file: 'yet',
};

export const sampleWithNewData: NewFoto = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
