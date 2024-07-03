import { ICorso, NewCorso } from './corso.model';

export const sampleWithRequiredData: ICorso = {
  id: 25920,
};

export const sampleWithPartialData: ICorso = {
  id: 22928,
  anno: 30916,
};

export const sampleWithFullData: ICorso = {
  id: 18598,
  anno: 18012,
  nome: 'separately',
};

export const sampleWithNewData: NewCorso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
