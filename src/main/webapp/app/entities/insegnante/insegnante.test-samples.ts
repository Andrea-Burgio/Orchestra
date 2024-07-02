import { IInsegnante, NewInsegnante } from './insegnante.model';

export const sampleWithRequiredData: IInsegnante = {
  id: 20345,
};

export const sampleWithPartialData: IInsegnante = {
  id: 2463,
  cognome: 'absolve afore yippee',
};

export const sampleWithFullData: IInsegnante = {
  id: 6520,
  nome: 'marvelous neuropathologist',
  cognome: 'unless upset',
};

export const sampleWithNewData: NewInsegnante = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
