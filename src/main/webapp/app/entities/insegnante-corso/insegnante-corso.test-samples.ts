import { IInsegnanteCorso, NewInsegnanteCorso } from './insegnante-corso.model';

export const sampleWithRequiredData: IInsegnanteCorso = {
  id: 4824,
};

export const sampleWithPartialData: IInsegnanteCorso = {
  id: 7063,
  mese: 3504,
};

export const sampleWithFullData: IInsegnanteCorso = {
  id: 22802,
  mese: 4747,
};

export const sampleWithNewData: NewInsegnanteCorso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
