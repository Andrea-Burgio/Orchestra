import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { InsegnanteDetailComponent } from './insegnante-detail.component';

describe('Insegnante Management Detail Component', () => {
  let comp: InsegnanteDetailComponent;
  let fixture: ComponentFixture<InsegnanteDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InsegnanteDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InsegnanteDetailComponent,
              resolve: { insegnante: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InsegnanteDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InsegnanteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load insegnante on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InsegnanteDetailComponent);

      // THEN
      expect(instance.insegnante()).toEqual(expect.objectContaining({ id: 123 }));
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
