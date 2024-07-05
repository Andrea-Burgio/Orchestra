import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 13162,
};

export const sampleWithPartialData: ICliente = {
  id: 10886,
  nome: 'disastrous oof hive',
  cognome: 'shuffle than stake',
};

export const sampleWithFullData: ICliente = {
  id: 4160,
  nome: 'gently speedily gracefully',
  cognome: 'where after',
};

export const sampleWithNewData: NewCliente = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
