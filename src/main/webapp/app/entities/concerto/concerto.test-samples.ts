import dayjs from 'dayjs/esm';

import { IConcerto, NewConcerto } from './concerto.model';

export const sampleWithRequiredData: IConcerto = {
  id: 10638,
};

export const sampleWithPartialData: IConcerto = {
  id: 15421,
};

export const sampleWithFullData: IConcerto = {
  id: 20563,
  data: dayjs('2024-07-02'),
  nome: 'abnormally fooey grandparent',
};

export const sampleWithNewData: NewConcerto = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
