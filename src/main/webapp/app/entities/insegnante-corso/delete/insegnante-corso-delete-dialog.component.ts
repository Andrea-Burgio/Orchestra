import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInsegnanteCorso } from '../insegnante-corso.model';
import { InsegnanteCorsoService } from '../service/insegnante-corso.service';

@Component({
  standalone: true,
  templateUrl: './insegnante-corso-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InsegnanteCorsoDeleteDialogComponent {
  insegnanteCorso?: IInsegnanteCorso;

  protected insegnanteCorsoService = inject(InsegnanteCorsoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.insegnanteCorsoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
