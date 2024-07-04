import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IInsegnanteCorso } from '../insegnante-corso.model';

@Component({
  standalone: true,
  selector: 'jhi-insegnante-corso-detail',
  templateUrl: './insegnante-corso-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class InsegnanteCorsoDetailComponent {
  insegnanteCorso = input<IInsegnanteCorso | null>(null);

  previousState(): void {
    window.history.back();
  }
}
