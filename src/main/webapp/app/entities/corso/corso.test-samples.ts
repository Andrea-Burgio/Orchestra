import { ICorso, NewCorso } from './corso.model';

export const sampleWithRequiredData: ICorso = {
  id: 22501,
};

export const sampleWithPartialData: ICorso = {
  id: 21737,
  nome: 'worth',
};

export const sampleWithFullData: ICorso = {
  id: 19585,
  anno: 18467,
  nome: 'dragon',
};

export const sampleWithNewData: NewCorso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
