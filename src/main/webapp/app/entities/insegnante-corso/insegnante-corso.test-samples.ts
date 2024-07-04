import { IInsegnanteCorso, NewInsegnanteCorso } from './insegnante-corso.model';

export const sampleWithRequiredData: IInsegnanteCorso = {
  id: 19082,
};

export const sampleWithPartialData: IInsegnanteCorso = {
  id: 30662,
};

export const sampleWithFullData: IInsegnanteCorso = {
  id: 28539,
  mese: 28117,
};

export const sampleWithNewData: NewInsegnanteCorso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
