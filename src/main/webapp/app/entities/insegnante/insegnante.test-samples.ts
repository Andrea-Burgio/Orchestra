import { IInsegnante, NewInsegnante } from './insegnante.model';

export const sampleWithRequiredData: IInsegnante = {
  id: 13609,
};

export const sampleWithPartialData: IInsegnante = {
  id: 7784,
  nome: 'aha',
};

export const sampleWithFullData: IInsegnante = {
  id: 26507,
  nome: 'clumsy complement succour',
  cognome: 'midst',
};

export const sampleWithNewData: NewInsegnante = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
