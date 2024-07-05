import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICorso } from 'app/entities/corso/corso.model';
import { CorsoService } from 'app/entities/corso/service/corso.service';
import { IConcerto } from '../concerto.model';
import { ConcertoService } from '../service/concerto.service';
import { ConcertoFormService, ConcertoFormGroup } from './concerto-form.service';

@Component({
  standalone: true,
  selector: 'jhi-concerto-update',
  templateUrl: './concerto-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConcertoUpdateComponent implements OnInit {
  isSaving = false;
  concerto: IConcerto | null = null;

  corsosSharedCollection: ICorso[] = [];

  protected concertoService = inject(ConcertoService);
  protected concertoFormService = inject(ConcertoFormService);
  protected corsoService = inject(CorsoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConcertoFormGroup = this.concertoFormService.createConcertoFormGroup();

  compareCorso = (o1: ICorso | null, o2: ICorso | null): boolean => this.corsoService.compareCorso(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ concerto }) => {
      this.concerto = concerto;
      if (concerto) {
        this.updateForm(concerto);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const concerto = this.concertoFormService.getConcerto(this.editForm);
    if (concerto.id !== null) {
      this.subscribeToSaveResponse(this.concertoService.update(concerto));
    } else {
      this.subscribeToSaveResponse(this.concertoService.create(concerto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConcerto>>): void {
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

  protected updateForm(concerto: IConcerto): void {
    this.concerto = concerto;
    this.concertoFormService.resetForm(this.editForm, concerto);

    this.corsosSharedCollection = this.corsoService.addCorsoToCollectionIfMissing<ICorso>(this.corsosSharedCollection, concerto.corso);
  }

  protected loadRelationshipsOptions(): void {
    this.corsoService
      .query()
      .pipe(map((res: HttpResponse<ICorso[]>) => res.body ?? []))
      .pipe(map((corsos: ICorso[]) => this.corsoService.addCorsoToCollectionIfMissing<ICorso>(corsos, this.concerto?.corso)))
      .subscribe((corsos: ICorso[]) => (this.corsosSharedCollection = corsos));
  }
}
