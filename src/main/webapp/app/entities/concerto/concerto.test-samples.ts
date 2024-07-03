import dayjs from 'dayjs/esm';

import { IConcerto, NewConcerto } from './concerto.model';

export const sampleWithRequiredData: IConcerto = {
  id: 31636,
};

export const sampleWithPartialData: IConcerto = {
  id: 6468,
  data: dayjs('2024-07-03'),
  nome: 'gratefully brr',
};

export const sampleWithFullData: IConcerto = {
  id: 7727,
  data: dayjs('2024-07-02'),
  nome: 'on',
};

export const sampleWithNewData: NewConcerto = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
