import { ICorso, NewCorso } from './corso.model';

export const sampleWithRequiredData: ICorso = {
  id: 16755,
};

export const sampleWithPartialData: ICorso = {
  id: 5904,
  anno: 4378,
  nome: 'outstanding',
};

export const sampleWithFullData: ICorso = {
  id: 26554,
  anno: 30126,
  nome: 'sleepy than billboard',
};

export const sampleWithNewData: NewCorso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
