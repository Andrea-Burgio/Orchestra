import { IInsegnante, NewInsegnante } from './insegnante.model';

export const sampleWithRequiredData: IInsegnante = {
  id: 456,
};

export const sampleWithPartialData: IInsegnante = {
  id: 22946,
  cognome: 'warmly bright',
};

export const sampleWithFullData: IInsegnante = {
  id: 14364,
  nome: 'by bah',
  cognome: 'hence',
};

export const sampleWithNewData: NewInsegnante = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
