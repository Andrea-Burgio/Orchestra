import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFoto } from '../foto.model';
import { FotoService } from '../service/foto.service';

@Component({
  standalone: true,
  templateUrl: './foto-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FotoDeleteDialogComponent {
  foto?: IFoto;

  protected fotoService = inject(FotoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fotoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
