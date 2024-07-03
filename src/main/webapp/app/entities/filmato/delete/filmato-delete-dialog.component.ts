import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFilmato } from '../filmato.model';
import { FilmatoService } from '../service/filmato.service';

@Component({
  standalone: true,
  templateUrl: './filmato-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FilmatoDeleteDialogComponent {
  filmato?: IFilmato;

  protected filmatoService = inject(FilmatoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.filmatoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
