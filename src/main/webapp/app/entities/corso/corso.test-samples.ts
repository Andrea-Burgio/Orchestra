import { ICorso, NewCorso } from './corso.model';

export const sampleWithRequiredData: ICorso = {
  id: 5995,
};

export const sampleWithPartialData: ICorso = {
  id: 29898,
  anno: 27893,
  nome: 'gah bitterly',
};

export const sampleWithFullData: ICorso = {
  id: 10036,
  anno: 7548,
  nome: 'though littleneck',
};

export const sampleWithNewData: NewCorso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
