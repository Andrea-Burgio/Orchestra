import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInsegnante } from 'app/entities/insegnante/insegnante.model';
import { InsegnanteService } from 'app/entities/insegnante/service/insegnante.service';
import { ICorso } from 'app/entities/corso/corso.model';
import { CorsoService } from 'app/entities/corso/service/corso.service';
import { InsegnanteCorsoService } from '../service/insegnante-corso.service';
import { IInsegnanteCorso } from '../insegnante-corso.model';
import { InsegnanteCorsoFormService, InsegnanteCorsoFormGroup } from './insegnante-corso-form.service';

@Component({
  standalone: true,
  selector: 'jhi-insegnante-corso-update',
  templateUrl: './insegnante-corso-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InsegnanteCorsoUpdateComponent implements OnInit {
  isSaving = false;
  insegnanteCorso: IInsegnanteCorso | null = null;

  insegnantesSharedCollection: IInsegnante[] = [];
  corsosSharedCollection: ICorso[] = [];

  protected insegnanteCorsoService = inject(InsegnanteCorsoService);
  protected insegnanteCorsoFormService = inject(InsegnanteCorsoFormService);
  protected insegnanteService = inject(InsegnanteService);
  protected corsoService = inject(CorsoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InsegnanteCorsoFormGroup = this.insegnanteCorsoFormService.createInsegnanteCorsoFormGroup();

  compareInsegnante = (o1: IInsegnante | null, o2: IInsegnante | null): boolean => this.insegnanteService.compareInsegnante(o1, o2);

  compareCorso = (o1: ICorso | null, o2: ICorso | null): boolean => this.corsoService.compareCorso(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ insegnanteCorso }) => {
      this.insegnanteCorso = insegnanteCorso;
      if (insegnanteCorso) {
        this.updateForm(insegnanteCorso);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const insegnanteCorso = this.insegnanteCorsoFormService.getInsegnanteCorso(this.editForm);
    if (insegnanteCorso.id !== null) {
      this.subscribeToSaveResponse(this.insegnanteCorsoService.update(insegnanteCorso));
    } else {
      this.subscribeToSaveResponse(this.insegnanteCorsoService.create(insegnanteCorso));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInsegnanteCorso>>): void {
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

  protected updateForm(insegnanteCorso: IInsegnanteCorso): void {
    this.insegnanteCorso = insegnanteCorso;
    this.insegnanteCorsoFormService.resetForm(this.editForm, insegnanteCorso);

    this.insegnantesSharedCollection = this.insegnanteService.addInsegnanteToCollectionIfMissing<IInsegnante>(
      this.insegnantesSharedCollection,
      insegnanteCorso.insegnante,
    );
    this.corsosSharedCollection = this.corsoService.addCorsoToCollectionIfMissing<ICorso>(
      this.corsosSharedCollection,
      insegnanteCorso.corso,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.insegnanteService
      .query()
      .pipe(map((res: HttpResponse<IInsegnante[]>) => res.body ?? []))
      .pipe(
        map((insegnantes: IInsegnante[]) =>
          this.insegnanteService.addInsegnanteToCollectionIfMissing<IInsegnante>(insegnantes, this.insegnanteCorso?.insegnante),
        ),
      )
      .subscribe((insegnantes: IInsegnante[]) => (this.insegnantesSharedCollection = insegnantes));

    this.corsoService
      .query()
      .pipe(map((res: HttpResponse<ICorso[]>) => res.body ?? []))
      .pipe(map((corsos: ICorso[]) => this.corsoService.addCorsoToCollectionIfMissing<ICorso>(corsos, this.insegnanteCorso?.corso)))
      .subscribe((corsos: ICorso[]) => (this.corsosSharedCollection = corsos));
  }
}
