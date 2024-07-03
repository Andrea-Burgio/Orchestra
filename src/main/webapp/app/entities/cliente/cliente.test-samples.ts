import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 2521,
};

export const sampleWithPartialData: ICliente = {
  id: 14372,
  cognome: 'embarrass',
};

export const sampleWithFullData: ICliente = {
  id: 23868,
  nome: 'lightly resign',
  cognome: 'boo resolve',
};

export const sampleWithNewData: NewCliente = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
