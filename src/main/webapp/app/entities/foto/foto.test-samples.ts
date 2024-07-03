import { IFoto, NewFoto } from './foto.model';

export const sampleWithRequiredData: IFoto = {
  id: 20525,
};

export const sampleWithPartialData: IFoto = {
  id: 10332,
};

export const sampleWithFullData: IFoto = {
  id: 13248,
  blob: '../fake-data/blob/hipster.png',
  blobContentType: 'unknown',
  nome_file: 'stage speedy',
};

export const sampleWithNewData: NewFoto = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
