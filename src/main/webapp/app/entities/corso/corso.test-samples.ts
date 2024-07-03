import { ICorso, NewCorso } from './corso.model';

export const sampleWithRequiredData: ICorso = {
  id: 5821,
};

export const sampleWithPartialData: ICorso = {
  id: 27893,
  anno: 29674,
  nome: 'blindly',
};

export const sampleWithFullData: ICorso = {
  id: 21906,
  anno: 28256,
  nome: 'if how',
};

export const sampleWithNewData: NewCorso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
