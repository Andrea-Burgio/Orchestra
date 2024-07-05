import { ICliente } from 'app/entities/cliente/cliente.model';
import { ICorso } from 'app/entities/corso/corso.model';

export interface IClienteCorso {
  id: number;
  mese?: number | null;
  pagato?: boolean | null;
  cliente?: ICliente | null;
  corso?: ICorso | null;
}

export type NewClienteCorso = Omit<IClienteCorso, 'id'> & { id: null };
