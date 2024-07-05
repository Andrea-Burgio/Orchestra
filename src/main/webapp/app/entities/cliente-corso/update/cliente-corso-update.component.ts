import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { ICorso } from 'app/entities/corso/corso.model';
import { CorsoService } from 'app/entities/corso/service/corso.service';
import { ClienteCorsoService } from '../service/cliente-corso.service';
import { IClienteCorso } from '../cliente-corso.model';
import { ClienteCorsoFormService, ClienteCorsoFormGroup } from './cliente-corso-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cliente-corso-update',
  templateUrl: './cliente-corso-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClienteCorsoUpdateComponent implements OnInit {
  isSaving = false;
  clienteCorso: IClienteCorso | null = null;

  clientesSharedCollection: ICliente[] = [];
  corsosSharedCollection: ICorso[] = [];

  protected clienteCorsoService = inject(ClienteCorsoService);
  protected clienteCorsoFormService = inject(ClienteCorsoFormService);
  protected clienteService = inject(ClienteService);
  protected corsoService = inject(CorsoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ClienteCorsoFormGroup = this.clienteCorsoFormService.createClienteCorsoFormGroup();

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  compareCorso = (o1: ICorso | null, o2: ICorso | null): boolean => this.corsoService.compareCorso(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clienteCorso }) => {
      this.clienteCorso = clienteCorso;
      if (clienteCorso) {
        this.updateForm(clienteCorso);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clienteCorso = this.clienteCorsoFormService.getClienteCorso(this.editForm);
    if (clienteCorso.id !== null) {
      this.subscribeToSaveResponse(this.clienteCorsoService.update(clienteCorso));
    } else {
      this.subscribeToSaveResponse(this.clienteCorsoService.create(clienteCorso));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClienteCorso>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(clienteCorso: IClienteCorso): void {
    this.clienteCorso = clienteCorso;
    this.clienteCorsoFormService.resetForm(this.editForm, clienteCorso);

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      clienteCorso.cliente,
    );
    this.corsosSharedCollection = this.corsoService.addCorsoToCollectionIfMissing<ICorso>(this.corsosSharedCollection, clienteCorso.corso);
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.clienteCorso?.cliente)),
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));

    this.corsoService
      .query()
      .pipe(map((res: HttpResponse<ICorso[]>) => res.body ?? []))
      .pipe(map((corsos: ICorso[]) => this.corsoService.addCorsoToCollectionIfMissing<ICorso>(corsos, this.clienteCorso?.corso)))
      .subscribe((corsos: ICorso[]) => (this.corsosSharedCollection = corsos));
  }
}
