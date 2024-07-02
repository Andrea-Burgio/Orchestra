import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 4790,
};

export const sampleWithPartialData: ICliente = {
  id: 28008,
  nome: 'delivery extract',
};

export const sampleWithFullData: ICliente = {
  id: 81,
  nome: 'when but phooey',
  cognome: 'frenetically',
};

export const sampleWithNewData: NewCliente = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
