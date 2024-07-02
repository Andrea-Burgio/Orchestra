import { ICorso, NewCorso } from './corso.model';

export const sampleWithRequiredData: ICorso = {
  id: 12612,
};

export const sampleWithPartialData: ICorso = {
  id: 10041,
  nome: 'fax smite',
};

export const sampleWithFullData: ICorso = {
  id: 5832,
  anno: 9288,
  nome: 'centre amidst',
};

export const sampleWithNewData: NewCorso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
