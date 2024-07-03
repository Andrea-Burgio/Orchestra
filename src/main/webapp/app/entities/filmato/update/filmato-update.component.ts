import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { FilmatoService } from '../service/filmato.service';
import { IFilmato } from '../filmato.model';
import { FilmatoFormService, FilmatoFormGroup } from './filmato-form.service';

@Component({
  standalone: true,
  selector: 'jhi-filmato-update',
  templateUrl: './filmato-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FilmatoUpdateComponent implements OnInit {
  isSaving = false;
  filmato: IFilmato | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected filmatoService = inject(FilmatoService);
  protected filmatoFormService = inject(FilmatoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FilmatoFormGroup = this.filmatoFormService.createFilmatoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filmato }) => {
      this.filmato = filmato;
      if (filmato) {
        this.updateForm(filmato);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('orchestraApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const filmato = this.filmatoFormService.getFilmato(this.editForm);
    if (filmato.id !== null) {
      this.subscribeToSaveResponse(this.filmatoService.update(filmato));
    } else {
      this.subscribeToSaveResponse(this.filmatoService.create(filmato));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFilmato>>): void {
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

  protected updateForm(filmato: IFilmato): void {
    this.filmato = filmato;
    this.filmatoFormService.resetForm(this.editForm, filmato);
  }
}
