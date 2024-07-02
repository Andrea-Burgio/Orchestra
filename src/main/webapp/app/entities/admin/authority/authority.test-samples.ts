import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '5436bc52-5658-4083-9b86-11d47c86ef0b',
};

export const sampleWithPartialData: IAuthority = {
  name: 'e540f4c4-69ed-4b89-b0c0-5dadaed3c0d6',
};

export const sampleWithFullData: IAuthority = {
  name: 'b8f9c6b8-8e29-4cc5-b720-d2f5baf5fabd',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
