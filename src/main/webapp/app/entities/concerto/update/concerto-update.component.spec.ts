import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ConcertoService } from '../service/concerto.service';
import { IConcerto } from '../concerto.model';
import { ConcertoFormService } from './concerto-form.service';

import { ConcertoUpdateComponent } from './concerto-update.component';

describe('Concerto Management Update Component', () => {
  let comp: ConcertoUpdateComponent;
  let fixture: ComponentFixture<ConcertoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let concertoFormService: ConcertoFormService;
  let concertoService: ConcertoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ConcertoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ConcertoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConcertoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    concertoFormService = TestBed.inject(ConcertoFormService);
    concertoService = TestBed.inject(ConcertoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const concerto: IConcerto = { id: 456 };

      activatedRoute.data = of({ concerto });
      comp.ngOnInit();

      expect(comp.concerto).toEqual(concerto);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConcerto>>();
      const concerto = { id: 123 };
      jest.spyOn(concertoFormService, 'getConcerto').mockReturnValue(concerto);
      jest.spyOn(concertoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ concerto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: concerto }));
      saveSubject.complete();

      // THEN
      expect(concertoFormService.getConcerto).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(concertoService.update).toHaveBeenCalledWith(expect.objectContaining(concerto));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConcerto>>();
      const concerto = { id: 123 };
      jest.spyOn(concertoFormService, 'getConcerto').mockReturnValue({ id: null });
      jest.spyOn(concertoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ concerto: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: concerto }));
      saveSubject.complete();

      // THEN
      expect(concertoFormService.getConcerto).toHaveBeenCalled();
      expect(concertoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConcerto>>();
      const concerto = { id: 123 };
      jest.spyOn(concertoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ concerto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(concertoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
