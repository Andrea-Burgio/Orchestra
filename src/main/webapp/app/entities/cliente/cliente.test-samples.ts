import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 6138,
};

export const sampleWithPartialData: ICliente = {
  id: 11381,
  nome: 'spanish corn unless',
};

export const sampleWithFullData: ICliente = {
  id: 11272,
  nome: 'solidarity usable',
  cognome: 'zowie around',
};

export const sampleWithNewData: NewCliente = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
