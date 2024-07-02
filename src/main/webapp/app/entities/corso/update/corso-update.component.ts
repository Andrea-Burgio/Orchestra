import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICorso } from '../corso.model';
import { CorsoService } from '../service/corso.service';
import { CorsoFormService, CorsoFormGroup } from './corso-form.service';

@Component({
  standalone: true,
  selector: 'jhi-corso-update',
  templateUrl: './corso-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CorsoUpdateComponent implements OnInit {
  isSaving = false;
  corso: ICorso | null = null;

  protected corsoService = inject(CorsoService);
  protected corsoFormService = inject(CorsoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CorsoFormGroup = this.corsoFormService.createCorsoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ corso }) => {
      this.corso = corso;
      if (corso) {
        this.updateForm(corso);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const corso = this.corsoFormService.getCorso(this.editForm);
    if (corso.id !== null) {
      this.subscribeToSaveResponse(this.corsoService.update(corso));
    } else {
      this.subscribeToSaveResponse(this.corsoService.create(corso));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICorso>>): void {
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

  protected updateForm(corso: ICorso): void {
    this.corso = corso;
    this.corsoFormService.resetForm(this.editForm, corso);
  }
}
