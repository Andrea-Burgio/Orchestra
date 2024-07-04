import { IInsegnante, NewInsegnante } from './insegnante.model';

export const sampleWithRequiredData: IInsegnante = {
  id: 11485,
};

export const sampleWithPartialData: IInsegnante = {
  id: 29527,
  cognome: 'club anenst',
};

export const sampleWithFullData: IInsegnante = {
  id: 3650,
  nome: 'round despite',
  cognome: 'questionable',
};

export const sampleWithNewData: NewInsegnante = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
