import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 4607,
};

export const sampleWithPartialData: ICliente = {
  id: 30596,
  nome: 'normalise',
  cognome: 'closely unnatural',
};

export const sampleWithFullData: ICliente = {
  id: 9217,
  nome: 'wherever behind though',
  cognome: 'rightfully',
};

export const sampleWithNewData: NewCliente = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
