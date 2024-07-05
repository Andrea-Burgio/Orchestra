import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ClienteCorsoDetailComponent } from './cliente-corso-detail.component';

describe('ClienteCorso Management Detail Component', () => {
  let comp: ClienteCorsoDetailComponent;
  let fixture: ComponentFixture<ClienteCorsoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClienteCorsoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ClienteCorsoDetailComponent,
              resolve: { clienteCorso: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ClienteCorsoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClienteCorsoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load clienteCorso on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ClienteCorsoDetailComponent);

      // THEN
      expect(instance.clienteCorso()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
