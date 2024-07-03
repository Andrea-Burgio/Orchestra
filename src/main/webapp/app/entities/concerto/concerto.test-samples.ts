import dayjs from 'dayjs/esm';

import { IConcerto, NewConcerto } from './concerto.model';

export const sampleWithRequiredData: IConcerto = {
  id: 27318,
};

export const sampleWithPartialData: IConcerto = {
  id: 31183,
};

export const sampleWithFullData: IConcerto = {
  id: 28383,
  data: dayjs('2024-07-02'),
  nome: 'divalent',
};

export const sampleWithNewData: NewConcerto = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
