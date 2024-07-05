import { ICliente } from 'app/entities/cliente/cliente.model';

export interface IClienteCorso {
  id: number;
  mese?: number | null;
  pagato?: boolean | null;
  cliente?: ICliente | null;
}

export type NewClienteCorso = Omit<IClienteCorso, 'id'> & { id: null };
