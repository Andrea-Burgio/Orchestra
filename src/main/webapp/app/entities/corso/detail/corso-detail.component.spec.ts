import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CorsoDetailComponent } from './corso-detail.component';

describe('Corso Management Detail Component', () => {
  let comp: CorsoDetailComponent;
  let fixture: ComponentFixture<CorsoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CorsoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CorsoDetailComponent,
              resolve: { corso: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CorsoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CorsoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load corso on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CorsoDetailComponent);

      // THEN
      expect(instance.corso()).toEqual(expect.objectContaining({ id: 123 }));
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
