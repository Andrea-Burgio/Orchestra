import dayjs from 'dayjs/esm';

import { IConcerto, NewConcerto } from './concerto.model';

export const sampleWithRequiredData: IConcerto = {
  id: 20911,
};

export const sampleWithPartialData: IConcerto = {
  id: 15357,
  nome: 'athwart',
};

export const sampleWithFullData: IConcerto = {
  id: 19411,
  data: dayjs('2024-07-02'),
  nome: 'boohoo quarrelsomely',
};

export const sampleWithNewData: NewConcerto = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
