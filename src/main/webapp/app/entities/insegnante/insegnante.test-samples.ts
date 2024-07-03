import { IInsegnante, NewInsegnante } from './insegnante.model';

export const sampleWithRequiredData: IInsegnante = {
  id: 10774,
};

export const sampleWithPartialData: IInsegnante = {
  id: 27798,
  nome: 'sound',
  cognome: 'giant yet',
};

export const sampleWithFullData: IInsegnante = {
  id: 30060,
  nome: 'of surpass',
  cognome: 'riot on',
};

export const sampleWithNewData: NewInsegnante = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
