import { ICorso, NewCorso } from './corso.model';

export const sampleWithRequiredData: ICorso = {
  id: 30269,
};

export const sampleWithPartialData: ICorso = {
  id: 6493,
  anno: 9985,
};

export const sampleWithFullData: ICorso = {
  id: 28775,
  anno: 13786,
  nome: 'how magnificent of',
};

export const sampleWithNewData: NewCorso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
