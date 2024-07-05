import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IClienteCorso } from '../cliente-corso.model';

@Component({
  standalone: true,
  selector: 'jhi-cliente-corso-detail',
  templateUrl: './cliente-corso-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ClienteCorsoDetailComponent {
  clienteCorso = input<IClienteCorso | null>(null);

  previousState(): void {
    window.history.back();
  }
}
