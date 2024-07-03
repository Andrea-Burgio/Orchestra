import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

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

  protected concertoService = inject(ConcertoService);
  protected concertoFormService = inject(ConcertoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConcertoFormGroup = this.concertoFormService.createConcertoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ concerto }) => {
      this.concerto = concerto;
      if (concerto) {
        this.updateForm(concerto);
      }
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
  }
}
