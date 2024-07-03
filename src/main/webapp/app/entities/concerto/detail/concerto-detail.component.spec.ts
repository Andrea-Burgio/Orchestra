import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConcertoDetailComponent } from './concerto-detail.component';

describe('Concerto Management Detail Component', () => {
  let comp: ConcertoDetailComponent;
  let fixture: ComponentFixture<ConcertoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConcertoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ConcertoDetailComponent,
              resolve: { concerto: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ConcertoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConcertoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load concerto on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ConcertoDetailComponent);

      // THEN
      expect(instance.concerto()).toEqual(expect.objectContaining({ id: 123 }));
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
