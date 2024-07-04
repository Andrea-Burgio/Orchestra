import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { InsegnanteCorsoDetailComponent } from './insegnante-corso-detail.component';

describe('InsegnanteCorso Management Detail Component', () => {
  let comp: InsegnanteCorsoDetailComponent;
  let fixture: ComponentFixture<InsegnanteCorsoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InsegnanteCorsoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InsegnanteCorsoDetailComponent,
              resolve: { insegnanteCorso: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InsegnanteCorsoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InsegnanteCorsoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load insegnanteCorso on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InsegnanteCorsoDetailComponent);

      // THEN
      expect(instance.insegnanteCorso()).toEqual(expect.objectContaining({ id: 123 }));
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
