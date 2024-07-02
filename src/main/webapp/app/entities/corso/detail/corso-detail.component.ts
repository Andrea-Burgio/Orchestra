import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICorso } from '../corso.model';

@Component({
  standalone: true,
  selector: 'jhi-corso-detail',
  templateUrl: './corso-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CorsoDetailComponent {
  corso = input<ICorso | null>(null);

  previousState(): void {
    window.history.back();
  }
}
