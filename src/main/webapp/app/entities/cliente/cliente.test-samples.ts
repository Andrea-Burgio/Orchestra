import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 11170,
};

export const sampleWithPartialData: ICliente = {
  id: 30425,
  cognome: 'worth firm',
};

export const sampleWithFullData: ICliente = {
  id: 8355,
  nome: 'aboard geez than',
  cognome: 'apropos',
};

export const sampleWithNewData: NewCliente = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
