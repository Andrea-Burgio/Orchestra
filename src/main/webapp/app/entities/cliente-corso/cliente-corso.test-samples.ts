import { IClienteCorso, NewClienteCorso } from './cliente-corso.model';

export const sampleWithRequiredData: IClienteCorso = {
  id: 4516,
};

export const sampleWithPartialData: IClienteCorso = {
  id: 6777,
  pagato: true,
};

export const sampleWithFullData: IClienteCorso = {
  id: 22607,
  mese: 32309,
  pagato: true,
};

export const sampleWithNewData: NewClienteCorso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
