import { IInsegnante, NewInsegnante } from './insegnante.model';

export const sampleWithRequiredData: IInsegnante = {
  id: 27162,
};

export const sampleWithPartialData: IInsegnante = {
  id: 28546,
};

export const sampleWithFullData: IInsegnante = {
  id: 16662,
  nome: 'radicalise plump during',
  cognome: 'female',
};

export const sampleWithNewData: NewInsegnante = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
