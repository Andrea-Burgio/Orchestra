import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICorso } from '../corso.model';
import { CorsoService } from '../service/corso.service';

@Component({
  standalone: true,
  templateUrl: './corso-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CorsoDeleteDialogComponent {
  corso?: ICorso;

  protected corsoService = inject(CorsoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.corsoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
