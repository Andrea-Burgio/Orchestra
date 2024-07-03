import { IInsegnante, NewInsegnante } from './insegnante.model';

export const sampleWithRequiredData: IInsegnante = {
  id: 4502,
};

export const sampleWithPartialData: IInsegnante = {
  id: 13747,
};

export const sampleWithFullData: IInsegnante = {
  id: 10092,
  nome: 'bah',
  cognome: 'but brr gosh',
};

export const sampleWithNewData: NewInsegnante = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
