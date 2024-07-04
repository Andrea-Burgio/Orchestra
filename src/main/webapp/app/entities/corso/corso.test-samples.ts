import { ICorso, NewCorso } from './corso.model';

export const sampleWithRequiredData: ICorso = {
  id: 19738,
};

export const sampleWithPartialData: ICorso = {
  id: 10920,
};

export const sampleWithFullData: ICorso = {
  id: 3529,
  anno: 16275,
  nome: 'lest indeed',
};

export const sampleWithNewData: NewCorso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
